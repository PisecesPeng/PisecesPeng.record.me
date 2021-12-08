## Java, 流Stream使用示例

- [Java, 流Stream使用示例](#java-流stream使用示例)
  - [1. 创建.of().generate().iterate()](#1-创建ofgenerateiterate)
  - [2. 限制/跳过.limit().skip()](#2-限制跳过limitskip)
  - [3. 过滤.filter()](#3-过滤filter)
  - [4. 去重.distinct()](#4-去重distinct)
  - [5. 映射.map()](#5-映射map)
  - [6. 窥视.peek()](#6-窥视peek)
  - [7. 排序.sort()](#7-排序sort)
  - [8. 最大值/最小值.max().min()](#8-最大值最小值maxmin)
  - [9. 匹配.match()](#9-匹配match)
  - [10.收集.collect()](#10收集collect)
  - [11. 集合.reduce()](#11-集合reduce)

> Stream有以下特点:  
> 1. 无存储.Stream不是一种数据结构,它是某种数据源的一个视图,数据源可以是数组、Java容器或I/O channel等.  
> 2. 函数式编程.对Stream的任何修改都不会修改背后的数据源.(比如过滤操作不会删除被过滤的元素,而是会产生一个不包含被过滤元素的新Stream)  
> 3. 惰性执行.Stream上的操作不会立即执行,只有等用户真正需要结果时才会执行.  
> 4. 可消费性.Stream只能被'消费'一次,一旦遍历过就会失效,若想要再次必须重新生成.  

---

### 1. 创建.of().generate().iterate()

```java
// 创建Stream of
Stream<String> stream = Stream.of("a", "b", "c", "d", "e");
stream.forEach(System.out::print);  // 输出结果为 : abcde

// 创建Stream generator
Stream<Double> doubleStream = Stream.generate(() -> Math.random()).limit(10);
doubleStream = Stream.generate(Math::random).limit(10);
doubleStream.forEach(System.out::print);  // 输出结果为 : 0.10008692896164717...

// 创建Stream iterate
Stream<Integer> integerStream = Stream.iterate(0, item -> item + 5).limit(10);
integerStream.forEach(System.out::print);  // 输出结果为 : 051015202530354045
```

---

### 2. 限制/跳过.limit().skip()

```java
// 限制 limit
Stream<String> stream = Stream.of("a", "b", "c", "d", "e");
stream.limit(3).forEach(System.out::print);  // 输出结果为 : abc
// 跳过 skip
stream = Stream.of("a", "b", "c", "d", "e");
stream.skip(3).forEach(System.out::print);  // 输出结果为 : de
```

---

### 3. 过滤.filter()

```java
Stream<String> stream = Stream.of("a", "bb", "", "", "e");
System.out.print(stream.filter(s -> s.isEmpty()).count());  // 输出结果为 : 2
```

---

### 4. 去重.distinct()

```java
Stream<String> stream = Stream.of("a", "a", "c", "d", "d");
stream.distinct().forEach(System.out::print);  // 输出结果为 : acd
```

---

### 5. 映射.map()

```java
Stream<Integer>  integerStream = Stream.of(1, 2, 3, 4, 5);
// map(输出每个数的平方)
integerStream.map(i -> i*i).forEach(System.out::print);  // 输出结果为 : 1491625
// integerStream.mapToInt(i -> i*i).forEach(System.out::println);
// integerStream.mapToLong(i -> i*i).forEach(System.out::println);
// integerStream.mapToDouble(i -> i*i).forEach(System.out::println);

// flatmap
Stream<List<Integer>> flatStream = Stream.of(Arrays.asList(1), Arrays.asList(2, 3), Arrays.asList(3, 5, 6));
//层级结构扁平化,将底层元素抽出来放到一起,最终输出都是直接的数字
System.out.println(flatStream.flatMap(childList -> childList.stream()).collect(Collectors.toList()));  // 输出结果为 : [1, 2, 3, 3, 5, 6]
```

---

### 6. 窥视.peek()

```java
Stream<String> stream = Stream.of("a", "b", "c", "d", "e");
System.out.println(stream.peek(v -> System.out.print(v)).map(String::toUpperCase).collect(Collectors.toList()));  // 输出结果为 : abcde[A, B, C, D, E]
```

---

### 7. 排序.sort()

```java
Stream<String> stream = Stream.of("a", "c", "e", "b", "d");
System.out.println(stream.sorted(Comparator.comparing(String::toString)).collect(Collectors.toList()));  // 输出结果为 : [a, b, c, d, e]
```

---

### 8. 最大值/最小值.max().min()

```java
Stream<String> maxStream = Stream.of("a", "b", "d", "e", "c");
System.out.println(maxStream.max(Comparator.comparing(String::toString)).get());  // 输出结果为 : e
Stream<Integer> minStream = Stream.of(6, 2, 1, 6, 8, 12);
System.out.println(minStream.min(Comparator.comparing(Integer::intValue)).get());  // 输出结果为 : 1
```

---

### 9. 匹配.match()

```java
Stream<String> stream = Stream.of("a", "c", "e", "b", "d");
// Stream中全部元素符合传入的Predicate,返回true
System.out.println(Boolean.valueOf(stream.allMatch(p -> "e".equals(p))));  // 输出结果为 : false

stream = Stream.of("a", "c", "e", "b", "d");
// Stream 中只要有一个元素符合传入的Predicate,返回true
System.out.println(Boolean.valueOf(stream.anyMatch(p -> "e".equals(p))));  // 输出结果为 : true

stream = Stream.of("a", "c", "e", "b", "d");
// Stream 中没有一个元素符合传入的Predicate,返回true
System.out.println(Boolean.valueOf(stream.noneMatch(p -> "e".equals(p))));  // 输出结果为 : false
```

---

### 10.收集.collect()

```java
// List
Stream<String> stream = Stream.of("a", "b", "c", "d", "e");
List<String> collectList = stream.collect(Collectors.toList());
// ArrayList<String> collectArrList = stream.collect(Collectors.toCollection(ArrayList::new));
collectList.forEach(System.out::print);  // 输出结果为 : abcde
```

```java
// Set
Stream<String> stream = Stream.of("a", "b", "c", "d", "e");
Set<String> collectSet = stream.collect(Collectors.toSet());
// HashSet<String> collectHashSet = stream.collect(Collectors.toCollection(HashSet::new));
collectSet.forEach(System.out::print);  // 输出结果为 : abcde
```

```java
// Demo Class for demo
class Demo {
    private String name;
    private Integer age;
    // 构造、set/get方法、toString略
}
```

```java
// Map
List<Demo> listDemo = new ArrayList(){{
    add(new Demo("a", 19));
    add(new Demo("b", 21));
    add(new Demo("c", 21));
}};

// Map生成
Map<String, Integer> collectMap = listDemo.stream().collect(Collectors.toMap(
        s -> s.getName(),  // 生成key
        s -> s.getAge()  // 生成value
));
// collectMap 结果为 : a:19 b:21 c:21 

// 默认将Demo的toString()作为key给出
// Map<Demo, Integer> collectMap = listDemo.stream().collect(Collectors.toMap(
//         Function.identity(),  // 生成key(sc : return t -> t;)
//         s -> s.getAge()  // 生成value
// ));

// 出现重复Key时, 设置新老Value的取舍逻辑
// Map<Integer, Demo> collectMap = listDemo.stream()
// 	.collect(
// 		Collectors.toMap(
//			Demo::getAge, (demo) -> demo,
//			(oldValue, newValue) -> newValue.getName().compareTo(oldValue.getName()) > 0 ? newValue : oldValue  // 新老Value的取舍逻辑
//		)
//	);

// 依据某个条件判断(是否满足),将其分成互补的两部分
Map<Boolean, List<Demo>> passingMap = listDemo.stream()
    			.collect(Collectors.partitioningBy(s -> s.getAge() >= 20));
// passingMap 结果为 : false, [Demo{name='a', age=19}] \n true, [Demo{name='b', age=21}, Demo{name='c', age=21}]

// 类似与SQL的group by,对属性进行分组,属性相同的元素会被对应到Map的同一个key上
Map<Integer, List<Demo>> byMap = listDemo.stream()
    				.collect(Collectors.groupingBy(Demo::getAge));
// byMap 结果为 : 19, [Demo{name='a', age=19}] \n 21, [Demo{name='b', age=21}, Demo{name='c', age=21}]

// 在取出相同'年龄'的基础上,输出时仅输出'名称'即可
// Map<Integer, List<String>> byMap = listDemo.stream().collect(Collectors.groupingBy(
//         Demo::getAge,
//         Collectors.mapping(Demo::getName, Collectors.toList())
// ));

// 分组数据汇总
List<Demo> listDemo = new ArrayList(){{
    add(new Demo("a", 19));
    add(new Demo("a", 20));
    add(new Demo("b", 21));
    add(new Demo("c", 21));
}};
Map<String, Integer> summingMap = listDemo.stream()
				.collect(Collectors.groupingBy(Demo::getName, Collectors.summingInt(Demo::getAge)));
// summingMap 结果为: {a=39, b=21, c=21}

Map<String, Optional<Demo>> maxMap = listDemo.stream()
				.collect(Collectors.groupingBy(Demo::getName, Collectors.maxBy(Comparator::getAge)));
// maxMap 结果为: {a=Demo{name='a', age=20}, b=Demo{name='b', age=21}, c=Demo{name='c', age=21}}

Map<String, Optional<Demo>> minMap = listDemo.stream()
				.collect(Collectors.groupingBy(Demo::getName, Collectors.minBy(Comparator::getAge)));
// minMap 结果为: {a=Demo{name='a', age=19}, b=Demo{name='b', age=21}, c=Demo{name='c', age=21}}

Map<String, Double> avgMap = list.stream()
                .collect(Collectors.groupingBy(Demo::getName, Collectors.averagingDouble(Demo::getAge)));
// avgMap 结果为: {a=19.5, b=21, c=21}
Double avgNum = list.stream()
                .collect(Collectors.collectingAndThen(Collectors.averagingDouble(Demo::getAge), Double::new));
// avgNum 结果为: 20.25
```


```java
Stream<String> stream = Stream.of("a", "b", "c", "d", "e");
// 字符串拼接
String joined = stream.collect(Collectors.joining(",", "{", "}"));
System.out.print(joined);  // 输出结果为 : {a,b,c,d,e}
```

---

### 11. 集合.reduce()

```java
Stream<String> stream = Stream.of("a", "b", "c", "d", "e");
// 追加
System.out.println(stream.reduce(String::concat).get());  // 输出结果为 : abcde

stream = Stream.of("aa", "bb", "ccc", "d", "eee");
// 求最长
System.out.println(stream.reduce((s1, s2) -> s1.length()>=s2.length() ? s1 : s2).get());  // 输出结果为 : ccc

stream = Stream.of("aa", "bb", "ccc", "d", "eee");
// 单词长度之和
System.out.println(stream.reduce(0, (sum, str) -> sum+str.length(), (a, b) -> a+b));  // 输出结果为 : 11
// stream.mapToInt(str -> str.length()).sum();
```

