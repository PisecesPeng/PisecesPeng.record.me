<h2> 常见MySQL语句优化笔记 </h2>

- [1. 合理创建索引](#1-%e5%90%88%e7%90%86%e5%88%9b%e5%bb%ba%e7%b4%a2%e5%bc%95)
- [2. SQL语句中IN包含的值不应过多](#2-sql%e8%af%ad%e5%8f%a5%e4%b8%adin%e5%8c%85%e5%90%ab%e7%9a%84%e5%80%bc%e4%b8%8d%e5%ba%94%e8%bf%87%e5%a4%9a)
- [3. 区分'IN'和'EXISTS', 'NOT IN'和'NOT EXISTS'](#3-%e5%8c%ba%e5%88%86in%e5%92%8cexists-not-in%e5%92%8cnot-exists)
- [4. 注意范围查询语句](#4-%e6%b3%a8%e6%84%8f%e8%8c%83%e5%9b%b4%e6%9f%a5%e8%af%a2%e8%af%ad%e5%8f%a5)
- [5. 避免使用'!='或'<>'操作符](#5-%e9%81%bf%e5%85%8d%e4%bd%bf%e7%94%a8%e6%88%96%e6%93%8d%e4%bd%9c%e7%ac%a6)
- [6. 避免使用'OR'连接条件](#6-%e9%81%bf%e5%85%8d%e4%bd%bf%e7%94%a8or%e8%bf%9e%e6%8e%a5%e6%9d%a1%e4%bb%b6)
- [7. 尽量用'UNION ALL'代替'UNION'](#7-%e5%b0%bd%e9%87%8f%e7%94%a8union-all%e4%bb%a3%e6%9b%bfunion)
- [8. 进行'is null'值和'is not null'判断都不可取](#8-%e8%bf%9b%e8%a1%8cis-null%e5%80%bc%e5%92%8cis-not-null%e5%88%a4%e6%96%ad%e9%83%bd%e4%b8%8d%e5%8f%af%e5%8f%96)
- [9. 'SELECT'语句务必指明字段名称](#9-select%e8%af%ad%e5%8f%a5%e5%8a%a1%e5%bf%85%e6%8c%87%e6%98%8e%e5%ad%97%e6%ae%b5%e5%90%8d%e7%a7%b0)
- [10. 适当使用'LIMIT 1'](#10-%e9%80%82%e5%bd%93%e4%bd%bf%e7%94%a8limit-1)
- [11. 不要在'where'子句中的'='左边进行计算](#11-%e4%b8%8d%e8%a6%81%e5%9c%a8where%e5%ad%90%e5%8f%a5%e4%b8%ad%e7%9a%84%e5%b7%a6%e8%be%b9%e8%bf%9b%e8%a1%8c%e8%ae%a1%e7%ae%97)
- [12. 避免隐式类型转换](#12-%e9%81%bf%e5%85%8d%e9%9a%90%e5%bc%8f%e7%b1%bb%e5%9e%8b%e8%bd%ac%e6%8d%a2)
- [13. 对于联合索引来说, 要遵守最左前缀法则](#13-%e5%af%b9%e4%ba%8e%e8%81%94%e5%90%88%e7%b4%a2%e5%bc%95%e6%9d%a5%e8%af%b4-%e8%a6%81%e9%81%b5%e5%ae%88%e6%9c%80%e5%b7%a6%e5%89%8d%e7%bc%80%e6%b3%95%e5%88%99)
- [14. 不建议使用'%'前缀模糊查询](#14-%e4%b8%8d%e5%bb%ba%e8%ae%ae%e4%bd%bf%e7%94%a8%e5%89%8d%e7%bc%80%e6%a8%a1%e7%b3%8a%e6%9f%a5%e8%af%a2)
- [15. 用'EXISTS'替换'DISTINCT'](#15-%e7%94%a8exists%e6%9b%bf%e6%8d%a2distinct)
- [16. 'DISTINCT'与'GROUP BY'的去重](#16-distinct%e4%b8%8egroup-by%e7%9a%84%e5%8e%bb%e9%87%8d)
- [17. 关于'JOIN'优化](#17-%e5%85%b3%e4%ba%8ejoin%e4%bc%98%e5%8c%96)
- [18. 关于'LIMIT'优化](#18-%e5%85%b3%e4%ba%8elimit%e4%bc%98%e5%8c%96)
- [19. 建立索引的小建议](#19-%e5%bb%ba%e7%ab%8b%e7%b4%a2%e5%bc%95%e7%9a%84%e5%b0%8f%e5%bb%ba%e8%ae%ae)

<hr>

> MySQL语句的优化, 不得不提的'**EXPLAIN**', 善用'**EXPLAIN**'帮助我们查看SQL执行计划.<br/>
> 不展开赘述, 主要先关心以下字段: <br/>
>> **type**: 连接类型(all、index、range、ref、eq_ref、const、system、null(从左到右, 性能从差到好)).<br/>
>> **key**: 使用到的索引名, 如果没有选择索引, 值是NULL.可以采取强制索引方式(FORCE INDEX、USE INDEX或IGNORE INDEX).<br/>
>> **key_len**: 索引字段的最大可能长度, 并非实际使用长度(不损失精确性的情况下, 长度越短越好).<br/>
>> **rows**: 预估扫描行数.<br/>
>> **extra**: 详细说明(详请自行搜索).<br/>

<hr>

### 1. 合理创建索引

对查询进行优化, 应尽量避免全表扫描, <br/>
可以在'**WHERE**'及'**ORDER BY**'涉及的列上先建立索引.<br/>

<hr>

### 2. SQL语句中IN包含的值不应过多

MySQL对于'**IN**'做了相应的优化, <br/>
即, 将'**IN**'中的常量全部存储在一个数组里面, 且这个数组自动排序.<br/>
但是如果数值较多, 这样产生的消耗是比较大的.<br/>
<br/>
还要注意'**IN**'和'**NOT IN**'都要慎用, 否则会导致全表扫描.<br/>
例如: ``` select id from t where num in(1,2,3) ```<br/>
对于连续的数值, 能用'**BETWEEN**'就不要用'**IN**'了, 再或者使用连接来替换.<br/>

<hr>

### 3. 区分'IN'和'EXISTS', 'NOT IN'和'NOT EXISTS'

首先先看两条sql<br/>
``` select * from ta where id in (select id from tb) ```<br/>
等同于<br/>
``` select * from ta where exists (select * from tb where tb.id = ta.id) ```<br/>
区分'**IN**'和'**EXISTS**'性能变化, 主要是造成了驱动顺序的改变, <br/>
如果是'**EXISTS**', 那么以外层表为驱动表, 先被访问, <br/>
如果是'**IN**', 那么先执行子查询.<br/>
所以'**IN**'适合于外表大而内表小的情况;'**EXISTS**'适合于外表小而内表大的情况.<br/>
<br/>
关于'**NOT IN**'和'**NOT EXISTS**', 推荐使用'**NOT EXISTS**'.<br/>
'**NOT IN**'如果查询语句使用了'**NOT IN**'那么内外表都进行全表扫描, 并均未用到索引;<br/>
而'**NOT EXISTS**'的子查询依然能用到表上的索引.<br/>
无论哪个表大, 用'**NOT EXISTS**'都比'**NOT IN**'要快.<br/>

<hr>

### 4. 注意范围查询语句

对于联合索引来说, 如果存在范围查询,<br/>
比如'**BETWEEN**','**>**','**<**'等条件时, 会造成后面的索引字段失效.<br/>

<hr>

### 5. 避免使用'!='或'<>'操作符

应尽量避免在'**WHERE**'子句中使用'**!=**'或'**<>**'操作符, 否则将引擎放弃使用索引而进行全表扫描.<br/>
例如:<br/>
``` select * from t where id != 3000; ```<br/>
可以修改为<br/>
``` select * from t where id < 3000 or id > 3000; ```<br/>
虽然这两种查询的结果一样, 但是第二种查询方案会比第一种查询方案更快些.<br/>

<hr>

### 6. 避免使用'OR'连接条件

应尽量避免在'**WHERE**'子句中使用'**OR**'来连接条件, 否则将导致引擎放弃使用索引而进行全表扫描,<br/>
且'**OR**'两边的字段中, 如果有一个不是索引字段, 而其他条件也不是索引字段, 会造成该查询不走索引的情况<br/>
例如:<br/>
``` 
select id from t where num=10 or num=20 
```
可以这样查询:<br/>
```
select id from t where num=10
union all
select id from t where num=20
```

<hr>

### 7. 尽量用'UNION ALL'代替'UNION'

'**UNION**'和'**UNION ALL**'的差异主要是:<br/>
前者需要将结果集合并后再进行唯一性过滤操作, <br/>
这就会涉及到排序, 增加大量的CPU运算, 加大资源消耗及延迟.<br/>
当然, '**UNION ALL**'的前提条件是两个结果集没有重复数据.<br/>

<hr>

### 8. 进行'is null'值和'is not null'判断都不可取

应尽量避免在'**WHERE**'子句中对字段进行'null'值判断, 否则将导致引擎放弃使用索引而进行全表扫描,<br/> 
不能用'null'作索引, 任何包含'null'值的列都将不会被包含在索引中.<br/>
即使索引有多列这样的情况下, 只要这些列中有一列含有'null', <br/>
该列就会从索引中排除.也就是说如果某列存在空值, 即使对该列建索引也不会提高性能.<br/>
任何在'**WHERE**'子句中使用'is null'或'is not null'的语句优化器是不允许使用索引的.<br/>
例如:<br/>
``` select id from t where num is null ```<br/>
可以在num上设置默认值'**DEFAULT** 0', 确保表中num列没有null值, 然后这样查询:<br/>
``` select id from t where num = 0 ```<br/>

<hr>

### 9. 'SELECT'语句务必指明字段名称

'**SELECT** *'增加很多不必要的消耗(cpu、io、内存、网络带宽);<br/>
增加了使用覆盖索引的可能性;<br/>

<hr>

### 10. 适当使用'LIMIT 1'

当只需要一条数据的时候可以使用'**LIMIT** 1',<br/>
这可以使得'**EXPLAIN**'中'type'列达到const类型<br/>

<hr>

### 11. 不要在'where'子句中的'='左边进行计算

函数、算术运算或其他表达式运算在'='左边计算时, 系统将可能无法正确使用索引.<br/>
例如:<br/>
``` select * from t where sum*2=36; ```<br/>
对字段就行了算术运算, 这会造成引擎放弃使用索引, 建议改成<br/>
``` select * from t where sum=36/2; ```<br/>

<hr>

### 12. 避免隐式类型转换

'**WHERE**'子句中出现字段的类型和传入的参数类型不一致的时候发生的类型转换,<br/>
建议先确定'**WHERE**'中的参数类型, 例如:<br/>
``` select * from t where type=1; ```<br/>
``` select * from t where type='1'; ```<br/>
两者的'EXPLAIN'是不一致的.

<hr>

### 13. 对于联合索引来说, 要遵守最左前缀法则

在使用索引字段作为条件时, 如果该索引是复合索引,<br/>
那么必须使用到该索引中的第一个字段作为条件时才能保证系统使用该索引, <br/>
否则该索引将不会被使用, 并且应尽可能的让字段顺序与索引顺序相一致.<br/>

<hr>

### 14. 不建议使用'%'前缀模糊查询

例如'**LIKE** '%name''或者'**LIKE** '%name%'', 这种查询会导致索引失效而进行全表扫描.<br/>
可以使用'**LIKE** 'name%'';或者使用全文索引.<br/>
在我们查询中经常会用到``` select * from t where name like '%pp%'; ```<br/>
如果是必须加前缀模糊查询的话, 那么普通索引是无法满足查询需求的.<br/>
在MySQL中, 可以有**全文索引**来完成.<br/>

<hr>

### 15. 用'EXISTS'替换'DISTINCT'

当提交一个包含一对多表信息的查询时,避免在'**SELECT**'子句中使用'**DISTINCT**'. <br/>
一般可以考虑用'**EXISTS**'替换, 这可以使查询更为迅速,<br/>
例子:<br/>
``` 
SELECT DISTINCT * 
FROM DEPT D, EMP E 
WHERE D.DEPT_NO = E.DEPT_NO;
```
而以下这个sql更加高效<br/>
``` 
SELECT * 
FROM DEPT D  
WHERE EXISTS ( 
SELECT 1
FROM  EMP E  
WHERE E.DEPT_NO = D.DEPT_NO
); 
```

<hr>

### 16. 'DISTINCT'与'GROUP BY'的去重

'**DISTINCT**'更倾向于'去重'的概念, 将数据查询出来后再把重复的去掉;<br/>
'**GROUP BY**'更倾向于'按组查询'的概念, 其实很多时候都是统计时使用;<br/>
但也可以适当使用'**GROUP BY**'帮助去重, 一般情况下性能上要比'**DISTINCT**'好些.<br/>

<hr>

### 17. 关于'JOIN'优化

'**LEFT JOIN**' 左表为驱动表;<br/>
'**INNER JOIN**' MySQL会自动找出那个数据少的表作用驱动表;<br/>
'**RIGHT JOIN**' 右表为驱动表;<br/>
<br/>
建议尽量使用'**INNER JOIN**',<br/>
参与联合查询的表至少为2张表, 一般都存在大小之分.<br/>
如果连接方式是'**INNER JOIN**', 在没有其他过滤条件的情况下MySQL会自动选择小表作为驱动表, <br/>
但是'**LEFT JOIN**'在驱动表的选择上遵循的是左边驱动右边的原则, 即左表为驱动表.<br/>
记得合理利用索引.<br/>
且可将驱动表的索引字段作为on的限制字段,利用小表去驱动大表.<br/>

> ps.<br/>
>  这里多说一个'**STRAIGHT_JOIN**'<br/>
> '**STRAIGHT_JOIN**'只适用于'**INNER JOIN**', <br/>
> 并不适用于'**LEFT JOIN**'、'**RIGHT JOIN**'(均已经代表指定了表的执行顺序),<br/>
> '**STRAIGHT_JOIN**'强制决定了左表就是驱动表, 右表则是被驱动表.<br/>
> 
> 不过还是尽可能让优化器去判断, 因为大部分情况下mysql优化器是比人要聪明的,<br/>
> 所以使用'**STRAIGHT_JOIN**'一定要慎重, 因为大部分情况下认为指定的执行顺序并不一定会比优化引擎要靠谱.<br/>

<hr>

### 18. 关于'LIMIT'优化

大数据量分页时, 会不会出现下面这种查询语句 : <br/>
``` select * from t where a = 1 limit 5000000, 5 ```<br/>
这种语句的查询速度时很慢的, sql需要将5000005条数据查询出来后, 再取得最后5条返回.<br/>
且会加载很多大量的热点不是很高的'数据页'占用buffer pool,造成'buffer pool'污染.<br/>
可以参考下面的这种写法 : <br/>
``` select * from t a inner join (select id from t where a = 1 limit 5000000, 5) b on a.id = b.id ```<br/>

<hr>

### 19. 建立索引的小建议

1. 经常用来查询的字段可建索引.<br/>

2. 常用来分组和排序的字段可建立索引.<br/>
索引的作用是查询和排序,``` order by ```和``` group by ```可以使用索引.<br/>
若是排序条件有多字段,也是按照最左前缀原则使用索引.<br/>

3. 更新频繁的字段不要建立索引.<br/>
频繁更新的字段如果建立来索引, 更新时就不仅更新数据,索引的B+树也会发生变化,<br/>
得不偿失.<br/>

4. 选择性小的列不要建立索引.<br/>
类似性别字段, 几百万数据里只有几个值, 建立索引毫无意义.<br/>

5. 索引尽量使用等值匹配(在连接条件中使用'='运算符).<br/>

6. 尽量使用覆盖索引.<br/>
如果查找的列刚刚好是(联合)索引的字段, 那就没有必要去再去搜索主键索引,<br/>
直接取叶子节点值即可,这就是覆盖索引.<br/>
例如少用'select *'不仅能减少读取的开销, 还能够使用覆盖索引.<br/>

<hr>

<br/>
<br/>
<br/>
<br/>
<br/>

感谢以下文章<br/>
https://mp.weixin.qq.com/s/nJHxIQYuABFWLSJoBIPHEA <br/>
https://juejin.im/post/5e575cb56fb9a07c951cdb39?utm_source=gold_browser_extension <br/>
