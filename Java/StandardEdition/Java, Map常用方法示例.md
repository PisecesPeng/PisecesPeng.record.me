## Java, Map常用方法示例

- [Java, Map常用方法示例](#java-map常用方法示例)
  - [1. 添加](#1-添加)
  - [2. 获得](#2-获得)
  - [3. 计算](#3-计算)
  - [4. 替换](#4-替换)
  - [5. 遍历](#5-遍历)
  - [6. 比较](#6-比较)
  - [7. 移除](#7-移除)
  - [8. 其他](#8-其他)

### 1. 添加
```java
// 添加/修改
map.put("a", "a");
System.out.println(map);  // {a=a}

// 是否存在? 不存在则进行操作, 存在则不操作
map.putIfAbsent("a", "b");
System.out.println(map);  // {a=a}
map.putIfAbsent("b", "b");
System.out.println(map);  // {a=a, b=b}

// 添加整个map
map.putAll(new HashMap<String, String>() {{
    put("c", "c");
}});
System.out.println(map);  // {a=a, b=b, c=c}
```

---

### 2. 获得
```java
System.out.println(map);  // 先初始化一个 {a=a} 的map

// 获得
System.out.println(map.get("a"));  // a

// 获得, 若不存在给默认值
System.out.println(map.getOrDefault("a", "b"));  // a
System.out.println(map.getOrDefault("b", "c"));  // c
```

---

### 3. 计算
```java
System.out.println(map);  // 先初始化一个 {a=a} 的map

// 计算value值
map.compute("a", (k, v) -> v.concat("aa"));
System.out.println(map);  // {a=aaa}
map.compute("b", (k, v) -> k.concat("bb"));
System.out.println(map);  // {a=aaa, b=bbb}

// 当key不存在时, 计算
map.computeIfAbsent("c", x -> x.concat("cc"));
System.out.println(map);  // {a=aaa, b=bbb, c=ccc}

// 当key存在时, 计算
map.computeIfPresent("c", (k, v) -> v.concat("cc"));
System.out.println(map);  // {a=aaa, b=bbb, c=ccccc}

// 当key不存在赋予第二个参数, 当key存在则赋予第三个参数
map.merge("d", "d", (k, v) -> k.concat(v));
System.out.println(map);  // {a=aaa, b=bbb, c=ccccc, d=d}
```

---

### 4. 替换
```java
System.out.println(map);  // 先初始化一个 {a=a} 的map

// 替换
map.replace("a", "aaa");
System.out.println(map);  // {a=aaa}
// 当旧值等于第二个参数时, 赋予第三个参数
map.replace("a", "aaa", "a");
System.out.println(map);  // {a=a}
// 全局替换
map.replaceAll((k, v) -> k.concat(v));
System.out.println(map);  // {a=aa}
```

---

### 5. 遍历
```java
System.out.println(map);  // 先初始化一个 {key1=value1, key2=value2, key3=value3} 的map

// 遍历key
map.keySet().forEach(k -> System.out.print(k + " "));  // key1 key2 key3 
// 遍历value
map.values().forEach(v -> System.out.print(v + " "));  // value1 value2 value3
// 遍历kv
map.entrySet().forEach(k -> System.out.print(k.getKey() + ":" + k.getValue() + " "));  // key1:value1 key2:value2 key3:value3 
// 遍历kv
map.forEach((k, v) -> System.out.print(k + ":" + v + " "));  // key1:value1 key2:value2 key3:value3
```

---

### 6. 比较
```java
System.out.println(map);  // 先初始化一个 {a=a, b=b} 的map

// 是否存在key
System.out.println(map.containsKey("a"));  // true

// 比较map
System.out.println(map.equals(new HashMap<String, String>(){{
    put("a", "a");
    put("b", "b");
}}));  // true
```

---

### 7. 移除
```java
System.out.println(map);  // 先初始化一个 {a=a, b=b} 的map

// 移除key&value匹配的元素
System.out.println(map.remove("a","b"));  // false
System.out.println(map);  // {a=a, b=b}

// 移除key匹配的元素
System.out.println(map.remove("c"));  // null
System.out.println(map);  // {a=a, b=b}
System.out.println(map.remove("a"));  // a
System.out.println(map);  // {b=b}

// 清空
map.clear();
System.out.println(map);  // {}
```

---

### 8. 其他
```java
System.out.println(map);  // 先初始化一个 {a=a, b=b} 的map

// map大小
System.out.println(map.size());  // 2

// map是否为空
System.out.println(map.isEmpty());  // false

// hashcode
System.out.println(map.hashCode());  // 0
```

