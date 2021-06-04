## Guava Maps 常用方法示例

- [Guava Maps 常用方法示例](#guava-maps-常用方法示例)
  - [1. 构造不同的Map[new Map]](#1-构造不同的mapnew-map)
  - [2. 构造不同的Map[asMap]](#2-构造不同的mapasmap)
  - [3. Map之间的差异[difference]](#3-map之间的差异difference)
  - [4. Map的Entry过滤[filterEntries]](#4-map的entry过滤filterentries)
  - [5. Map的Key过滤[filterKeys]](#5-map的key过滤filterkeys)
  - [6. Map的Value过滤[filterValues]](#6-map的value过滤filtervalues)
  - [7. 根据Entry转换Map的Value[transformEntries]](#7-根据entry转换map的valuetransformentries)
  - [8. 根据Value转换Map的Value[transformValues]](#8-根据value转换map的valuetransformvalues)
  - [9. 不同的线程安全Map[synchronized*Map]](#9-不同的线程安全mapsynchronizedmap)
  - [10. 不同的不可修改Map[unmodifiable*Map]](#10-不同的不可修改mapunmodifiablemap)
  - [11. Converter转换器[asConverter]](#11-converter转换器asconverter)
  - [12. 输出限定范围的Key[subMap]](#12-输出限定范围的keysubmap)
  - [13. 根据key批量给定Map[toMap]](#13-根据key批量给定maptomap)
  - [14. 根据properties生成一个Map[fromProperties]](#14-根据properties生成一个mapfromproperties)
  - [15. Immutable小方法[immutable*]](#15-immutable小方法immutable)
  - [16. 生成Collector[toImmutableEnumMap]](#16-生成collectortoimmutableenummap)
  - [17. 根据value批量给定Map[uniqueIndex]](#17-根据value批量给定mapuniqueindex)

### 1. 构造不同的Map[new Map]

```java
HashMap hashMap1 = Maps.newHashMap();  // 构造一个可变的空的HashMap实例
HashMap hashMap2 = Maps.newHashMap(new HashMap<>());  // 构造一个与给定map有相同映射关系的可变HashMap实例
HashMap hashMap3 = Maps.newHashMapWithExpectedSize(23);  // 构造一个期望长度为expectedSize的HashMap实例.

EnumMap enumMap1 = Maps.newEnumMap(MapEnum.class);  // 构造一个具有给定键类型的EnumMap实例
EnumMap enumMap2 = Maps.newEnumMap(new EnumMap<>(MapEnum.class));  // 构造一个与给定map有相同映射关系的EnumMap实例

TreeMap treeMap1 = Maps.newTreeMap();  // 构造一个可变的、空的TreeMap实例
TreeMap treeMap3 = Maps.newTreeMap(Comparator.comparingInt(Object::hashCode));  // 构造一个可变的、空的、使用给定比较器的TreeMap实例
TreeMap treeMap2 = Maps.newTreeMap(new TreeMap<>());  // 构造一个可变的、与给定SortedMap有相同映射关系的TreeMap实例

LinkedHashMap linkedHashMap1 = Maps.newLinkedHashMap();  // 构造一个可变的、空的、插入排序的LinkedHashMap实例
LinkedHashMap linkedHashMap2 = Maps.newLinkedHashMap(new LinkedHashMap<>());  // 构造一个可变的、插入排序的、与给定map有相同映射关系的LinkedHashMap实例
LinkedHashMap linkedHashMap3 = Maps.newLinkedHashMapWithExpectedSize(23);  // 构造一个期望长度为expectedSize的LinkedHashMap实例

ConcurrentMap<Object, Object> concurrentHashMap1 = Maps.newConcurrentMap();  // 构造一个ConcurrentMap实例

IdentityHashMap identityHashMap1 = Maps.newIdentityHashMap();  // 构造一个IdentityHashMap实例
```

---

### 2. 构造不同的Map[asMap]

```java
Map map1 = Maps.asMap(  // 返回一个活动的map, 键值为给定的set中的值, value为通过给定Function计算后的值
        new HashSet<Object>() {{
            add("a");
            add("b");
        }},
        (x) -> x.hashCode()
);

SortedMap sortedMap1 = Maps.asMap(  // 返回有序Set集合的map表示, 根据给定的Function从给定的Set中映射键值
        new TreeSet<Object>() {{  // TreeSet extends SortedSet<E>
            add("a");
            add("b");
        }},
        (x) -> x.hashCode()
);

NavigableMap navigableMap1 = Maps.asMap(  // 返回NavigableSet集合的map表示, 根据给定的Function从给定的Set中映射键值
        new TreeSet<Object>() {{  // TreeSet implements NavigableSet<E>
            add("a");
            add("b");
        }},
        (x) -> x.hashCode()
);
```

---

### 3. Map之间的差异[difference]

```java
MapDifference mapDifference1 = Maps.difference(  // 返回两个给定map之间的差异
        new HashMap<String, String>() {{
            put("a", "a");
            put("b", "1");
            put("c", "c");
        }},
        new HashMap<String, String>() {{
            put("a", "a");
            put("b", "2");
            put("d", "d");
        }}
);
System.out.println(mapDifference1.entriesDiffering().entrySet());  // 输出: [b=(1, 2)]
System.out.println(mapDifference1.entriesInCommon().entrySet());  // 输出: [a=a]
System.out.println(mapDifference1.entriesOnlyOnLeft().entrySet());  // 输出: [c=c]
System.out.println(mapDifference1.entriesOnlyOnRight().entrySet());  // 输出: [d=d]

MapDifference mapDifference2 = Maps.difference(  // 返回两个已排序的Map之间的差异, 通过给定left的比较器
        new TreeMap<String, String>() {{
            put("a", "a");
            put("b", "1");
            put("c", "c");
        }},
        new HashMap<String, String>() {{
            put("a", "a");
            put("b", "2");
            put("d", "d");
        }}
);
System.out.println(mapDifference2.entriesDiffering().entrySet());  // 输出: [b=(1, 2)]
System.out.println(mapDifference2.entriesInCommon().entrySet());  // 输出: [a=a]
System.out.println(mapDifference2.entriesOnlyOnLeft().entrySet());  // 输出: [c=c]
System.out.println(mapDifference2.entriesOnlyOnRight().entrySet());  // 输出: [d=d]

MapDifference mapDifference3 = Maps.difference(  // 返回两个给定map之间的差异, 通过所提供的valueEquivalence进行等值比较
        new HashMap<String, String>() {{
            put("a", "a");
            put("b", "1");
            put("c", "c");
        }},
        new HashMap<String, String>() {{
            put("a", "a");
            put("b", "2");
            put("d", "d");
        }},
        new Equivalence<String>() {
            @Override
            protected boolean doEquivalent(String a, String b) {
                return a.hashCode() < b.hashCode();
            }

            @Override
            protected int doHash(String s) {
                return 0;
            }
        }
);
System.out.println(mapDifference3.entriesDiffering().entrySet());  // 输出: []
System.out.println(mapDifference3.entriesInCommon().entrySet());  // 输出: [a=a, b=1]
System.out.println(mapDifference3.entriesOnlyOnLeft().entrySet());  // 输出: [c=c]
System.out.println(mapDifference3.entriesOnlyOnRight().entrySet());  // 输出: [d=d]
```

---

### 4. Map的Entry过滤[filterEntries]

```java
Map map1 = Maps.filterEntries(  // 返回给定Map中的Entry值通过给定Predicate过滤后的map映射
        new HashMap<String, String>() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }},
        (x) -> x.getKey().hashCode() >= "b".hashCode() && x.getValue().hashCode() > "2".hashCode()
);
System.out.println(map1.entrySet());  // 输出: [c=3]

SortedMap sortedMap1 = Maps.filterEntries(  // 返回给定Map中的Entry值通过给定Predicate过滤后的SortedMap映射
        new TreeMap() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }},
        (x) -> x.getKey().hashCode() >= "b".hashCode() && x.getValue().hashCode() > "2".hashCode()
);
System.out.println(sortedMap1.entrySet());  // 输出: [c=3]

NavigableMap navigableMap1 = Maps.filterEntries(  // 返回给定Map中的Entry值通过给定Predicate过滤后的NavigableMap映射
        new TreeMap() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }},
        (x) -> x.getKey().hashCode() >= "b".hashCode() && x.getValue().hashCode() > "2".hashCode()
);
System.out.println(navigableMap1.entrySet());  // 输出: [c=3]

BiMap biMap1 = Maps.filterEntries(  // 返回给定Map中的Entry值通过给定Predicate过滤后的BiMap映射
        HashBiMap.create(new HashMap<Object, Object>() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }}),
        (x) -> x.getKey().hashCode() >= "b".hashCode() && x.getValue().hashCode() > "2".hashCode()
);
System.out.println(biMap1.entrySet());  // 输出: [c=3]
```

---

### 5. Map的Key过滤[filterKeys]

```java
Map map1 = Maps.filterKeys(  // 返回给定Map中的Key值通过给定Predicate过滤后的map映射
        new HashMap<String, String>() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }},
        (x) -> x.hashCode() >= "b".hashCode()
);
System.out.println(map1.entrySet());  // 输出: [b=2, c=3]

SortedMap sortedMap1 = Maps.filterKeys(  // 返回给定Map中的Key值通过给定Predicate过滤后的SortedMap映射
        new TreeMap() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }},
        (x) -> x.hashCode() >= "b".hashCode()
);
System.out.println(sortedMap1.entrySet());  // 输出: [b=2, c=3]

NavigableMap navigableMap1 = Maps.filterKeys(  // 返回给定Map中的Key值通过给定Predicate过滤后的NavigableMap映射
        new TreeMap() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }},
        (x) -> x.hashCode() >= "b".hashCode()
);
System.out.println(navigableMap1.entrySet());  // 输出: [b=2, c=3]

BiMap biMap1 = Maps.filterKeys(  // 返回给定Map中的Key值通过给定Predicate过滤后的BiMap映射
        HashBiMap.create(new HashMap<Object, Object>() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }}),
        (x) -> x.hashCode() >= "b".hashCode()
);
System.out.println(biMap1.entrySet());  // 输出: [b=2, c=3]
```

---

### 6. Map的Value过滤[filterValues]

```java
 Map map1 = Maps.filterValues(  // 返回给定Map中的Value值通过给定Predicate过滤后的map映射
        new HashMap<String, String>() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }},
        (x) -> x.hashCode() >= "3".hashCode()
);
System.out.println(map1.entrySet());  // 输出: [c=3]

SortedMap sortedMap1 = Maps.filterValues(  // 返回给定Map中的Value值通过给定Predicate过滤后的SortedMap映射
        new TreeMap() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }},
        (x) -> x.hashCode() >= "3".hashCode()
);
System.out.println(sortedMap1.entrySet());  // 输出: [c=3]

NavigableMap navigableMap1 = Maps.filterValues(  // 返回给定Map中的Value值通过给定Predicate过滤后的NavigableMap映射
        new TreeMap() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }},
        (x) -> x.hashCode() >= "3".hashCode()
);
System.out.println(navigableMap1.entrySet());  // 输出: [c=3]

BiMap biMap1 = Maps.filterValues(  // 返回给定Map中的Value值通过给定Predicate过滤后的BiMap映射
        HashBiMap.create(new HashMap<Object, Object>() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }}),
        (x) -> x.hashCode() >= "3".hashCode()
);
System.out.println(biMap1.entrySet());  // 输出: [c=3]
```

---

### 7. 根据Entry转换Map的Value[transformEntries]

```java
Map map1 = Maps.transformEntries(  // 返回一个Map映射, 其Entry为给定fromMap.Entry通过给定EntryTransformer转换后的值
        new HashMap<String, String>() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }},
        (Maps.EntryTransformer<String, String, Object>) (key, value) -> key.toUpperCase()
);
System.out.println(map1.entrySet());  // 输出: [a=A, b=B, c=C]

SortedMap sortedMap1 = Maps.transformEntries(  // 返回一个SortedMap映射, 其Entry为给定fromMap.Entry通过给定EntryTransformer转换后的值
        new TreeMap<String, String>() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }},
        (Maps.EntryTransformer<String, String, Object>) (key, value) -> key.toUpperCase()
);
System.out.println(sortedMap1.entrySet());  // 输出: [a=A, b=B, c=C]

NavigableMap navigableMap1 = Maps.transformEntries(  // 返回一个NavigableMap映射, 其Entry为给定fromMap.Entry通过给定EntryTransformer转换后的值
        new TreeMap<String, String>() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
        }},
        (Maps.EntryTransformer<String, String, Object>) (key, value) -> key.toUpperCase()
);
System.out.println(navigableMap1.entrySet());  // 输出: [a=A, b=B, c=C]
```

---

### 8. 根据Value转换Map的Value[transformValues]

```java
Map map1 = Maps.transformValues(  // 返回一个Map映射, 其value为给定Map中value通过Function转换后的值
        new HashMap<String, Integer>() {{
            put("a", 1);
            put("b", 2);
            put("c", 3);
        }},
        (x) -> x + 10
);
System.out.println(map1.entrySet());  // 输出: [a=11, b=12, c=13]

SortedMap sortedMap1 = Maps.transformValues(  // 返回一个SortedMap映射, 其value为给定Map中value通过Function转换后的值
        new TreeMap<String, Integer>() {{
            put("a", 1);
            put("b", 2);
            put("c", 3);
        }},
        (x) -> x + 10
);
System.out.println(sortedMap1.entrySet());  // 输出: [a=11, b=12, c=13]

NavigableMap navigableMap1 = Maps.transformValues(  // 返回一个NavigableMap映射, 其value为给定Map中value通过Function转换后的值
        new TreeMap<String, Integer>() {{
            put("a", 1);
            put("b", 2);
            put("c", 3);
        }},
        (x) -> x + 10
);
System.out.println(navigableMap1.entrySet());  // 输出: [a=11, b=12, c=13]
```

---

### 9. 不同的线程安全Map[synchronized*Map]

```java
BiMap bimap1 = Maps.synchronizedBiMap(HashBiMap.create());  // 返回一个同步的(线程安全)的bimap, 由给定的bimap支持

NavigableMap navigableMap1 = Maps.synchronizedNavigableMap(new TreeMap<>());  // 返回一个同步的(线程安全)的NavigableMap, 由给定的navigableMap支持
```

---

### 10. 不同的不可修改Map[unmodifiable*Map]

```java
BiMap<Object, Object> bimap1 = Maps.unmodifiableBiMap(HashBiMap.create());  // 返回给定的bimap的不可修改的BiMap表示.

NavigableMap navigableMap1 = Maps.unmodifiableNavigableMap(new TreeMap<>());  // 返回给定的navigableMap的不可修改的NavigableMap表示.
```

---

### 11. Converter转换器[asConverter]

```java
Converter converter1 = Maps.asConverter(HashBiMap.create(new HashMap<>()));  // 返回一个Converter转换器, 通过bimap.get()方法转换值, 它的逆视图使用bimap.inverse().get()方法转换值
```

---

### 12. 输出限定范围的Key[subMap]

```java
NavigableMap navigableMap1 = Maps.subMap(  // 返回其键包含在范围内的map部分的视图
        new TreeMap<String, String>() {{
            put("a", "1");
            put("b", "2");
            put("c", "3");
            put("d", "4");
            put("e", "5");
        }},
        Range.range("b", BoundType.OPEN, "d", BoundType.CLOSED)
);  // 返回其键包含在范围内的map部分的视图
System.out.println(navigableMap1.entrySet());  // 输出: [c=3, d=4]
```

---

### 13. 根据key批量给定Map[toMap]

```java
// toMap()有(Iterable, Function)与(Iterator, Function)两种参数类型
ImmutableMap immutableMap1 = Maps.toMap(  // 返回一个不可变的ImmutableMap实例, 其键值为给定keys中去除重复值后的值, 其值为键被计算了Function后的值
        new ArrayList<Object>() {{
            add("a");
            add("b");
            add("b");
            add("c");
        }},
        (x) -> x.hashCode()
);
System.out.println(immutableMap1.entrySet());  // 输出: [a=97, b=98, c=99]
```

---

### 14. 根据properties生成一个Map[fromProperties]

```java
ImmutableMap immutableMap1 = Maps.fromProperties(new Properties() {{  // 通过给定的Properties实例, 返回一个不可变的ImmutableMap实例
    setProperty("a", "1");
    setProperty("b", "2");
    setProperty("c", "3");
}});
System.out.println(immutableMap1.entrySet());  // 输出: [b=2, a=1, c=3]
```

---

### 15. Immutable小方法[immutable*]

```java
Map.Entry entry = Maps.immutableEntry("a", "1");  // 通过给定的key/value, 返回一个不可变的map映射
System.out.println(entry);  // 输出: a=1

ImmutableMap immutableMap1 = Maps.immutableEnumMap(new HashMap<MapEnum, Object>(){{
    put(MapEnum.A, "aaa");
    put(MapEnum.B, "bbb");
}});  // 返回包含给定项的不可变映射实例
System.out.println(immutableMap1.entrySet());  // 输出: [A=aaa, B=bbb]
```

---

### 16. 生成Collector[toImmutableEnumMap]

```java
Collector collector1 = Maps.toImmutableEnumMap(  // 返回一个Collector，它将元素累积到ImmutableMap中，其中的键和值是将提供的映射函数应用于输入元素的结果
        (x) -> MapEnum.valueOf((String) x),
        (x) -> MapEnum.valueOf((String) x).getCode()
);

Collector collector2 = Maps.toImmutableEnumMap(  // 返回一个Collector，它将元素累积到ImmutableMap中，其中的键和值是将提供的映射函数应用于输入元素的结果
        (x) -> MapEnum.valueOf((String) x),
        (x) -> MapEnum.valueOf((String) x).getCode(),
        (BinaryOperator) (o, o2) -> o.hashCode() + o2.hashCode()
);
```

---

### 17. 根据value批量给定Map[uniqueIndex]

```java
// uniqueIndex()有(Iterable, Function)与(Iterator, Function)两种参数类型
ImmutableMap immutableMap1 = Maps.uniqueIndex(  // List元素作为Map的value, 函数式接口用于通过List元素确定Map的key
        new ArrayList<String>() {{
            add("a");
            add("b");
        }},
        (x) -> x.toUpperCase()
);
System.out.println(immutableMap1.entrySet());  // 输出: [A=a, B=b]
```

---

> 本文中使用的的Enum代码如下

```java
enum MapEnum {
    A("a"),
    B("b"),
    C("c");

    private String code;

    MapEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
```
