<h2> ThreadLocal, expungeStaleEntry方法详解</h2>
<br/>

> tips. 首先来说说为什么``` ThreadLocal ```会存在内存泄露的可能?<br/>

'ThreadLocalMap'是'Entry[]'去保存value,<br/>
而'Entry'继承了'WeakReference<ThreadLocal<?>>', 即Entry的key是弱引用.<br/>
当'ThreadLocal'没有外部强引用时, 那么会被GC回收, 然而该key对应的value却不会被回收.<br/>
若是当前线程一直不结束的话, 会存在一条强引用链, value也会一直累加导致内存泄露:<br/>
``` Thread ref -> Thread -> ThreadLocalMap -> Entry -> value ```

为了弥补'内存泄露',<br/>
那么就来看看'ThreadLocalMap'中的``` expungeStaleEntry() ```方法做了些什么吧.<br/>

```java
/**
 * Expunge a stale entry by rehashing any possibly colliding entries
 * lying between staleSlot and the next null slot.  This also expunges
 * any other stale entries encountered before the trailing null.  See
 * Knuth, Section 6.4
 */
private int expungeStaleEntry(int staleSlot) {
    Entry[] tab = table;
    int len = tab.length;

    // expunge entry at staleSlot
    tab[staleSlot].value = null;
    tab[staleSlot] = null;
    size--;

    // Rehash until we encounter null
    Entry e;
    int i;
    for (i = nextIndex(staleSlot, len);
            (e = tab[i]) != null;
            i = nextIndex(i, len)) {
        ThreadLocal<?> k = e.get();
        if (k == null) {
            e.value = null;
            tab[i] = null;
            size--;
        } else {
            int h = k.threadLocalHashCode & (len - 1);
            if (h != i) {
                tab[i] = null;

                // Unlike Knuth 6.4 Algorithm R, we must scan until
                // null because multiple entries could have been stale.
                while (tab[h] != null)
                    h = nextIndex(h, len);
                tab[h] = e;
            }
        }
    }
    return i;
}
```

我们将这个方法分成几个部分, 一个个来看到底做了些什么.<br/>

```java
Entry[] tab = table;
int len = tab.length;

// 这部分主要是拿到ThreadLocalMap的所有Entry[], 并且得到其length,
// 供方法中的其他地方使用.
```

```java
// expunge entry at staleSlot
tab[staleSlot].value = null;
tab[staleSlot] = null;
size--;

// 这部分主要就是将通过下标获得的entry, 断开强引用, 等待GC.
```

```java
for (i = nextIndex(staleSlot, len);
     (e = tab[i]) != null;
     i = nextIndex(i, len))

// 从这部分开始, 就属于'额外'的工作了, 毕竟需要清理的entry已经操作完了.
// 在这个循环中, 会不断累加i值, 且在tab[]不为null的情况下一直循环.
```

```java
if (k == null) {
    e.value = null;
    tab[i] = null;
    size--;
}

// 在for循环中, 当k(即ThreadLocal)为null时, 同样进行清理操作
```

注意, 下面就是'Rehash'的代码部分了
```java
else {
    int h = k.threadLocalHashCode & (len - 1);
    // ...略
}
```
若``` k(即ThreadLocal) ```不为null时,<br/>
首先会取得该``` k(即ThreadLocal) ```应该存在的下标``` h ```:<br/>
> 'ThreadLocalMap'的存储'k&v'的方式不是'**链表法**'而是'**开地址法**'<br/>
> 也就是``` k.threadLocalHashCode & (len - 1) ```可以获得该'Entry'的直接索引位置.<br/>

从Java源码中可知``` threadLocalHashCode ```是:<br/>
```java
private final int threadLocalHashCode = nextHashCode();
private static AtomicInteger nextHashCode = new AtomicInteger();

/**
 * The difference between successively generated hash codes - turns
 * implicit sequential thread-local IDs into near-optimally spread
 * multiplicative hash values for power-of-two-sized tables.
 */
// 这个数是Integer有符号整数的0.618倍, 既黄金比.
// 使用这个比例, 可以使key在数组上被更均匀的分散. 至于为什么...有兴趣的可以去深入了解下.
private static final int HASH_INCREMENT = 0x61c88647;

private static int nextHashCode() {
    return nextHashCode.getAndAdd(HASH_INCREMENT);
}
```

那么``` (len - 1) ```又是什么呢?<br/>
从Java源码中可以看到, table的大小必须是要2的N次幂.<br/>
```java
/**
 * The initial capacity -- MUST be a power of two.
 */
private static final int INITIAL_CAPACITY = 16;

/**
 * The table, resized as necessary.
 * table.length MUST always be a power of two.
 */
private Entry[] table;
```
``` (len - 1) ```其实就是为了取模运算.<br/>
例如: table长度是16, 那么(16 - 1)就是15, 其二进制就为1111.<br/>
<br/>
从以上的拆解, 可以知道``` k.threadLocalHashCode & (len - 1) ```<br/>
本质就是取模的方式去获得指定key的Entry[]下标.<br/>


让我们继续往下.<br/>
```java
if (h != i) {
    tab[i] = null;

    // Unlike Knuth 6.4 Algorithm R, we must scan until
    // null because multiple entries could have been stale.
    while (tab[h] != null)
        h = nextIndex(h, len);
    tab[h] = e;
}
```
若是以上的'取模运算结果'不等于当前的``` i ```,<br/>
那么就说明当前的``` i ```是'不对'的(通过key计算得出的Entry[]下标可能获取不到准确的value).<br/>
```java
// 这是ThreadLocalMap获取数据的代码, 也是通过取模运算得到的Entry下标
private Entry getEntry(ThreadLocal<?> key) {
    int i = key.threadLocalHashCode & (table.length - 1);
    Entry e = table[i];
    // ...略
}
```
当然也不能说``` i ```'不对', 因为这是一块'Rehash'的过程, 下文中会说明.<br/>

若是以上的'取模运算结果'不等于当前的``` i ```时,<br/>
Java源码中做了两件事,<br/>
将<br/>
```java
// 'i'下标的'entry'置为null
table[i] = null 
```
以及<br/>
``` java
// 遍历下标, 找出下一个为'null'的entry, 并将值赋予
while (tab[h] != null)
    h = nextIndex(h, len); 
tab[h] = e;
```
其实'Rehash'的过程, 就是重新梳理Entry中的数据.<br/>
这也是我上面说的, 仅仅说'取模运算结果'不等于当前的``` i ```,说明当前的``` i ```是不对的', 这个说法是有点偏颇的.<br/>
因为'i'本就不一定就是'key取模运算的结果'.<br/>
<br/>
这里有个需要注意的地方, 既然"'i'本就不一定就是'key取模运算的结果'",<br/>
那么怎么保证get value的时候, 我可以获得我当前'ThreadLocal'的value呢??<br/>
我们再看下get value的Java源码:<br/>
```java
/**
 * Get the entry associated with key.  This method
 * itself handles only the fast path: a direct hit of existing
 * key. It otherwise relays to getEntryAfterMiss.  This is
 * designed to maximize performance for direct hits, in part
 * by making this method readily inlinable.
 */
private Entry getEntry(ThreadLocal<?> key) {
    int i = key.threadLocalHashCode & (table.length - 1);
    Entry e = table[i];
    if (e != null && e.get() == key)
        return e;
    else
        return getEntryAfterMiss(key, i, e);
}
/**
 * Version of getEntry method for use when key is not found in
 * its direct hash slot.
 */
private Entry getEntryAfterMiss(ThreadLocal<?> key, int i, Entry e) {
    Entry[] tab = table;
    int len = tab.length;

    while (e != null) {
        ThreadLocal<?> k = e.get();
        if (k == key)
            return e;
        if (k == null)
            expungeStaleEntry(i);
        else
            i = nextIndex(i, len);
        e = tab[i];
    }
    return null;
}
```
从``` getEntryAfterMiss ```的取值逻辑不难看出,<br/>
当'通过key取模运算的结果'无法获得准确的value时候(``` e.get() != key ```),<br/>
其实也会通过while循环去遍历去获得该key(即ThreadLocal)的真实entry.<br/>
这和``` expungeStaleEntry ```赋值的逻辑如出一辙.<br/>
<br/>
ps. <br/>
'ThreadLocal'在多处地方如set、get、remove等地方直接或间接调用了``` expungeStaleEntry() ```.<br/>
但是还是建议在日常开发中, 能够使用完ThreadLocal后手动调用'remove()'方法.<br/>

<br/>

文章看到这, 可能会有有两个疑惑:<br/>
1. 为什么Entry的key不设置为强引用呢?<br/>
2. 为什么ThreadLocalMap不使用key-value的形式去存储呢？<br/>
<br/>

> 第一个问题: **为什么Entry的key不设置为强引用呢?**<br/>
>> 可以先假设key设置为了强引用, 当``` threadLocal = null ```实例释放后,<br/>
>> ``` threadLocal ```会有强引用指向``` threadLocalMap ```,<br/>
>> 而``` threadLocalMap.Entry ```会有强引用指向``` threadLocal ```,<br/>
>> GC会分析其一直处于可达状态, 没办法回收.<br/>


> 第二个问题: **为什么ThreadLocalMap不使用key-value的形式去存储呢?**<br/>
>> 若是使用了这种形式去实现, 其实质上会造成'节点的生命周期'与'线程'强绑定.<br/>
>> 首先要了解Map的key其实不是key变量本身, 其也只是一个内存地址,<br/>
>> 若使用``` threadLocal ```为key, ``` threadLocal = null ```后, 是不影响Map的key指向的内存.<br/>
>> 若是这种情况, GC也会认为其一直处于可达状态, 没办法回收,<br/>
>> 且'ThreadLocalMap'本身也无法知道到底哪个key是已经'无用'的``` threadLocal ```.<br/>

综上, 哪怕'强引用'可能不会出现内存泄露、'key-value'可能思路上会更简单点.<br/>
还是选择使用Entry[]这种方式其实是权衡过的,<br/>
因为这种方式, 使得'ThreadLocalMap'本身的垃圾清理提供了便利,<br/>
内存泄露等问题也可用'expungeStaleEntry()'等方法去补救.<br/>


<br/>
<br/>
希望这篇文章能够帮助到你更加了解这一块的内容,<br/>
如果文章有遗漏或错误, 欢迎给我提issues或者邮件.<br/>

