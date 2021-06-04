## Spring Data JPA, 开发中遇到的问题

- [Spring Data JPA, 开发中遇到的问题](#spring-data-jpa-开发中遇到的问题)
  - [1. 在原生SQL语句中引用Class对应的表名](#1-在原生sql语句中引用class对应的表名)
  - [2. 在JPA自动建表后,插入中文报错](#2-在jpa自动建表后插入中文报错)
  - [3. 字段需要设置一个默认值(主键自增)](#3-字段需要设置一个默认值主键自增)
  - [4. 无效的数据访问API,执行update/delete错误](#4-无效的数据访问api执行updatedelete错误)
  - [5. 避免数据截断的问题(Data truncation)](#5-避免数据截断的问题data-truncation)

### 1. 在原生SQL语句中引用Class对应的表名

> 1. 需要指定'**nativeQuery = true**'来使用原生SQL语句.
> 2. 在Class类的@**Entity**注解中加上'name = "###"'的属性("###"为数据库表名).


在SQL语句引用表名时加入'#{#entityName}'即可.
如: `SELECT * FROM #{#entityName} WHERE pk_id=:id`

---

### 2. 在JPA自动建表后,插入中文报错

> 1. 先查看表的字段编码类型`SHOW FULL COLUMNS FROM '表名'`
> 2. 修改字段的Collation`ALTER TABLE '表名' CONVERT TO CHARACTER SET UTF8`

---

### 3. 字段需要设置一个默认值(主键自增)

`ps. GeneratedValue设置属性似乎..没作用...`

> 1. 若非主键,设置默认值即可.
> 2. 主键设置自增`ALTER TABLE '表名' MODIFY '字段' '字段类型(int、bigint等类型)' AUTO_INCREMENT;`

---

### 4. 无效的数据访问API,执行update/delete错误

> 可在@**Modifying**后面加@**Transactional**注解(添加事务)

---

### 5. 避免数据截断的问题(Data truncation)

> 在自定义SQL语句时,尽量避免使用'**AND**'而用'**,**'替代.

