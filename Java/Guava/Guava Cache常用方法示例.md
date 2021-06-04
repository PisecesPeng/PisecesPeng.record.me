## Guava Cache 常用方法示例

- [Guava Cache 常用方法示例](#guava-cache-常用方法示例)
  - [1. 最基础的例子[CacheBuilder]](#1-最基础的例子cachebuilder)
  - [2. 若无缓存时,自定义缓存值[CacheLoader、get()]](#2-若无缓存时自定义缓存值cacheloaderget)
  - [3. 控制缓存的大小/多少[.maximumSize()、.maximumWeight()]](#3-控制缓存的大小多少maximumsizemaximumweight)
  - [4. 控制缓存回收的时间[.expireAfterWrite()、.expireAfterAccess()]](#4-控制缓存回收的时间expireafterwriteexpireafteraccess)
  - [5. 缓存更新[.refreshAfterWrite()]](#5-缓存更新refreshafterwrite)
  - [6. 手动清除缓存[.invalidate()、.invalidateAll()]](#6-手动清除缓存invalidateinvalidateall)
  - [7. 设置监听器[.removalListener()]](#7-设置监听器removallistener)
  - [8. 自带的统计功能[.recordStats()]](#8-自带的统计功能recordstats)
  - [9. 显示缓存中的数据[.asMap()]](#9-显示缓存中的数据asmap)

> Guava Cache有一些优点如下:
> 1. 线程安全的缓存,与ConcurrentMap相似(前者更"好"),在高并发情况下、能够正常缓存更新以及返回.  
> 2. 提供了三种基本的缓存回收方式:基于容量回收、定时回收和基于引用回收(本文没有提及引用回收).  
> 3. 提供了两种定时回收:按照写入时间,最早写入的最先回收；按照访问时间,最早访问的最早回收.  
> 4. 可以监控缓存加载/命中情况.  
> 5. 使用方便、简单.  

---

### 1. 最基础的例子[CacheBuilder]

```java
// 新建CacheBuilder
Cache<Integer, String> cache = CacheBuilder.newBuilder().build();
cache.put(1, "a");
cache.put(2, "b");
System.out.println(cache.getIfPresent(1));  // 输出: a
System.out.println(cache.getIfPresent(3));  // 输出: null
System.out.println(cache.getAllPresent(new ArrayList<Integer>(){{
    add(1);
    add(2);
}}));  // 输出: {1=a, 2=b}
```

---

### 2. 若无缓存时,自定义缓存值[CacheLoader、get()]

```java
// 遇到不存在的key,定义默认缓存值
// 1. 在cache定义时设置通用缓存模版
LoadingCache<Integer, String> cache1 = CacheBuilder.newBuilder().build(
        new CacheLoader<Integer, String>() {
            @Override
            public String load(Integer key) throws Exception {
                return "hellokey" + key;
            }
        }
);
cache1.put(1, "a");
System.out.println(cache1.getIfPresent(1));  // 输出: a
try {
    System.out.println(cache1.getAll(new ArrayList<Integer>(){{  // getAll()将没有命中的key调用load()方法去加载数据
        add(1);
        add(2);
    }}));  // 输出: {1=a, 2=hellokey2}
    System.out.println(cache1.get(3));  // 输出: hellokey3
} catch (ExecutionException e) {
    e.printStackTrace();
}

// 2. 在获取缓存值时设置缓存
Cache<Integer, String> cache2 = CacheBuilder.newBuilder().build();
cache2.put(1, "a");
System.out.println(cache2.getIfPresent(1));  // 输出: a
try {
    String value = cache2.get(2, () -> "hellokey2");
    System.out.println(value);  // 输出: hellokey2
} catch (ExecutionException e) {
    e.printStackTrace();
}
```

---

### 3. 控制缓存的大小/多少[.maximumSize()、.maximumWeight()]

```java
// ps. .maximumSize(long),.maximumWeight(long)互斥,build()只可以二选一
// 1. 基于缓存多少
Cache<Integer, String> cache1 = CacheBuilder.newBuilder()
        .maximumSize(2L)  // 设置缓存上限,最多两个
        .build();
cache1.put(1, "a");
cache1.put(2, "b");
cache1.put(3, "c");
System.out.println(cache1.asMap());  // 输出: {3=c, 2=b}
System.out.println(cache1.getIfPresent(2));  // 输出: b
cache1.put(4, "d");
System.out.println(cache1.asMap());  // 输出: {2=b, 4=d}

// 2. 基于缓存大小
Cache<Integer, Integer> cache2 = CacheBuilder.newBuilder()
        .maximumWeight(100L)  // 指定最大总重
        .weigher((Weigher<Integer, Integer>) (key, value) -> {
            if (value % 2 == 0) {
                return 20;  // 偶数,则权重为20
            } else {
                return 5;  // 非偶数,则权重为5
            }
        })  // 设置权重函数
        .build();
for (int i = 0; i <= 20; i += 2) {
    cache2.put(i, i);
}
System.out.println(cache2.asMap());  // 输出: {20=20, 18=18, 16=16, 14=14}
cache2.invalidateAll();  // 清除所有的缓存
for (int i = 0; i <= 20; i += 1) {
    cache2.put(i, i);
}
System.out.println(cache2.asMap());  // 输出: {20=20, 19=19, 18=18, 17=17, 16=16, 14=14, 11=11}
```

---

### 4. 控制缓存回收的时间[.expireAfterWrite()、.expireAfterAccess()]

```java
// 1. 设置缓存写入后多久过期
Cache<Integer, Integer> cache1 = CacheBuilder.newBuilder()
        .expireAfterWrite(2, TimeUnit.SECONDS)  // 缓存写入2s后过期
        .build();
cache1.put(1,1);
System.out.println(cache1.asMap());  // 输出: {1=1}
try {
    Thread.sleep(3000);
} catch (InterruptedException e) {
    e.printStackTrace();
}
System.out.println(cache1.asMap());  // 输出: {}

// 2. 设置缓存读取后多久过期
Cache<Integer, Integer> cache2 = CacheBuilder.newBuilder()
        .expireAfterAccess(2, TimeUnit.SECONDS)  // 缓存读取2s后过期
        .build();
cache2.put(1,1);
try {
    Thread.sleep(3000);
} catch (InterruptedException e) {
    e.printStackTrace();
}
cache2.getIfPresent(1);
System.out.println(cache2.asMap());  // 输出: {1=1}
try {
    Thread.sleep(3000);
} catch (InterruptedException e) {
    e.printStackTrace();
}
System.out.println(cache2.asMap());  // 输出: {}
```

---

### 5. 缓存更新[.refreshAfterWrite()]

```java
// 设置更新时间, 定时去更新缓存中的数据
LoadingCache<String, String> cache = CacheBuilder.newBuilder()
        .refreshAfterWrite(2, TimeUnit.SECONDS)  // 缓存写入2s后更新
        .build(new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                return UUID.randomUUID().toString();  // 假设是个查库操作之类的..
            }
        });
cache.put("1", "1");
System.out.println(cache.getIfPresent("1"));  // 输出: 1
try {
    Thread.sleep(3000);
} catch (InterruptedException e) {
    e.printStackTrace();
}
System.out.println(cache.getIfPresent("1"));  // 输出: a6c1bf6f-61a7-46cb-99d9-ef7a81a7cacc
cache.refresh("1");  // 刷新key="1"的值,不需要等待更新时间
System.out.println(cache.getIfPresent("1"));  // 输出: bf4912c1-b3a4-45f7-a50a-de2bb98757db
```

---

### 6. 手动清除缓存[.invalidate()、.invalidateAll()]

```java
// 清除缓存中的数据
Cache<Integer, String> cache = CacheBuilder.newBuilder().build();
cache.put(1, "a");
System.out.println(cache.asMap());  // 输出: {1=a}
cache.invalidateAll();  // 清除所有缓存
System.out.println(cache.asMap());  // 输出: {}
cache.put(2, "b");
System.out.println(cache.asMap());  // 输出: {2=b}
cache.invalidate(2);  // 清除指定缓存
System.out.println(cache.asMap());  // 输出: {}
cache.put(1, "a");
cache.put(2, "b");
cache.put(3, "c");
System.out.println(cache.asMap());  // 输出: {2=b, 1=a, 3=c}
cache.invalidateAll(new ArrayList<Integer>() {{  // 批量清除缓存
    add(1);
    add(2);
}});
System.out.println(cache.asMap());  // 输出: {3=c}
```

---

### 7. 设置监听器[.removalListener()]

```java
// 设置移除监听器(ps. 当移除缓存时,会打印出被移除缓存的信息(基于模版格式))
LoadingCache<Integer, Integer> cache = CacheBuilder.newBuilder()
        .expireAfterWrite(2, TimeUnit.SECONDS)  // 设置2s后过期时间
        .removalListener(notification -> System.out.println(
                "remove key[" + notification.getKey()
                        + "],value[" + notification.getValue()
                        + "],remove reason[" + notification.getCause() + "]")
        )  // 设置移除监听器,并设置输出模版
        .build(
                new CacheLoader<Integer, Integer>() {
                    @Override
                    public Integer load(Integer key) throws Exception {
                        return 2;  // 当无值时, 设置默认值
                    }
                }
        );
cache.put(1, 1);
cache.put(2, 2);
System.out.println(cache.asMap());  // 输出: {2=2, 1=1}
cache.invalidateAll();
System.out.println(cache.asMap());  // 输出: {}
cache.put(3, 3);
try {
    // ps. 如果定义的CacheLoader没有声明任何检查型异常,则可以通过getUnchecked()取值
    System.out.println(cache.getUnchecked(3));  // 输出: 3
    Thread.sleep(3000);
    System.out.println(cache.getUnchecked(3));  // 输出: 2
} catch (InterruptedException e) {
    e.printStackTrace();
}
```

---

### 8. 自带的统计功能[.recordStats()]

```java
// 开启统计,并查看统计信息
LoadingCache<String, String> cache = CacheBuilder.newBuilder()
        .recordStats()  // 开启统计功能
        .refreshAfterWrite(2, TimeUnit.SECONDS)  // 缓存写入2s后更新
        .build(new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                return UUID.randomUUID().toString();  // 假设是个查库操作之类的..
            }
        });
cache.put("1", "a");
System.out.println(cache.asMap());  // 输出: {1=a}
System.out.println(cache.stats());  // 输出: CacheStats{hitCount=0, missCount=0, loadSuccessCount=0, loadExceptionCount=0, totalLoadTime=0, evictionCount=0}
cache.getIfPresent("2");
System.out.println(cache.asMap());  // 输出: {1=a}
System.out.println(cache.stats());  // 输出: CacheStats{hitCount=0, missCount=1, loadSuccessCount=0, loadExceptionCount=0, totalLoadTime=0, evictionCount=0}
try {
    Thread.sleep(3000);
} catch (InterruptedException e) {
    e.printStackTrace();
}
cache.getIfPresent("1");
System.out.println(cache.asMap());  // 输出: {1=0207bb01-7b3c-4b66-b575-9fb2c5511a96}
System.out.println(cache.stats());  // 输出: CacheStats{hitCount=1, missCount=1, loadSuccessCount=1, loadExceptionCount=0, totalLoadTime=21118733, evictionCount=0}
/**
 * hitCount;  // 缓存命中数
 * missCount; // 缓存未命中数
 * loadSuccessCount; // load成功数
 * loadExceptionCount; // load异常数
 * totalLoadTime; // load的总共耗时
 * evictionCount; // 缓存项被回收的总数,不包括显式清除
 */
```

---

### 9. 显示缓存中的数据[.asMap()]

```java
// asMap视图
Cache<Integer, String> cache = CacheBuilder.newBuilder().build();
cache.put(1, "a");
cache.put(2, "b");
cache.put(3, "c");
cache.asMap();  // 返回的是个ConcurrentMap
System.out.println(cache.asMap().containsKey(1));  // 输出: true
System.out.println(cache.asMap().containsValue("b"));  // 输出: true
System.out.println(cache.asMap().get(1));  // 输出: a
System.out.println(cache.asMap().put(5, "e"));  // 输出: null
System.out.println(cache.asMap().entrySet());  // 输出: [5=e, 2=b, 1=a, 3=c]
System.out.println(cache.asMap());  // 输出: {5=e, 2=b, 1=a, 3=c}
```
