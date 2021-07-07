## 常见MySQL语句优化笔记

- [常见MySQL语句优化笔记](#常见mysql语句优化笔记)
  - [1. 合理创建索引](#1-合理创建索引)
  - [2. SQL语句中IN包含的值不应过多](#2-sql语句中in包含的值不应过多)
  - [3. 区分'IN'和'EXISTS', 'NOT IN'和'NOT EXISTS'](#3-区分in和exists-not-in和not-exists)
  - [4. 注意范围查询语句](#4-注意范围查询语句)
  - [5. 避免使用'!='或'<>'操作符](#5-避免使用或操作符)
  - [6. 避免使用'OR'连接条件](#6-避免使用or连接条件)
  - [7. 尽量用'UNION ALL'代替'UNION'](#7-尽量用union-all代替union)
  - [8. 'is null'和'is not null'不一定不走索引](#8-is-null和is-not-null不一定不走索引)
  - [9. 'SELECT'语句务必指明字段名称](#9-select语句务必指明字段名称)
  - [10. 适当使用'LIMIT'](#10-适当使用limit)
  - [11. 不要在'where'从句中的'='左边进行计算](#11-不要在where从句中的左边进行计算)
  - [12. 避免隐式类型转换](#12-避免隐式类型转换)
  - [13. 对于联合索引来说, 要遵守最左前缀法则](#13-对于联合索引来说-要遵守最左前缀法则)
  - [14. 不建议使用'%'前缀模糊查询](#14-不建议使用前缀模糊查询)
  - [15. 用'EXISTS'替换'DISTINCT'](#15-用exists替换distinct)
  - [16. 'DISTINCT'与'GROUP BY'的去重](#16-distinct与group-by的去重)
  - [17. 关于'JOIN'优化](#17-关于join优化)
  - [18. 关于'LIMIT'优化](#18-关于limit优化)
  - [19. 建立索引的小建议](#19-建立索引的小建议)
  - [20. 尽量把所有列定义为'NOT NULL'](#20-尽量把所有列定义为not-null)

MySQL语句的优化, 善用'**EXPLAIN**'帮助我们查看SQL执行计划, 善用'**profile**'分析并调优SQL.

这里不详细说明, **EXPLAIN**中主要先关心以下字段:   
**type**: 连接类型(all、index、range、ref、eq_ref、const、system、null(从左到右, 性能从差到好)).  
**key**: 使用到的索引名, 如果没有选择索引, 值是NULL.可以采取强制索引方式(FORCE INDEX、USE INDEX或IGNORE INDEX).    
**key_len**: 索引字段的最大可能长度, 并非实际使用长度(不损失精确性的情况下, 长度越短越好).  
**extra**: 详细说明(详请自行搜索).  

---

### 1. 合理创建索引

对查询进行优化, 应尽量避免全表扫描,  
可以在'**WHERE**'、'**ORDER BY**'和'**GROUP BY**'涉及的列上先建立索引(记得考虑组合索引等..).  

---

### 2. SQL语句中IN包含的值不应过多

MySQL对于'**IN**'做了相应的优化,   
即, 将'**IN**'中的常量全部存储在一个数组里面, 且这个数组自动排序.  
但是如果数值较多, 这样产生的消耗是比较大的.  

还要注意'**IN**'和'**NOT IN**'都要慎用, 否则会导致全表扫描.  
例如: `select id from t where num in(1,2,3)`  
对于连续的数值, 能用'**BETWEEN**'就不要用'**IN**'了, 再或者使用连接来替换.  

---

### 3. 区分'IN'和'EXISTS', 'NOT IN'和'NOT EXISTS'

首先先看两条sql  
`select * from ta where id in (select id from tb)`  
等同于  
`select * from ta where exists (select * from tb where tb.id = ta.id)`  
区分'**IN**'和'**EXISTS**'性能变化, 主要是造成了驱动顺序的改变,  
如果是'**EXISTS**', 那么以外层表为驱动表, 先被访问,  
如果是'**IN**', 那么先执行子查询.  
所以'**IN**'适合于外表大而内表小的情况;'**EXISTS**'适合于外表小而内表大的情况.  

关于'**NOT IN**'和'**NOT EXISTS**', 推荐使用'**NOT EXISTS**'.
'**NOT IN**'如果查询语句使用了'**NOT IN**'那么内外表都进行全表扫描, 并均未用到索引;  
而'**NOT EXISTS**'的子查询依然能用到表上的索引.  
无论哪个表大, 用'**NOT EXISTS**'都比'**NOT IN**'要快.  

---

### 4. 注意范围查询语句

对于联合索引来说, 如果存在范围查询,  
比如'**BETWEEN**','**>**','**<**'等条件时, 会造成后面的索引字段失效.  

```sql
...略
WHERE date >= TO_DATE(?, 'YYYY-MM-DD')
    AND date <= TO_DATE(?, 'YYYY-MM-DD')
    AND id  = ?;
```

以上sql, id的索引会失效.  
简单来说, 存在多列索引, 建议将需要用'**=**'查询的列的索引放在最开始, '**RANGE**'搜索的列往后放  

---

### 5. 避免使用'!='或'<>'操作符

应尽量避免在'**WHERE**'子句中使用'**!=**'或'**<>**'操作符, 否则将引擎放弃使用索引而进行全表扫描.  
例如:  
`select * from t where id != 3000;`  
可以修改为  
`select * from t where id < 3000 or id > 3000;`  
虽然这两种查询的结果一样, 但是第二种查询方案会比第一种查询方案更快些.  

---

### 6. 避免使用'OR'连接条件

应尽量避免在'**WHERE**'子句中使用'**OR**'来连接条件, 否则将导致引擎放弃使用索引而进行全表扫描,  
且'**OR**'两边的字段中, 如果有一个不是索引字段, 而其他条件也不是索引字段, 会造成该查询不走索引的情况.  

例如:
```sql
select id from t where num=10 or num=20
```
可以这样查询:
```sql
select id from t where num=10
union all
select id from t where num=20
```

---

### 7. 尽量用'UNION ALL'代替'UNION'


'**UNION**'和'**UNION ALL**'的差异主要是:  
前者需要将结果集合并后再进行唯一性过滤操作,   
这就会涉及到排序, 增加大量的CPU运算, 加大资源消耗及延迟.  
当然, '**UNION ALL**'的前提条件是两个结果集没有重复数据.  

---

### 8. 'is null'和'is not null'不一定不走索引

'is null'和'is not null'不一定走索引,  
但是也不是说一定不会走索引,  
所以不需要过度的排斥这种写法,  
这和数据量有一定的关系, 建议用explain具体分析

---

### 9. 'SELECT'语句务必指明字段名称  

'**SELECT** *'增加很多不必要的消耗(cpu、io、内存、网络带宽);
增加了使用覆盖索引的可能性;  

---

### 10. 适当使用'LIMIT'  

比如:  
查询当只需要一条数据的时候可以使用'**LIMIT** 1', 这可使得'**EXPLAIN**'中'type'列达到const类型.  
删除的时候, 可以使用'**LIMIT**'限制数量, 可避免'长事务'、提高效率.  

---

### 11. 不要在'where'从句中的'='左边进行计算  

函数、算术运算或其他表达式运算在'='左边计算时, 系统将可能无法正确使用索引.  
例如:  
`select * from t where sum*2=36;`  
对字段就行了算术运算, 这会造成引擎放弃使用索引, 建议改成  
`select * from t where sum=36/2;`  
或者,  
```sql
select id,time
from tb
where Date_ADD(time,Interval 7 DAY) >= now();
```
假设time加了索引的话，建议改成: 
```sql 
select id,time
from tb
where time >= Date_ADD(NOW(),INTERVAL -7 DAY);
```

---

### 12. 避免隐式类型转换

'**WHERE**'子句中出现字段的类型和传入的参数类型不一致的时候发生的类型转换,  
建议先确定'**WHERE**'中的参数类型, 例如:  
`select * from t where type=1;`  
`select * from t where type='1';`  
若`type`类型是`number`, 那么使用`'1'`会有隐式类型转换的影响.  

---

### 13. 对于联合索引来说, 要遵守最左前缀法则

在使用索引字段作为条件时, 如果该索引是组合索引,  
那么必须使用到该索引`之前`的字段作为条件,  
才能保证系统使用该索引, 否则该索引将不会被使用,  
比如(a, b, c)为一个组合索引时候,  
必须有`a`的条件时才会走`b`索引, 必须有`a`、`b`的条件才会走`c`索引.  
ps. 在`order by {组合索引字段}`等语句中, 并不能作为触发组合索引的条件.  

---

### 14. 不建议使用'%'前缀模糊查询

例如'**LIKE** '%name''或者'**LIKE** '%name%'', 这种查询可能会导致索引失效而进行全表扫描.  
可以使用'**LIKE** 'name%'';或者使用全文索引;  
但是, 不是说有了前缀模糊查询就一定不走索引(例如, 复合查询..),
还是建议使用explain具体分析.

---

### 15. 用'EXISTS'替换'DISTINCT'

当提交一个包含一对多表信息的查询时,避免在'**SELECT**'子句中使用'**DISTINCT**'.  
一般可以考虑用'**EXISTS**'替换, 这可以使查询更为迅速,  
例子:
```sql
SELECT DISTINCT * 
FROM DEPT D, EMP E 
WHERE D.DEPT_NO = E.DEPT_NO;
```
而以下这个sql更加高效
```sql
SELECT * 
FROM DEPT D  
WHERE EXISTS ( 
SELECT 1
FROM  EMP E  
WHERE E.DEPT_NO = D.DEPT_NO
);
```

---

### 16. 'DISTINCT'与'GROUP BY'的去重

'**DISTINCT**'更倾向于'去重'的概念, 将数据查询出来后再把重复的去掉;  
'**GROUP BY**'更倾向于'按组查询'的概念, 其实很多时候都是统计时使用;  
可以适当使用'**GROUP BY**'帮助去重, 
不过性能的好坏各有说法, 可以去网上找文章深入了解.  

---

### 17. 关于'JOIN'优化

'**LEFT JOIN**' 左表为驱动表;  
'**INNER JOIN**' MySQL会自动找出那个数据少的表作用驱动表;  
'**RIGHT JOIN**' 右表为驱动表;  

建议尽量使用'**INNER JOIN**',  
参与联合查询的表至少为2张表, 一般都存在大小之分.  
如果连接方式是'**INNER JOIN**', 在没有其他过滤条件的情况下MySQL会自动选择小表作为驱动表,  
但是'**LEFT JOIN**'在驱动表的选择上遵循的是左边驱动右边的原则, 即左表为驱动表.  
记得合理利用索引.  
且可将驱动表的索引字段作为on的限制字段,利用小表去驱动大表.  

> ps.  
> 这里多说一个'**STRAIGHT_JOIN**'  
> '**STRAIGHT_JOIN**'只适用于'**INNER JOIN**',  
> 并不适用于'**LEFT JOIN**'、'**RIGHT JOIN**'(均已经代表指定了表的执行顺序),  
> '**STRAIGHT_JOIN**'强制决定了左表就是驱动表, 右表则是被驱动表.  
> 不过还是尽可能让优化器去判断, 因为大部分情况下mysql优化器是比人要聪明的,  
> 所以使用'**STRAIGHT_JOIN**'一定要慎重, 因为大部分情况下认为指定的执行顺序并不一定会比优化引擎要靠谱.  

---

### 18. 关于'LIMIT'优化

大数据量分页时, 会不会出现下面这种查询语句 :  
`select * from t where a = 1 limit 5000000, 5`  
这种语句的查询速度时很慢的, sql需要将5000005条数据查询出来后, 再取得最后5条返回.  
且会加载很多大量的热点不是很高的'数据页'占用buffer pool,造成'buffer pool'污染.  
可以参考下面的这种写法 :  
`select * from t a inner join (select id from t where a = 1 limit 5000000, 5) b on a.id = b.id`  
`select * from t a, (select id from t where a = 1 limit 5000000, 5) b where a.id = b.id`  

---

### 19. 建立索引的小建议

1. 经常用来查询的字段可建索引.  
2. 常用来分组和排序的字段可建立索引.  
索引的作用是查询和排序,`order by`和`group by`可以使用索引.  
若是排序条件有多字段,也是按照最左前缀原则使用索引.  
3. 更新频繁的字段不要建立索引.频繁更新的字段如果建立来索引, 更新时就不仅更新数据,索引的B+树也会发生变化,得不偿失.
4. 选择性小的列不要建立索引.类似性别字段, 几百万数据里只有几个值, 建立索引毫无意义.
5. 索引尽量使用等值匹配(在连接条件中使用'='运算符).  
6. 尽量使用覆盖索引.如果查找的列刚刚好是(联合)索引的字段, 那就没有必要去再去搜索主键索引,直接取叶子节点值即可,这就是覆盖索引.例如少用'select *'不仅能减少读取的开销, 还能够使用覆盖索引.

---

### 20. 尽量把所有列定义为'NOT NULL'

1. '**NOT NULL列**'更节省空间, 'NULL列'需要一个额外字节作为判断是否为NULL的标志位.  
2. 当count()具体字段时, 聚合函数仅对表中非NULL的列进行统计.
3. '**NULL**'与其他值进行运算时, 结果都是返回**NULL**.
4. '**GROUP BY**'与'**ORDER BY**', 默认都会将**NULL**排在返回最前.
5. 当使用'where :col != :value', 会查询不出该列为'**NULL**'的数据.
6. '**NULL**'列值是可以使用索引的, 但是当'**NULL**'列值变多会导致索引失效(简单来说, 就是NULL值导致优化器在做索引选择的时候更复杂、更加难以优化).
7. (代码角度)'**NULL列**'需要注意空指针问题, '**NULL列**'在计算和比较的时候, 需要注意空指针问题.  

---

<br><br><br><br><br>

感谢以下文章  
[https://mp.weixin.qq.com/s/nJHxIQYuABFWLSJoBIPHEA](https://mp.weixin.qq.com/s/nJHxIQYuABFWLSJoBIPHEA)  
[https://juejin.im/post/5e575cb56fb9a07c951cdb39?utm_source=gold_browser_extension](https://juejin.im/post/5e575cb56fb9a07c951cdb39?utm_source=gold_browser_extension)  
[https://www.kawabangga.com/posts/3893](https://www.kawabangga.com/posts/3893)  
[https://juejin.im/post/5ea16dede51d45470b4ffc5b?utm_source=gold_browser_extension](https://juejin.im/post/5ea16dede51d45470b4ffc5b?utm_source=gold_browser_extension)  
[https://mp.weixin.qq.com/s/85C2_McS-XykTmFJWN7kTQ](https://mp.weixin.qq.com/s/85C2_McS-XykTmFJWN7kTQ)  
