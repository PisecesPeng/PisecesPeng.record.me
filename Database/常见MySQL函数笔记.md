## 常见MySQL函数笔记

- [常见MySQL函数笔记](#常见mysql函数笔记)
  - [1. 流程函数](#1-流程函数)
    - [1) if 函数, 判断](#1-if-函数-判断)
    - [2) ifnull 函数, 判断是否为null](#2-ifnull-函数-判断是否为null)
    - [3) case 函数, 搜索](#3-case-函数-搜索)
  - [2. 日期函数](#2-日期函数)
    - [1) now & sysdate 函数, 当前日期时间](#1-now--sysdate-函数-当前日期时间)
    - [2) curdate & current_date 函数, 当前日期](#2-curdate--current_date-函数-当前日期)
    - [3) curtime & current_time 函数, 当前时间](#3-curtime--current_time-函数-当前时间)
    - [4) year & month & day 函数, 年、月、日](#4-year--month--day-函数-年月日)
    - [5) dayofweek & dayofmonth & dayofyear 函数, 日在周索引、日在月索引、日在年索引](#5-dayofweek--dayofmonth--dayofyear-函数-日在周索引日在月索引日在年索引)
    - [6) weekday & week 函数, 日在周索引、周在年索引](#6-weekday--week-函数-日在周索引周在年索引)
    - [7) unix_timestamp & from_unixtime 函数, 当前时间戳、解析时间戳](#7-unix_timestamp--from_unixtime-函数-当前时间戳解析时间戳)
    - [8) date_add & adddate 函数, 日期增加时间](#8-date_add--adddate-函数-日期增加时间)
    - [9) date_sub & subdate 函数, 日期减少时间](#9-date_sub--subdate-函数-日期减少时间)
    - [10) addtime & subtime 函数, 日期增加时间、日期减少时间](#10-addtime--subtime-函数-日期增加时间日期减少时间)
    - [11) datediff 函数, 日期间隔](#11-datediff-函数-日期间隔)
    - [12) date_format 函数, 日期格式化](#12-date_format-函数-日期格式化)
    - [13) str_to_date 函数, 字符转日期](#13-str_to_date-函数-字符转日期)
  - [3. 字符函数](#3-字符函数)
    - [1) length 函数, 字符长度](#1-length-函数-字符长度)
    - [2) concat 函数, 字符拼接](#2-concat-函数-字符拼接)
    - [3) lower & upper 函数, 小写转换、大写转换](#3-lower--upper-函数-小写转换大写转换)
    - [4) insert & replace 函数, 插入字符、替换字符](#4-insert--replace-函数-插入字符替换字符)
    - [5) right & left 函数, 右截取字符、左截取字符](#5-right--left-函数-右截取字符左截取字符)
    - [6) rpad & lpad 函数, 右填充指定字符、左填充指定字符](#6-rpad--lpad-函数-右填充指定字符左填充指定字符)
    - [7) reverse 函数, 反转字符](#7-reverse-函数-反转字符)
    - [8) substr 函数, 截取字符](#8-substr-函数-截取字符)
    - [9) trim 函数, 删除两侧指定字符(默认空格)](#9-trim-函数-删除两侧指定字符默认空格)
    - [10)instr 函数, 返回指定子串第一次出现索引(默认为0)](#10instr-函数-返回指定子串第一次出现索引默认为0)
  - [4. 数值函数](#4-数值函数)
    - [1) round 函数, 四舍五入](#1-round-函数-四舍五入)
    - [2) ceil & floor 函数, 向上取整、向下取整](#2-ceil--floor-函数-向上取整向下取整)
    - [3) mod 函数, 去余](#3-mod-函数-去余)
    - [4) truncate 函数, 小数位截断](#4-truncate-函数-小数位截断)
    - [5) abs 函数, 绝对值](#5-abs-函数-绝对值)
    - [6) pow 函数, 求次方](#6-pow-函数-求次方)
    - [7) sqrt 函数, 求二次方根](#7-sqrt-函数-求二次方根)
    - [8) sign 函数, 参数符号(正数:1、零:0、负数:-1)](#8-sign-函数-参数符号正数1零0负数-1)
    - [9) rand 函数, 随机数(给定参数则返回随机数不变)](#9-rand-函数-随机数给定参数则返回随机数不变)
  - [5. 聚合函数](#5-聚合函数)
    - [1) max 函数, 求最大值](#1-max-函数-求最大值)
    - [2) min 函数, 求最小值](#2-min-函数-求最小值)
    - [3) count 函数, 统计结果行数](#3-count-函数-统计结果行数)
    - [4) sum 函数, 求和](#4-sum-函数-求和)
    - [5) avg 函数, 求平均值](#5-avg-函数-求平均值)

### 1. 流程函数

#### 1) if 函数, 判断
```sql
select if(5 < 10, 'true', 'false') as '是否大于';  -- 输出 true
select if(5 > 10, 'true', 'false') as '是否大于';  -- 输出 false
```

#### 2) ifnull 函数, 判断是否为null
```sql
select ifnull('false', 'true') as '为空?';  -- 输出 false
select ifnull(null, 'true') as '为空?';  -- 输出 true
```

#### 3) case 函数, 搜索
```sql
select 
case weekday(now()) 
  when 0 then '星期一' 
  when 1 then '星期二'
  when 2 then '星期三' 
  when 3 then '星期四' 
  when 4 then '星期五' 
  when 5 then '星期六'
else '星期天'
end as '今天礼拜几?';
-- 输出 星期天
```

### 2. 日期函数

#### 1) now & sysdate 函数, 当前日期时间
```sql
select now(), sysdate();
-- 输出 2020-08-06 19:31:11、 2020-08-06 19:31:11
```

#### 2) curdate & current_date 函数, 当前日期
```sql
select curdate(), current_date();
-- 输出 2020-08-06、 2020-08-06
```

#### 3) curtime & current_time 函数, 当前时间
```sql
select curtime(), current_time();
-- 输出 19:31:11、 19:31:11
```

#### 4) year & month & day 函数, 年、月、日
```sql
select year(now()),month(now()),day(now());
-- 输出 2020、 8、 6
```

#### 5) dayofweek & dayofmonth & dayofyear 函数, 日在周索引、日在月索引、日在年索引
```sql
select dayofweek(now()), dayofmonth(now()), dayofyear(now());
-- 输出 5、 6、 219
```

#### 6) weekday & week 函数, 日在周索引、周在年索引
```sql
select weekday(now()), week(now());
-- 输出 3、 31
```

#### 7) unix_timestamp & from_unixtime 函数, 当前时间戳、解析时间戳
```sql
select unix_timestamp(), from_unixtime(1596720656);
-- 输出 1596720656、 2020-08-06 21:30:56
```

#### 8) date_add & adddate 函数, 日期增加时间
```sql
select date_add(now(), INTERVAL 1 DAY), adddate(now(), INTERVAL'1:1'MINUTE_SECOND);
-- 输出 2020-08-07 20:22:16、2020-08-06 20:23:17
```

#### 9) date_sub & subdate 函数, 日期减少时间
```sql
select date_sub(now(), INTERVAL 1 DAY), subdate(now(), INTERVAL'1:1'MINUTE_SECOND);
-- 输出 2020-08-05 20:24:43、2020-08-06 20:23:42
```

#### 10) addtime & subtime 函数, 日期增加时间、日期减少时间
```sql
select addtime(now(), '1:1:1'), subtime(now(),'1:1');
-- 输出 2020-08-06 21:29:17、 2020-08-06 19:27:16
```

#### 11) datediff 函数, 日期间隔
```sql
select datediff(now(), '2020-08-05'), datediff(now(), '2020-08-07');
-- 输出 1、 -1
```

#### 12) date_format 函数, 日期格式化
```sql
SELECT date_format(now(),'%Y年%m月%d日');
-- 输出 2020年08月06日
SELECT date_format(now(),'%Y-%m-%d %H:%i:%s');
-- 输出 2020-08-06 13:51:48
```

#### 13) str_to_date 函数, 字符转日期

```sql
SELECT STR_TO_DATE('08-06 2020','%m-%d %Y');
-- 输出 2020-08-06
```

### 3. 字符函数

#### 1) length 函数, 字符长度
```sql
select length('PisecesPeng');
-- 输出 11
```

#### 2) concat 函数, 字符拼接
```sql
select concat('Piseces','Pen', 'g');
-- 输出 PisecesPeng
```

#### 3) lower & upper 函数, 小写转换、大写转换
```sql
select lower('PisecesPeng'), upper('PisecesPeng');
-- 输出 pisecespeng、 PISECESPENG
```

#### 4) insert & replace 函数, 插入字符、替换字符
```sql
select insert('PisssPeng', 3, 0,'seces'), replace('PisPeng', 's', 'seces');
-- 输出 PisecessssPeng、 PisecesPeng
```

#### 5) right & left 函数, 右截取字符、左截取字符
```sql
select right('PisecesPeng', 4), left('PisecesPeng', 7);
-- 输出 Peng、 Piseces
```

#### 6) rpad & lpad 函数, 右填充指定字符、左填充指定字符
```sql
select rpad('rpad', 6, '#'), lpad('lpad', 6, '#');
-- 输出 rpad##、 ##lpad
```

#### 7) reverse 函数, 反转字符
```sql
select reverse('abcdefg');
-- 输出 gnePsecesiP
```

#### 8) substr 函数, 截取字符
```sql
select substr('PisecesPeng', 3);  -- 输出 secesPeng
select substr('PisecesPeng', -4, 4);  -- 输出 Peng
```

#### 9) trim 函数, 删除两侧指定字符(默认空格)
```sql
select trim('  trim  ');  -- 输出 trim
select trim('t' from 'trim');  -- 输出 rim
```

#### 10)instr 函数, 返回指定子串第一次出现索引(默认为0)
```sql
select instr('PisecesPeng','Peng');  -- 输出 8
```

### 4. 数值函数

#### 1) round 函数, 四舍五入
```sql
select round(1.3), round(-1.5);
-- 输出 1、-2
```

#### 2) ceil & floor 函数, 向上取整、向下取整
```sql
select ceil(1.2), floor(-1.3);
-- 输出 2、-2
```

#### 3) mod 函数, 去余
```sql
select mod(120, 10), mod(10, -3);
-- 输出 0、-1
```

#### 4) truncate 函数, 小数位截断
```sql
select truncate(3.149, 2);  -- 输出 3.14
```

#### 5) abs 函数, 绝对值
```sql
select abs(-2.4);  -- 输出 2.4
```

#### 6) pow 函数, 求次方
```sql
select pow(9, -3), pow(10, 3);
-- 输出 0.0013717421124828531、 1000
```

#### 7) sqrt 函数, 求二次方根
```sql
select sqrt(25), sqrt(-9);
-- 输出 5、 null
```

#### 8) sign 函数, 参数符号(正数:1、零:0、负数:-1)
```sql
select sign(7),sign(0),sign(-7);
-- 输出 1、 0、 -1
```

#### 9) rand 函数, 随机数(给定参数则返回随机数不变)
```sql
select rand(),rand(10);
-- 输出 0.8987601342599467、 0.6570515219653505
```

### 5. 聚合函数

#### 1) max 函数, 求最大值

#### 2) min 函数, 求最小值

#### 3) count 函数, 统计结果行数

#### 4) sum 函数, 求和

#### 5) avg 函数, 求平均值


<br><br><br><br><br>

感谢以下文章
[http://c.biancheng.net/mysql/function/](http://c.biancheng.net/mysql/function/)
[https://juejin.im/post/6844904176057581576](https://juejin.im/post/6844904176057581576)

