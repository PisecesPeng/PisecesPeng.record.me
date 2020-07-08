<h2> Guava Array & List 常用方法示例 </h2>

- [1. Guava Array](#1-guava-array)
  - [1.1 数组与List相互转换[asList、toArray]](#11-%E6%95%B0%E7%BB%84%E4%B8%8Elist%E7%9B%B8%E4%BA%92%E8%BD%AC%E6%8D%A2aslisttoarray)
  - [1.2 多个数组合并[concat]](#12-%E5%A4%9A%E4%B8%AA%E6%95%B0%E7%BB%84%E5%90%88%E5%B9%B6concat)
  - [1.3 最大最小值与是否包含[max、min、contains]](#13-%E6%9C%80%E5%A4%A7%E6%9C%80%E5%B0%8F%E5%80%BC%E4%B8%8E%E6%98%AF%E5%90%A6%E5%8C%85%E5%90%ABmaxmincontains)
  - [1.4 参数间插入字符[join]](#14-%E5%8F%82%E6%95%B0%E9%97%B4%E6%8F%92%E5%85%A5%E5%AD%97%E7%AC%A6join)
  - [1.5 匹配下标[indexOf、lastIndexOf]](#15-%E5%8C%B9%E9%85%8D%E4%B8%8B%E6%A0%87indexoflastindexof)
  - [1.7 拆分与逆拆分[toByteArray、fromByteArray]](#17-%E6%8B%86%E5%88%86%E4%B8%8E%E9%80%86%E6%8B%86%E5%88%86tobytearrayfrombytearray)
  - [1.8 比较[compare]](#18-%E6%AF%94%E8%BE%83compare)
- [2. Guava List](#2-guava-list)
  - [2.1 生成List[asList]](#21-%E7%94%9F%E6%88%90listaslist)
  - [2.2 不同的构造ArrayList[newArrayList]](#22-%E4%B8%8D%E5%90%8C%E7%9A%84%E6%9E%84%E9%80%A0arraylistnewarraylist)
  - [2.3 构造CopyOnWriteArrayList[newCopyOnWriteArrayList]](#23-%E6%9E%84%E9%80%A0copyonwritearraylistnewcopyonwritearraylist)
  - [2.4 构造newLinkedList[newLinkedList]](#24-%E6%9E%84%E9%80%A0newlinkedlistnewlinkedlist)
  - [2.5 分割字符串至List[charactersOf]](#25-%E5%88%86%E5%89%B2%E5%AD%97%E7%AC%A6%E4%B8%B2%E8%87%B3listcharactersof)
  - [2.6 计算笛卡尔乘积[cartesianProduct]](#26-%E8%AE%A1%E7%AE%97%E7%AC%9B%E5%8D%A1%E5%B0%94%E4%B9%98%E7%A7%AFcartesianproduct)
  - [2.7 指定大小分割List[partition]](#27-%E6%8C%87%E5%AE%9A%E5%A4%A7%E5%B0%8F%E5%88%86%E5%89%B2listpartition)
  - [2.8 反转[reverse]](#28-%E5%8F%8D%E8%BD%ACreverse)

<hr>

## 1. Guava Array

> 'Guava Array'常见方法使用

### 1.1 数组与List相互转换[asList、toArray]
```java
int[] ints1 = new int[]{1, 2, 3};
List<Integer> list1 = Ints.asList(ints1);  // 数组转List
int[] ints2 = Ints.toArray(list1);  // List转数组

Arrays.stream(ints1).forEach(System.out::print);  // 输出: 123
list1.stream().forEach(System.out::print);  // 输出: 123
Arrays.stream(ints2).forEach(System.out::print);  // 输出: 123
```
<hr>

### 1.2 多个数组合并[concat]
```java
int[] ints3 = Ints.concat(new int[]{1, 3, 5}, new int[]{2, 4, 6});  // 将多个数组合并为一个
Arrays.stream(ints3).forEach(System.out::print);  // 输出: 135246
```
<hr>

### 1.3 最大最小值与是否包含[max、min、contains]
```java
int[] ints3 = new int[]{1, 2, 3, 4, 5, 6};
int max = Ints.max(ints3);  // 取最大值
int min = Ints.min(ints3);  // 取最小值
System.out.print("Max: " + max + " , Min: " + min);  // 输出: Max: 6 , Min: 1
boolean bool1 = Ints.contains(ints3, 7);  // 数组是否包含某数值
System.out.println(bool1);  // 输出: false
```
<hr>

### 1.4 参数间插入字符[join]
```java
String str1 =  Ints.join("#", 1 ,2 ,3);  // 在参数之间插入字符串进行分割
System.out.println(str1);  // 输出: 1#2#3
```
<hr>

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
<hr>

### 1.7 拆分与逆拆分[toByteArray、fromByteArray]
```java
int value1 = 6;
byte[] bytes1 = Ints.toByteArray(value1);  // 将参数拆分为byte表示的数组
int value2 = Ints.fromByteArray(bytes1);  // toByteArray的逆过程
int value3 = Ints.fromBytes(bytes1[0], bytes1[1],bytes1[2], bytes1[3]);  // 返回int值的字节表示的是给定的4个字节

Bytes.asList(bytes1).stream().forEach(System.out::print);  // 输出: 0006
System.out.println("fromByteArray: " + value2);  // 输出: 6
System.out.println("fromBytes: " + value3);  // 输出: 6
```
<hr>

### 1.8 比较[compare]
```java
int res5 = Ints.compare(3, 6);  // 比较两个参数的大小, 返回-1、0、1
System.out.println("compare: " + res5);  // 输出: -1
```
<hr>

## 2. Guava List

> 'Guava List'常见方法使用

### 2.1 生成List[asList]
```java
String str1 = "thisisA.";
String[] strs1 = {"thisisB.", "thisisC."};

List<String> list1 = Lists.asList(str1, strs1);  // 返回一个不可变的List，其中包含指定的第一个元素和附加的元素数组组成，修改这个数组将反映到返回的List上
List<String> list2 = Lists.asList(str1, str1, strs1);  // 返回一个不可变的List，其中包含指定的第一个元素、第二个元素和附加的元素数组组成，修改这个数组将反映到返回的List上

list1.stream().forEach(System.out::print);  // 输出: thisisA.thisisB.thisisC.
list2.stream().forEach(System.out::print);  // 输出: thisisA.thisisA.thisisB.thisisC.
```
<hr>

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
<hr>

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
<hr>

### 2.4 构造newLinkedList[newLinkedList]
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
<hr>

### 2.5 分割字符串至List[charactersOf]
```java
ImmutableList list11 = Lists.charactersOf("thisisabc");  // 将传进来的String或者CharSequence分割为单个的字符，并存入到一个ImmutableList对象中返回
System.out.print(list11.size());  // 输出: 9
```
<hr>

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
<hr>

### 2.7 指定大小分割List[partition]
```java
List<List<String>> list13 = Lists.partition(new ArrayList<String>() {{  // 分割list，分割后每个list的元素个数为size
    add("c");
    add("b");
    add("a");
}}, 2);
list13.stream().forEach(System.out::print);  // 输出: [c, b][a]
```
<hr>

### 2.8 反转[reverse]
```java
List<String> list14 = Lists.reverse(new ArrayList<String>() {{  // 反转list的元素
    add("c");
    add("b");
    add("a");
}});
list14.stream().forEach(System.out::print);  // 输出: abc
```
<hr>