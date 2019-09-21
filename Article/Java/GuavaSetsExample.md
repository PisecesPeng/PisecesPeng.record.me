<h2> Guava Sets常用方法示例 </h2>

- [1. 常用构造HashSet实例[newHashSet]](#1-%E5%B8%B8%E7%94%A8%E6%9E%84%E9%80%A0hashset%E5%AE%9E%E4%BE%8Bnewhashset)
- [2. 常用构造EnumSet实例[new*EnumSet]](#2-%E5%B8%B8%E7%94%A8%E6%9E%84%E9%80%A0enumset%E5%AE%9E%E4%BE%8Bnewenumset)
- [3. 常用构造TreeSet实例[newTreeSet]](#3-%E5%B8%B8%E7%94%A8%E6%9E%84%E9%80%A0treeset%E5%AE%9E%E4%BE%8Bnewtreeset)
- [4. 常用构造CopyOnWriteArraySet实例[newCopyOnWriteArraySet]](#4-%E5%B8%B8%E7%94%A8%E6%9E%84%E9%80%A0copyonwritearrayset%E5%AE%9E%E4%BE%8Bnewcopyonwritearrayset)
- [5. 常用构造LinkedHashSet实例[newLinkedHashSet]](#5-%E5%B8%B8%E7%94%A8%E6%9E%84%E9%80%A0linkedhashset%E5%AE%9E%E4%BE%8Bnewlinkedhashset)
- [6. 构造其它Set实例[*NavigableSet]](#6-%E6%9E%84%E9%80%A0%E5%85%B6%E5%AE%83set%E5%AE%9E%E4%BE%8Bnavigableset)
- [7. 返回不可修改SetView[Sets.SetView]](#7-%E8%BF%94%E5%9B%9E%E4%B8%8D%E5%8F%AF%E4%BF%AE%E6%94%B9setviewsetssetview)
- [8. 任意匹配出不同的Set[cartesianProduct]](#8-%E4%BB%BB%E6%84%8F%E5%8C%B9%E9%85%8D%E5%87%BA%E4%B8%8D%E5%90%8C%E7%9A%84setcartesianproduct)
- [9. 反向获得其余枚举值[complementOf]](#9-%E5%8F%8D%E5%90%91%E8%8E%B7%E5%BE%97%E5%85%B6%E4%BD%99%E6%9E%9A%E4%B8%BE%E5%80%BCcomplementof)
- [10. set元素过滤[filter]](#10-set%E5%85%83%E7%B4%A0%E8%BF%87%E6%BB%A4filter)
- [11. 生成Collector[toImmutableEnumSet]](#11-%E7%94%9F%E6%88%90collectortoimmutableenumset)

<hr>

### 1. 常用构造HashSet实例[newHashSet]
```java
HashSet hashset1 = Sets.newHashSet();  // 构造一个可变的空的HashSet实例
HashSet hashSet2 = Sets.newHashSet("a", "b", "a", "c");  // 构造一个包含给定元素的可变的空HashSet实例
HashSet hashSet3 = Sets.newHashSet(new ArrayList<String>() {{  //  构造一个包含给定元素的可变的空HashSet实例, 且元素实现Iterable接口
    add("a");
    add("b");
    add("a");
    add("c");
}});
HashSet hashSet4 = Sets.newHashSetWithExpectedSize(30);  // 构造一个期望长度为expectedSize的HashSet实例

hashset1.forEach(System.out::print);  // 输出:
hashSet2.forEach(System.out::print);  // 输出: abc
hashSet3.forEach(System.out::print);  // 输出: abc
hashSet4.forEach(System.out::print);  // 输出:

// 构造一个线程安全的Set，由ConcurrentHashMap的实例支持，因此进行了相同的并发性担保，与HashSet不同的是，这个Set不允许null元素，该Set是可序列化的
Set set1 = Sets.newConcurrentHashSet();
Set set2 = Sets.newConcurrentHashSet(new ArrayList<String>() {{  //  构造一个包含给定元素的可变的空HashSet实例, 且元素实现Iterable接口
    add("a");
    add("b");
    add("a");
    add("c");
}});

set1.forEach(System.out::print);  // 输出:
set2.forEach(System.out::print);  // 输出: abc

Set set3 = Sets.newIdentityHashSet();  // 构造一个空的Set，使用特性来确定是否相等
set3.forEach(System.out::print);  // 输出:
```
<hr>

### 2. 常用构造EnumSet实例[new*EnumSet]
```java
// 构造一个新的可变的EnumSet实例，该实例包含按自然顺序排列的给定元素
EnumSet enumSet1 = Sets.newEnumSet(new ArrayList<SetEnum>() {{
    add(SetEnum.A);
    add(SetEnum.B);
    add(SetEnum.A);
    add(SetEnum.C);
}}, SetEnum.class);

// 构造一个包含给定枚举元素的不可变的Set实例
ImmutableSet immutableSet1 = Sets.immutableEnumSet(SetEnum.A, SetEnum.B);
ImmutableSet immutableSet2 = Sets.immutableEnumSet(new ArrayList<SetEnum>() {{
    add(SetEnum.A);
    add(SetEnum.B);
    add(SetEnum.A);
    add(SetEnum.C);
}});

enumSet1.forEach(System.out::print);  // 输出: ABC
immutableSet1.forEach(System.out::print);  // 输出: AB
immutableSet2.forEach(System.out::print);  // 输出: ABC
```
<hr>

### 3. 常用构造TreeSet实例[newTreeSet]
```java
TreeSet treeSet1 = Sets.newTreeSet();  // 构造一个可变的空的TreeSet实例
TreeSet treeSet2 = Sets.newTreeSet(Comparator.comparingInt(Object::hashCode));  // 构造一个具有给定的比较器可变TreeSet的实例
TreeSet treeSet3 = Sets.newTreeSet(new ArrayList<String>() {{  // 构造一个可变的包含给定元素的TreeSet实例
    add("a");
    add("b");
    add("a");
    add("c");
}});

treeSet1.forEach(System.out::print);  // 输出:
treeSet2.forEach(System.out::print);  // 输出:
treeSet3.forEach(System.out::print);  // 输出: abc
```
<hr>

### 4. 常用构造CopyOnWriteArraySet实例[newCopyOnWriteArraySet]
```java
CopyOnWriteArraySet copyOnWriteArraySet1 = Sets.newCopyOnWriteArraySet();  // 构造一个空的CopyOnWriteArraySet实例
CopyOnWriteArraySet copyOnWriteArraySet2 = Sets.newCopyOnWriteArraySet(new ArrayList<String>() {{  // 构造一个包含给定元素的CopyOnWriteArraySet实例
    add("a");
    add("b");
    add("a");
    add("c");
}});

copyOnWriteArraySet1.forEach(System.out::print);  // 输出:
copyOnWriteArraySet2.forEach(System.out::print);  // 输出: abc
```
<hr>

### 5. 常用构造LinkedHashSet实例[newLinkedHashSet]
```java
LinkedHashSet linkedHashSet1 = Sets.newLinkedHashSet();  // 构造一个可变的空的LinkedHashSet实例
LinkedHashSet linkedHashSet2 = Sets.newLinkedHashSet(new ArrayList<String>() {{  // 构造一个包含给定元素的LinkedHashSet实例
    add("a");
    add("b");
    add("a");
    add("c");
}});
LinkedHashSet linkedHashSet3 = Sets.newLinkedHashSetWithExpectedSize(30);  // 构造一个期望长度为expectedSize的LinkedHashSet实例

linkedHashSet1.forEach(System.out::print);  // 输出:
linkedHashSet2.forEach(System.out::print);  // 输出: abc
linkedHashSet3.forEach(System.out::print);  // 输出:
```
<hr>

### 6. 构造其它Set实例[*NavigableSet]
```java
// 返回一个同步的（线程安全的）NavigableSet，由指定的NavigableSet支持
NavigableSet navigableSet1 = Sets.synchronizedNavigableSet(new TreeSet<String>() {{  // TreeSet implements NavigableSet<E>
    add("a");
    add("b");
}});
// 返回指定NavigableSet的不可修改视图
NavigableSet navigableSet2 = Sets.unmodifiableNavigableSet(new TreeSet<String>() {{  // TreeSet implements NavigableSet<E>
    add("a");
    add("b");
}});
// 返回一个set，包含给定set的所有可能父级集合
Set set4 = Sets.powerSet(Sets.newHashSet("a", "b", "a", "c"));
// 返回set的部分视图，其元素包含在range中
NavigableSet navigableSet3 =
        Sets.subSet(new TreeSet<String>() {{  // TreeSet implements NavigableSet<E>
                        add("a");
                        add("b");
                        add("c");
                    }},
                Range.range("a", BoundType.OPEN, "b", BoundType.CLOSED));

navigableSet1.forEach(System.out::print);  // 输出: ab
navigableSet2.forEach(System.out::print);  // 输出: ab
set4.forEach(System.out::print);  // 输出: [][a][b][a, b][c][a, c][b, c][a, b, c]
navigableSet3.forEach(System.out::print);  // 输出: b
```
<hr>

### 7. 返回不可修改SetView[Sets.SetView]
```java
// 返回两个set集合的交集的不可修改SetView
Sets.SetView setView1 = Sets.intersection(
        Sets.newHashSet("a", "b", "a", "c"),
        Sets.newHashSet("a", "d", "c", "f")
);
// 返回两个set集合的差的不可修改SetView(参数顺序会影响结果)
Sets.SetView setView2 = Sets.difference(
        Sets.newHashSet("a", "d", "c", "f"),
        Sets.newHashSet("a", "b", "a", "c")
);
Sets.SetView setView3 = Sets.difference(
        Sets.newHashSet("a", "b", "a", "c"),
        Sets.newHashSet("a", "d", "c", "f")
);
// 返回两个set集合的对称差的不可修改SetView
Sets.SetView setView4 = Sets.symmetricDifference(
        Sets.newHashSet("a", "b", "a", "c"),
        Sets.newHashSet("a", "d", "c", "f")
);
// 返回两个set集合的并集的不可修改SetView
Sets.SetView setView5 = Sets.union(
        Sets.newHashSet("a", "b", "a", "c"),
        Sets.newHashSet("a", "d", "c", "f")
);

setView1.forEach(System.out::print);  // 输出: ac
setView2.forEach(System.out::print);  // 输出: df
setView3.forEach(System.out::print);  // 输出: b
setView4.forEach(System.out::print);  // 输出: bdf
setView5.forEach(System.out::print);  // 输出: abcdf
```
<hr>

### 8. 任意匹配出不同的Set[cartesianProduct]
```java
// 返回通过从各给定集中选择一个元素所形成每一个可能的集合
Set set5 = Sets.cartesianProduct(Lists.newArrayList(
        Sets.newHashSet("a", "b", "a", "c"),
        Sets.newHashSet("a", "d", "c", "f")
));
Set set6 = Sets.cartesianProduct(
        Sets.newHashSet("a", "b", "a", "c"),
        Sets.newHashSet("a", "d", "c", "f")
);

set5.forEach(System.out::print);  // 输出: [a, a][a, c][a, d][a, f][b, a][b, c][b, d][b, f][c, a][c, c][c, d][c, f]
set6.forEach(System.out::print);  // 输出: [a, a][a, c][a, d][a, f][b, a][b, c][b, d][b, f][c, a][c, c][c, d][c, f]

System.out.println();

// 返回大小为set的所有子集的集合
Set set7 = Sets.combinations(
        Sets.newHashSet("a", "b", "a", "c", "D"),
        3
);

set7.forEach(System.out::print);  // 输出: [a, b, c][a, b, D][a, c, D][b, c, D]
```
<hr>

### 9. 反向获得其余枚举值[complementOf]
```java
// 创建一个EnumSet包括不属于指定集合中的所有枚举值
EnumSet enumSet2 = Sets.complementOf(new ArrayList<SetEnum>() {{
    add(SetEnum.A);
    add(SetEnum.B);
}});
EnumSet enumSet3 = Sets.complementOf(new ArrayList<SetEnum>() {{
    add(SetEnum.A);
}}, SetEnum.class);

enumSet2.forEach(System.out::print);  // 输出: C
enumSet3.forEach(System.out::print);  // 输出: BC
```
<hr>

### 10. set元素过滤[filter]
```java
// 返回传入NavigableSet集合unfiltered中满足给定Predicate的元素集合NavigableSet
NavigableSet navigableSet4 =
        Sets.filter(new TreeSet<String>() {{  // TreeSet implements NavigableSet<E> extends SortedSet<E>
                        add("a");
                        add("b");
                        add("c");
                    }},
                x -> x.equals("b")
        );
Set set8 = Sets.filter(Sets.newHashSet("a", "b", "a", "c", ""), x -> x.equals("c"));

navigableSet4.forEach(System.out::print);  // 输出: b
set8.forEach(System.out::print);  // 输出: c
```
<hr>

### 11. 生成Collector[toImmutableEnumSet]
```java
// 返回一个收集器，它将输入元素累积到一个新的ImmutableSet中，并使用专门用于枚举的实现
Collector collector = Sets.toImmutableEnumSet();
```
<hr>

> 本文中使用的的Enum代码如下
```java
enum SetEnum {

    A("a"),
    B("b"),
    C("c");

    private String code;

    SetEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
```
