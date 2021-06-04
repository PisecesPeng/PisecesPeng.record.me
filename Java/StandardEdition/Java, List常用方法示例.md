## Java, List常用方法示例

- [Java, List常用方法示例](#java-list常用方法示例)
  - [1. 增加](#1-增加)
  - [2. 修改](#2-修改)
  - [3. 移除](#3-移除)
  - [4. 迭代](#4-迭代)
  - [5. 切分](#5-切分)
  - [6. 其他](#6-其他)

> List是一个按顺序查找、有序的集合,可以包含重复的元素且允许存储项为空,提供了按索引访问的方式.

---

### 1. 增加

```java
List<String> listAdd = new ArrayList();

listAdd.add("a");
listAdd.add(1,"b");
listAdd.addAll(Arrays.asList("c", "d", "e"));
listAdd.addAll(2, Arrays.asList("f", "g", "h"));

listAdd.forEach(System.out::print);  // 输出结果为 : abfghcde
```

---

### 2. 修改

```java
List<String> listSet = new ArrayList(){{
    add("a");
    add("b");
    add("c");
}};

listSet.set(2, "d");

listSet.forEach(System.out::print);  // 输出结果为 : abd
```

---

### 3. 移除

```java
List<String> listRemove = new ArrayList(){{
    add("a");
    add("b");
    add("c");
}};

listRemove.remove("a");
listRemove.forEach(System.out::print);  // 输出结果为 : bc
listRemove.remove(0);
listRemove.forEach(System.out::print);  // 输出结果为 : c
listRemove.removeAll(Arrays.asList("c"));
listRemove.forEach(System.out::print);  // 输出结果为 :
```

---

### 4. 迭代

```java
List<String> listIter = new ArrayList(){{
    add("a");
    add("b");
    add("c");
    add("d");
}};

// iterator()
Iterator<String> it1 = listIter.iterator();
while (it1.hasNext()) {
    String tmp = it1.next();
    if ("d".equals(tmp)) {
        it1.remove();
    }
    if ("b".equals(tmp)) {
        it1.remove();
    }
}
listIter.forEach(System.out::print);  // 输出结果为 : ac

// listIterator() ps.支持双向遍历
ListIterator<String> it2 = listIter.listIterator();

while (it2.hasNext()) {
    String tmp = it2.next();
    if ("a".equals(tmp)) {
        it2.set("ok");
    }
    //  add()后,内部会执行'lastRet = -1;'
    if ("a".equals(tmp)) {
        it2.add("b");
    }
    /*
        // 对同一元素的'set()'若是放'add()'后, 则报错.
        'ArrayList.class' source code:
        add()中:
            int lastRet = -1; // index of last element returned; -1 if no such
        set()中:
            if (lastRet < 0) throw new IllegalStateException();
    */
    // 错误示例
//    if ("a".equals(tmp)) {
//        it2.set("ok");
//    }
    if ("c".equals(tmp)) {
        it2.set("ok");
    }
}
listIter.forEach(System.out::print);  // 输出结果为 : okbok

// listIterator(index idx)
ListIterator<String> it3 = listIter.listIterator(2);
// 该ListIterator从index'2'开始迭代
while (it3.hasNext()) {
    String tmp = it3.next();
    // 会跳过'b'元素,故'b'元素不会被修改
    if ("b".equals(tmp)) {
        it3.set("ok");
    }
}
listIter.forEach(System.out::print);  // 输出结果为 : okbok
```

---

### 5. 切分

```java
List<String> listSpliter = new ArrayList(){{
    add("a");
    add("b");
    add("c");
    add("d");
    add("e");
}};

Spliterator<String> s1 = listSpliter.spliterator();
// 错误示例
// System.out.print("s1 : ");
// s1.forEachRemaining(System.out::print);
Spliterator<String> s2 = s1.trySplit();

/*
    不可以在接下来还需要trySplit()的情况下,立即使用forEachRemaining()去遍历
    不然的话,会出现NPE
    'ArrayList.class'source code:
    forEachRemaining()中:
        index = hi
    trySplit()中:
        int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
        return (lo >= mid) ? null : // divide range in half unless too small
            new ArrayListSpliterator<E>(list, lo, index = mid, expectedModCount);

*/
// forEachRemaining()内部会执行'index = hi'
System.out.print("s1 : ");
s1.forEachRemaining(System.out::print);  // 输出结果为 : s1 : cde
System.out.print("s2 : ");
s2.forEachRemaining(System.out::print);  // 输出结果为 : s2 : ab
```

---

### 6. 其他

```java
List<String> listFunction = new ArrayList(){{
    add("a");
    add("b");
    add("c");
    add("d");
    add("e");
    add("f");
}};

// List是否为空
System.out.println(listFunction.isEmpty());  // 输出结果为 : false

// List是否包含指定元素
System.out.println(listFunction.contains("b"));  // 输出结果为 : true
System.out.println(listFunction.containsAll(Arrays.asList("a", "d", "f")));  // 输出结果为 : true

// 截取List区间元素
// subList()返回的是内部类, 但对其操作后的结果会反应到原List中(注意元素下标的变化)
listFunction.subList(2, 5).forEach(System.out::print);  // 输出结果为 : cde

// 比较List间相同
// Arrays.asList()输出的List, 是不具备add、remove等方法的定长集合
System.out.println(listFunction.equals(Arrays.asList("a", "b", "c", "d", "e", "f")));  // 输出结果为 : true

// 取得指定元素下标
System.out.println(listFunction.lastIndexOf("f"));  // 输出结果为 : 5

// 取得List大小
System.out.println(listFunction.size());  // 输出结果为 : 6

// 指定需保留的元素
System.out.println(listFunction.retainAll(Arrays.asList("a", "c", "e")));  // 输出结果为 : true

// 修改List中所有元素为指定格式
listFunction.replaceAll(t -> t + "ok ");
listFunction.forEach(System.out::print);  // 输出结果为 : aok cok eok

// List转换为数组
Object[] objects = listFunction.toArray();
for (Object obj: objects) {
    System.out.print(obj);  // 输出结果为 : aok cok eok
}
String[] strings = listFunction.toArray(new String[listFunction.size()]);
for (String str: strings) {
    System.out.print(str);  // 输出结果为 : aok cok eok
}

// 清空List
listFunction.clear();
listFunction.forEach(System.out::print);  // 输出结果为 :
```

