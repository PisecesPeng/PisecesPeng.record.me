<h2> 简要了解Spring @Transactional </h2>

- [1. 先说说'脏读、不可重复读、幻读'](#1-%E5%85%88%E8%AF%B4%E8%AF%B4%E8%84%8F%E8%AF%BB%E4%B8%8D%E5%8F%AF%E9%87%8D%E5%A4%8D%E8%AF%BB%E5%B9%BB%E8%AF%BB)
- [2. @**Transactional**注解的属性一览](#2-transactional%E6%B3%A8%E8%A7%A3%E7%9A%84%E5%B1%9E%E6%80%A7%E4%B8%80%E8%A7%88)
  - [value](#value)
  - [rollbackFor](#rollbackfor)
  - [readOnly](#readonly)
  - [timeout](#timeout)
  - [propagation](#propagation)
  - [isolation](#isolation)
<hr>

### 1. 先说说'脏读、不可重复读、幻读'

> 1. **脏读** : 脏读就是指当一个事务正在访问数据,并且对数据进行了修改,而这种修改还没有提交到数据库中,<br/>
> 这时,另外一个事务也访问这个数据,然后使用了这个数据.<br/>
 <div align="center">
    <img src="https://raw.githubusercontent.com/PisecesPeng/PisecesPeng.record.me/master/resource/image/SpringTransactional/DirtyReads.png">
</div>
<br>

> 2. **不可重复读** : 是指在一个事务内,多次读同一数据.在这个事务还没有结束时,另外一个事务也访问该同一数据.<br/>
> 那么,在第一个事务中的两次读数据之间,由于第二个事务的修改,那么第一个事务两次读到的的数据可能是不一样的.<br/>
> 这样就发生了在一个事务内两次读到的数据是不一样的,因此称为是不可重复读.
 <div align="center">
    <img src="https://raw.githubusercontent.com/PisecesPeng/PisecesPeng.record.me/master/resource/image/SpringTransactional/Non-repeatableReads.png">
</div>
<br>

> 3. **幻读** : 是指当事务不是独立执行时发生的一种现象,例如第一个事务对一个表中的数据进行了修改,这种修改涉及到表中的全部数据行.<br/>
> 同时,第二个事务也修改这个表中的数据,这种修改是向表中插入一行新数据.<br/>
> 那么,以后就会发生操作第一个事务的用户发现表中还有没有修改的数据行,就好象发生了幻觉一样.<br/>
 <div align="center">
    <img src="https://raw.githubusercontent.com/PisecesPeng/PisecesPeng.record.me/master/resource/image/SpringTransactional/PhantomReads.png">
</div>
<br>

``` ps. 不可重复读的重点是'修改',同样的条件,你读取过的数据,再次读取出来发现'值'不一样了 ```<br/>
``` ps. 幻读的重点在于'新增/删除',同样的条件,你读取过的数据,再次读取出来发现'记录数'不一样了 ```
<hr>

### 2. @**Transactional**注解的属性一览

属性 | 类型 | 描述
---|---|---
value | String | 可选的限定描述符,指定使用的事务管理器
propagation | enum: Propagation | 可选的事务传播行为设置
isolation | enum: Isolation | 可选的事务隔离级别设置
readOnly | boolean | 设置当前事物为读写或只读事务
timeout | int(in seconds granularity) | 事务超时时间设置
rollbackFor | Class对象数组,必须继承自Throwable | 导致事务回滚的异常类数组
rollbackForClassName | 类名数组,必须继承自Throwable | 导致事务回滚的异常类名字数组
noRollbackFor | Class对象数组,必须继承自Throwable | 不会导致事务回滚的异常类数组
noRollbackForClassName | 类名数组,必须继承自Throwable | 不会导致事务回滚的异常类名字数组

#### value
```java
/* 
使用value去指定使用哪个事务管理器 
若默认没有'事务管理器',则可手动开启注解事务管理,
并创建事物管理器@Bean(name="testManager")
*/ 
@Transactional(value="testManager")
```

#### rollbackFor
```java
/*
使用rollbackFor去指定一个或多个异常类
如果异常被try{}catch{}了,事务就不回滚了,如果想让事务回滚必须再往外抛try{}catch{throw Exception}
*/
@Transactional(rollbackFor=Exception.class)
```

#### readOnly
```java
/*
只读事务用于客户代码只读但不修改数据的情形,只读事务用于特定情景下的优化,比如使用Hibernate的时候.默认为读写事务
"只读事务"并不是一个强制选项,它只是一个"暗示",提示数据库驱动程序和数据库系统,这个事务并不包含更改数据的操作,
那么JDBC驱动程序和数据库就有可能根据这种情况对该事务进行一些特定的优化,
比方说不安排相应的数据库锁,以减轻事务对数据库的压力,毕竟事务也是要消耗数据库的资源的.
但是你非要在"只读事务"里面修改数据,也并非不可以,只不过对于数据一致性的保护不像"读写事务"那样保险而已.
因此,"只读事务"仅仅是一个性能优化的推荐配置而已,并非强制你要这样做不可
*/
@Transactional(readOnly=true)
```
#### timeout
```java
/*
所谓事务超时,就是指一个事务所允许执行的最长时间,如果超过该时间限制但事务还没有完成,则自动回滚事务.
默认设置为底层事务系统的超时值,如果底层数据库事务系统没有设置超时值,那么就是none,没有超时限制.
*/
@Transactional(timeout=30)
```

#### propagation
```java
/*
所谓事务的传播行为是指,如果在开始当前事务之前,一个事务上下文已经存在,此时有若干选项可以指定一个事务性方法的执行行为.
REQUIRED : 如果当前存在事务,则加入该事务;如果当前没有事务,则创建一个新的事务.
SUPPORTS : 如果当前存在事务,则加入该事务;如果当前没有事务,则以非事务的方式继续运行.
MANDATORY : 如果当前存在事务,则加入该事务;如果当前没有事务,则抛出异常.
REQUIRES_NEW : 创建一个新的事务,如果当前存在事务,则把当前事务挂起.
NOT_SUPPORTED : 以非事务方式运行,如果当前存在事务,则把当前事务挂起.
NEVER : 以非事务方式运行,如果当前存在事务,则抛出异常.
NESTED : 如果当前存在事务,则创建一个事务作为当前事务的嵌套事务来运行;如果当前没有事务,则该取值等价于REQUIRED.
*/
@Transactional(propagation = Propagation.REQUIRED)
```

#### isolation
```java
/*
隔离级别是指若干个并发的事务之间的隔离程度. 
DEFAULT : 这是默认值,表示使用底层数据库的默认隔离级别.对大部分数据库而言,通常这值就是: READ_COMMITTED.
READ_UNCOMMITTED : 该隔离级别表示一个事务可以读取另一个事务修改但还没有提交的数据.该级别不能防止脏读和不可重复读,因此很少使用该隔离级别.
READ_COMMITTED : 该隔离级别表示一个事务只能读取另一个事务已经提交的数据.该级别可以防止脏读,这也是大多数情况下的推荐值.
REPEATABLE_READ : 该隔离级别表示一个事务在整个过程中可以多次重复执行某个查询,并且每次返回的记录都相同.即使在多次查询之间有新增的数据满足该查询,这些新增的记录也会被忽略.该级别可以防止脏读和不可重复读.
SERIALIZABLE : 所有的事务依次逐个执行,这样事务之间就完全不可能产生干扰,该级别可以防止脏读、不可重复读以及幻读.但是这将严重影响程序的性能.通常情况下也不会用到该级别.
*/
@Transactional(isolation = Isolation.DEFAULT)
```
<hr>
