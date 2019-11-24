<h2> 常见MySQL语句优化笔记 </h2>

- [1. 合理创建索引](#1-%E5%90%88%E7%90%86%E5%88%9B%E5%BB%BA%E7%B4%A2%E5%BC%95)
- [2. SQL语句中IN包含的值不应过多](#2-sql%E8%AF%AD%E5%8F%A5%E4%B8%ADin%E5%8C%85%E5%90%AB%E7%9A%84%E5%80%BC%E4%B8%8D%E5%BA%94%E8%BF%87%E5%A4%9A)
- [3. 区分'IN'和'EXISTS', 'NOT IN'和'NOT EXISTS'](#3-%E5%8C%BA%E5%88%86in%E5%92%8Cexists-not-in%E5%92%8Cnot-exists)
- [4. 注意范围查询语句](#4-%E6%B3%A8%E6%84%8F%E8%8C%83%E5%9B%B4%E6%9F%A5%E8%AF%A2%E8%AF%AD%E5%8F%A5)
- [5. 避免使用'!='或'<>'操作符](#5-%E9%81%BF%E5%85%8D%E4%BD%BF%E7%94%A8%E6%88%96%E6%93%8D%E4%BD%9C%E7%AC%A6)
- [6. 避免使用'OR'连接条件](#6-%E9%81%BF%E5%85%8D%E4%BD%BF%E7%94%A8or%E8%BF%9E%E6%8E%A5%E6%9D%A1%E4%BB%B6)
- [7. 尽量用'UNION ALL'代替'UNION'](#7-%E5%B0%BD%E9%87%8F%E7%94%A8union-all%E4%BB%A3%E6%9B%BFunion)
- [8. 进行'is null'值和'is not null'判断都不可取](#8-%E8%BF%9B%E8%A1%8Cis-null%E5%80%BC%E5%92%8Cis-not-null%E5%88%A4%E6%96%AD%E9%83%BD%E4%B8%8D%E5%8F%AF%E5%8F%96)
- [9. 'SELECT'语句务必指明字段名称](#9-select%E8%AF%AD%E5%8F%A5%E5%8A%A1%E5%BF%85%E6%8C%87%E6%98%8E%E5%AD%97%E6%AE%B5%E5%90%8D%E7%A7%B0)
- [10. 适当使用'LIMIT 1'](#10-%E9%80%82%E5%BD%93%E4%BD%BF%E7%94%A8limit-1)
- [11. 不要在'where'子句中的'='左边进行计算](#11-%E4%B8%8D%E8%A6%81%E5%9C%A8where%E5%AD%90%E5%8F%A5%E4%B8%AD%E7%9A%84%E5%B7%A6%E8%BE%B9%E8%BF%9B%E8%A1%8C%E8%AE%A1%E7%AE%97)
- [12. 避免隐式类型转换](#12-%E9%81%BF%E5%85%8D%E9%9A%90%E5%BC%8F%E7%B1%BB%E5%9E%8B%E8%BD%AC%E6%8D%A2)
- [13. 对于联合索引来说, 要遵守最左前缀法则](#13-%E5%AF%B9%E4%BA%8E%E8%81%94%E5%90%88%E7%B4%A2%E5%BC%95%E6%9D%A5%E8%AF%B4-%E8%A6%81%E9%81%B5%E5%AE%88%E6%9C%80%E5%B7%A6%E5%89%8D%E7%BC%80%E6%B3%95%E5%88%99)
- [14. 不建议使用'%'前缀模糊查询](#14-%E4%B8%8D%E5%BB%BA%E8%AE%AE%E4%BD%BF%E7%94%A8%E5%89%8D%E7%BC%80%E6%A8%A1%E7%B3%8A%E6%9F%A5%E8%AF%A2)
- [15. 用'EXISTS'替换'DISTINCT'](#15-%E7%94%A8exists%E6%9B%BF%E6%8D%A2distinct)
- [16. 关于'JOIN'优化](#16-%E5%85%B3%E4%BA%8Ejoin%E4%BC%98%E5%8C%96)

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

### 16. 关于'JOIN'优化

'**LEFT JOIN**' 左表为驱动表;<br/>
'**INNER JOIN**' MySQL会自动找出那个数据少的表作用驱动表;<br/>
'**RIGHT JOIN**' 右表为驱动表;<br/>
<br/>
建议尽量使用'**INNER JOIN**',<br/>
参与联合查询的表至少为2张表, 一般都存在大小之分.<br/>
如果连接方式是'**INNER JOIN**', 在没有其他过滤条件的情况下MySQL会自动选择小表作为驱动表, <br/>
但是'**LEFT JOIN**'在驱动表的选择上遵循的是左边驱动右边的原则, 即左表为驱动表.<br/>
且合理利用索引<br/>
被驱动表的索引字段作为on的限制字段,利用小表去驱动大表<br/>

> ps.<br/>
>  这里多说一个'**STRAIGHT_JOIN**'<br/>
> '**STRAIGHT_JOIN**'只适用于'**INNER JOIN**', <br/>
> 并不适用于'**LEFT JOIN**'、'**RIGHT JOIN**'(均已经代表指定了表的执行顺序),<br/>
> '**STRAIGHT_JOIN**'强制决定了左表就是驱动表, 右表则是被驱动表.<br/>
> 
> 不过还是尽可能让优化器去判断, 因为大部分情况下mysql优化器是比人要聪明的,<br/>
> 所以使用'**STRAIGHT_JOIN**'一定要慎重, 因为大部分情况下认为指定的执行顺序并不一定会比优化引擎要靠谱.<br/>

<hr>

<br/>
<br/>
<br/>
<br/>
<br/>

感谢以下文章给了我写作思路:<br/>
https://mp.weixin.qq.com/s/nJHxIQYuABFWLSJoBIPHEA<br/>

