## Guava Array & Lists 常用方法示例

- [Guava Array & Lists 常用方法示例](#guava-array--lists-常用方法示例)
- [1. Guava Array 常见方法使用](#1-guava-array-常见方法使用)
  - [1.1 数组与List相互转换[asList、toArray]](#11-数组与list相互转换aslisttoarray)
  - [1.2 多个数组合并[concat]](#12-多个数组合并concat)
  - [1.3 最大最小值与是否包含[max、min、contains]](#13-最大最小值与是否包含maxmincontains)
  - [1.4 参数间插入字符[join]](#14-参数间插入字符join)
  - [1.5 匹配下标[indexOf、lastIndexOf]](#15-匹配下标indexoflastindexof)
  - [1.6 拆分与逆拆分[toByteArray、fromByteArray]](#16-拆分与逆拆分tobytearrayfrombytearray)
  - [1.7 比较[compare]](#17-比较compare)
- [2. Guava List 常见方法使用](#2-guava-list-常见方法使用)
  - [2.1 生成List[asList]](#21-生成listaslist)
  - [2.2 不同的构造ArrayList[newArrayList]](#22-不同的构造arraylistnewarraylist)
  - [2.3 构造CopyOnWriteArrayList[newCopyOnWriteArrayList]](#23-构造copyonwritearraylistnewcopyonwritearraylist)
  - [2.4 构造LinkedList[newLinkedList]](#24-构造linkedlistnewlinkedlist)
  - [2.5 分割字符串至List[charactersOf]](#25-分割字符串至listcharactersof)
  - [2.6 计算笛卡尔乘积[cartesianProduct]](#26-计算笛卡尔乘积cartesianproduct)
  - [2.7 指定大小分割List[partition]](#27-指定大小分割listpartition)
  - [2.8 反转[reverse]](#28-反转reverse)

## 1. Guava Array 常见方法使用

> 注意, 本文仅展示`Ints`的部分方法,  
> Guava还提供了很多诸如: `Longs`、`Doubles`、`Booleans`等...基础类操作类

### 1.1 数组与List相互转换[asList、toArray]

```java
int[] ints1 = new int[]{1, 2, 3};
List<Integer> list1 = Ints.asList(ints1);  // 数组转List
int[] ints2 = Ints.toArray(list1);  // List转数组

Arrays.stream(ints1).forEach(System.out::print);  // 输出: 123
list1.stream().forEach(System.out::print);  // 输出: 123
Arrays.stream(ints2).forEach(System.out::print);  // 输出: 123
```

---

### 1.2 多个数组合并[concat]

```java
int[] ints3 = Ints.concat(new int[]{1, 3, 5}, new int[]{2, 4, 6});  // 将多个数组合并为一个
Arrays.stream(ints3).forEach(System.out::print);  // 输出: 135246
```

---

### 1.3 最大最小值与是否包含[max、min、contains]

```java
int[] ints3 = new int[]{1, 2, 3, 4, 5, 6};
int max = Ints.max(ints3);  // 取最大值
int min = Ints.min(ints3);  // 取最小值
System.out.print("Max: " + max + " , Min: " + min);  // 输出: Max: 6 , Min: 1
boolean bool1 = Ints.contains(ints3, 7);  // 数组是否包含某数值
System.out.println(bool1);  // 输出: false
```

---

### 1.4 参数间插入字符[join]

```java
String str1 =  Ints.join("#", 1 ,2 ,3);  // 在参数之间插入字符串进行分割
System.out.println(str1);  // 输出: 1#2#3
```

---

### 1.5 匹配下标[indexOf、lastIndexOf]

```java
int[] ints4 = new int[]{1, 3, 5, 7, 9, 7};
int[] ints5 = new int[]{7, 9, 7};
int target1 = 10;
int target2 = 7;

int res1 = Ints.indexOf(ints4, target1);  // 返回首次匹配的下标位置, 不存在返回-1
int res2 = Ints.indexOf(ints4, ints5);  // 返回首次匹配的下标位置, 不存在返回-1
int res3 = Ints.lastIndexOf(ints5, target2);  // 反向indexOf()

System.out.println("indexof: " + res1);  // 输出: -1
System.out.println("indexof: " + res2);  // 输出: 3
System.out.println("lastindexof: " + res3);  // 输出: 2
```

---

### 1.6 拆分与逆拆分[toByteArray、fromByteArray]

```java
int value1 = 6;
byte[] bytes1 = Ints.toByteArray(value1);  // 将参数拆分为byte表示的数组
int value2 = Ints.fromByteArray(bytes1);  // toByteArray的逆过程
int value3 = Ints.fromBytes(bytes1[0], bytes1[1],bytes1[2], bytes1[3]);  // 返回int值的字节表示的是给定的4个字节

Bytes.asList(bytes1).stream().forEach(System.out::print);  // 输出: 0006
System.out.println("fromByteArray: " + value2);  // 输出: 6
System.out.println("fromBytes: " + value3);  // 输出: 6
```

---

### 1.7 比较[compare]

```java
int res5 = Ints.compare(3, 6);  // 比较两个参数的大小, 返回-1、0、1
System.out.println("compare: " + res5);  // 输出: -1
```

---

## 2. Guava List 常见方法使用

### 2.1 生成List[asList]

```java
String str1 = "thisisA.";
String[] strs1 = {"thisisB.", "thisisC."};

List<String> list1 = Lists.asList(str1, strs1);  // 返回一个不可变的List，其中包含指定的第一个元素和附加的元素数组组成，修改这个数组将反映到返回的List上
List<String> list2 = Lists.asList(str1, str1, strs1);  // 返回一个不可变的List，其中包含指定的第一个元素、第二个元素和附加的元素数组组成，修改这个数组将反映到返回的List上

list1.stream().forEach(System.out::print);  // 输出: thisisA.thisisB.thisisC.
list2.stream().forEach(System.out::print);  // 输出: thisisA.thisisA.thisisB.thisisC.
```

---

### 2.2 不同的构造ArrayList[newArrayList]

```java
List<String> list3 = Lists.newArrayList();  // 构造一个可变的、空的ArrayList实例
list3.stream().forEach(System.out::print);  // 输出:

List<String> list4 = Lists.newArrayList("a", "b", "c", "d");  // 构造一个可变的、包含元素的ArrayList实例
list4.stream().forEach(System.out::print);  // 输出: abcd

List<String> list5 = Lists.newArrayList(new ArrayList<String>() {{  // 构造一个可变的、包含元素的ArrayList实例
    add("a");
    add("b");
    add("c");
}});
list5.stream().forEach(System.out::print);  // 输出: abc

List<String> list6 = Lists.newArrayListWithCapacity(30);  // 构造一个分配指定空间大小的ArrayList实例
list6.stream().forEach(System.out::print);  // 输出:
```

---

### 2.3 构造CopyOnWriteArrayList[newCopyOnWriteArrayList]

```java
CopyOnWriteArrayList list7 = Lists.newCopyOnWriteArrayList();  // 构造一个空的CopyOnWriteArrayList实例
list7.stream().forEach(System.out::print);  // 输出:

CopyOnWriteArrayList list8 = Lists.newCopyOnWriteArrayList(new ArrayList<String>() {{  // 构造一个包含元素的CopyOnWriteArrayList实例
    add("a");
    add("b");
    add("c");
}});
list8.stream().forEach(System.out::print);  // 输出: abc
```

---

### 2.4 构造LinkedList[newLinkedList]

```java
LinkedList list9 = Lists.newLinkedList();  // 构造一个空的LinkedList实例
list9.stream().forEach(System.out::print);  // 输出:

LinkedList list10 = Lists.newLinkedList(new ArrayList<String>() {{  // 构造一个包含元素的LinkedList实例
    add("a");
    add("b");
    add("c");
}});
list10.stream().forEach(System.out::print);  // 输出: abc
```

---

### 2.5 分割字符串至List[charactersOf]

```java
ImmutableList list11 = Lists.charactersOf("thisisabc");  // 将传进来的String或者CharSequence分割为单个的字符，并存入到一个ImmutableList对象中返回
System.out.print(list11.size());  // 输出: 9
```

---

### 2.6 计算笛卡尔乘积[cartesianProduct]

```java
List<List<String>> list12 = Lists.cartesianProduct(
        new ArrayList<String>() {{  // 计算多个list笛卡尔乘积
            add("a");
            add("b");
            add("c");
        }},
        new ArrayList<String>() {{  // 计算多个list笛卡尔乘积
            add("c");
            add("b");
            add("a");
        }}
);
list12.stream().forEach(System.out::print);  // 输出: [a, c][a, b][a, a][b, c][b, b][b, a][c, c][c, b][c, a]
```

---

### 2.7 指定大小分割List[partition]

```java
List<List<String>> list13 = Lists.partition(new ArrayList<String>() {{  // 分割list，分割后每个list的元素个数为size
    add("c");
    add("b");
    add("a");
}}, 2);
list13.stream().forEach(System.out::print);  // 输出: [c, b][a]
```

---

### 2.8 反转[reverse]

```java
List<String> list14 = Lists.reverse(new ArrayList<String>() {{  // 反转list的元素
    add("c");
    add("b");
    add("a");
}});
list14.stream().forEach(System.out::print);  // 输出: abc
```

---

