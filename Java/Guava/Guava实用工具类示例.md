## Guava实用工具类示例

- [Guava实用工具类示例](#guava实用工具类示例)
  - [1. 可存在多个Value的map - Multimap](#1-可存在多个value的map---multimap)
  - [2. 自由反转的Map - BiMap](#2-自由反转的map---bimap)
  - [3. 有行有列的Table - Table](#3-有行有列的table---table)
  - [4. 统计数量的Set - Multiset](#4-统计数量的set---multiset)

> **Guava**封装了很多不错的工具类, 本文主要简单介绍下我常用的几种.

---

### 1. 可存在多个Value的map - Multimap

```java
// import com.google.common.collect.ArrayListMultimap;
// import com.google.common.collect.HashMultimap;
// import com.google.common.collect.Multimap;

// value不去重
Multimap arrayListMultimap = ArrayListMultimap.create();
arrayListMultimap.put("a", "123");
arrayListMultimap.put("b", "456");
arrayListMultimap.put("a", "123");
arrayListMultimap.put("a", "789");
System.out.println(arrayListMultimap);  // {a=[123, 123, 789], b=[456]}

Map<String, List<String>> arrayListMap = arrayListMultimap.asMap();
System.out.println(arrayListMap);  // {a=[123, 123, 789], b=[456]}

// value去重
Multimap hashMultimap = HashMultimap.create();
hashMultimap.put("a", "123");
hashMultimap.put("b", "456");
hashMultimap.put("a", "123");
hashMultimap.put("a", "789");
System.out.println(hashMultimap);  // {a=[789, 123], b=[456]}

Map<String, Set<String>> setMap = hashMultimap.asMap();
System.out.println(setMap);  // {a=[789, 123], b=[456]} 
```

### 2. 自由反转的Map - BiMap

```java
// import com.google.common.collect.BiMap;
// import com.google.common.collect.HashBiMap;

BiMap<String, String> biMap = HashBiMap.create();

biMap.put("key", "value");
System.out.println(biMap); // {key=value}

// 如果value重复, put方法会抛异常, 除非用forcePut方法
biMap.forcePut("key", "valuechange");
System.out.println(biMap); // {key=valuechange}

// 翻转key/value
BiMap<String, String> inverse = biMap.inverse();
System.out.println(inverse); // {valuechange=key}
```

### 3. 有行有列的Table - Table

```java
// import com.google.common.collect.HashBasedTable;
// import com.google.common.collect.Table;

Table<Integer, String, String> table = HashBasedTable.create();
table.put(18, "男", "zhangsan");
table.put(18, "女", "Lily");
table.put(19, "男", "zhangsan");
System.out.println(table);  // {18={男=zhangsan, 女=Lily}, 19={男=zhangsan}}
System.out.println(table.get(18, "男"));  // zhangsan
// 行数据
Map<String, String> row = table.row(18);
System.out.println(row);  // {男=zhangsan, 女=Lily}
// 列数据
Map<Integer, String> column = table.column("男");
System.out.println(column);  // {18=zhangsan, 19=zhangsan}
// 数据
Collection<String> values = table.values();
System.out.println(values);  // [zhangsan, Lily, zhangsan]
```

### 4. 统计数量的Set - Multiset

```java
Multiset<String> multiset = HashMultiset.create();
multiset.add("a");
multiset.add("b");
multiset.add("b");
multiset.add("a", 2);
System.out.println(multiset.count("a"));  // 3
// 查看去重的元素
Set<String> set = multiset.elementSet();
System.out.println(set);  // [a, b]
// 还能查看没有去重的元素
System.out.println(multiset);  // [a, a, a, b, b]
// 还能手动设置某个元素出现的次数
multiset.setCount("a", 5);
System.out.println(multiset.entrySet());
```

