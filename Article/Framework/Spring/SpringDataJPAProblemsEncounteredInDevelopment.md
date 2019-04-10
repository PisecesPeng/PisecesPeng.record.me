<h2> Spring Data JPA, 开发中遇到的问题 </h2>

- [1. 在原生SQL语句中引用Class对应的表名](#1-%E5%9C%A8%E5%8E%9F%E7%94%9Fsql%E8%AF%AD%E5%8F%A5%E4%B8%AD%E5%BC%95%E7%94%A8class%E5%AF%B9%E5%BA%94%E7%9A%84%E8%A1%A8%E5%90%8D)
- [2. 在JPA自动建表后,插入中文报错](#2-%E5%9C%A8jpa%E8%87%AA%E5%8A%A8%E5%BB%BA%E8%A1%A8%E5%90%8E%E6%8F%92%E5%85%A5%E4%B8%AD%E6%96%87%E6%8A%A5%E9%94%99)
- [3. 字段需要设置一个默认值(主键自增)](#3-%E5%AD%97%E6%AE%B5%E9%9C%80%E8%A6%81%E8%AE%BE%E7%BD%AE%E4%B8%80%E4%B8%AA%E9%BB%98%E8%AE%A4%E5%80%BC%E4%B8%BB%E9%94%AE%E8%87%AA%E5%A2%9E)
- [4. 无效的数据访问API,执行update/delete错误](#4-%E6%97%A0%E6%95%88%E7%9A%84%E6%95%B0%E6%8D%AE%E8%AE%BF%E9%97%AEapi%E6%89%A7%E8%A1%8Cupdatedelete%E9%94%99%E8%AF%AF)
- [5. 避免数据截断的问题(Data truncation)](#5-%E9%81%BF%E5%85%8D%E6%95%B0%E6%8D%AE%E6%88%AA%E6%96%AD%E7%9A%84%E9%97%AE%E9%A2%98data-truncation)

### 1. 在原生SQL语句中引用Class对应的表名

> 1. 需要指定'**nativeQuery = true**'来使用原生SQL语句.
> 2. 在Class类的@**Entity**注解中加上'name = "###"'的属性("###"为数据库表名). 

在SQL语句引用表名时加入'#{#entityName}'即可.
如: ``` SELECT * FROM #{#entityName} WHERE pk_id=:id ```

### 2. 在JPA自动建表后,插入中文报错

> 1. 先查看表的字段编码类型``` SHOW FULL COLUMNS FROM '表名' ```
> 2. 修改字段的Collation``` ALTER TABLE '表名' CONVERT TO CHARACTER SET UTF8 ```

### 3. 字段需要设置一个默认值(主键自增)

``` ps. GeneratedValue设置属性似乎..没作用... ```

> 若非主键,设置默认值即可.
> 主键设置自增``` ALTER TABLE '表名' MODIFY '字段' '字段类型(int、bigint等类型)' AUTO_INCREMENT; ```

### 4. 无效的数据访问API,执行update/delete错误

> 可在@**Modifying**后面加@**Transactional**注解(添加事务)

### 5. 避免数据截断的问题(Data truncation)

> 在自定义SQL语句时,尽量避免使用'**AND**'而用'**,**'替代.
