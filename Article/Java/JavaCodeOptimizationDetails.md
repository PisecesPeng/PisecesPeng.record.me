<h2> 提高Java代码运行效率的35个小细节 </h2>

> 性能优化大部分都是在时间、效率、代码结构层次等方面的权衡,<br/>
> 以下优化是比较浅鲜的,仅供参考,开发中需根据实际场景,敲出最合适的代码.<br/>
<hr>

### 1. 尽量在合适的场合使用单例

使用**单例**可以减轻加载的负担,缩短加载的时间,提高加载的效率,<br/>
但并不是所有地方都适用于单例,简单来说,单例主要适用于以下三个方面:<br/>

> 1. 控制资源的使用,通过线程同步来控制资源的并发访问;
> 2. 控制实例的产生,以达到节约资源的目的;
> 3. 控制数据共享,在不建立直接关联的条件下,让多个不相关的进程或线程之间实现通信.

### 2. 尽量避免随意使用静态变量

若某个对象被定义为**static变量**所引用,那么GC通常是不会回收这个对象所占有的内存,例如<br/>
```java
public class A{
    private static B b = new B();
}
```
``` ps. 此时静态变量b的生命周期与A类同步,如果A类不会卸载,那么b对象会常驻内存,直到程序终止. ```

### 3. 尽量避免过多过常地创建Java对象/尽量重用对象

尽量避免在经常调用的方法,循环中new对象,以及String对象的使用等等,<br/>
由于Java虚拟机不仅要花时间生成对象,而且还需要花时间对这些对象进行垃圾回收和处理,<br/>
因此在我们可以控制的范围内,最大限度地重用对象,或用基本的数据类型或数组来替代对象.<br/>
```
ps.
Hotspot JVM为了提升对象内存的分配效率,会为所创建的线程分配一块独立空间TLAB(仅作用于新生代),
这使得通常对象分配中,小的对象要比大的对象分配起来更高效.
```
详见JVM方面的知识[JVM结构的简单梳理](https://github.com/PisecesPeng/PisecesPeng.record.me/blob/master/Article/Java/JVMStructure.md).

### 4. 尽量使用final修饰符

带有final修饰符的类是不可派生的.在JAVA核心API中,有许多应用final的例子,<br/>
例如比较常见的java.lang.String,整个类都是final的.<br/>
为类指定final修饰符可以让类不可以被继承,为方法指定final修饰符可以让方法不可以被重写.<br/>
另外,如果一个类是final的,则该类所有方法都是final的.<br/>
**Java编译器**会寻找机会**内联**(inline)所有的final方法,内联对于提升Java运行效率作用重大.<br/>

### 5. 尽量使用局部变量

调用方法时传递的参数以及在调用中创建的临时变量都保存在**虚拟机栈**中速度较快,<br/>
而其他变量,如静态变量、实例变量等,都在堆中创建,访问速度较慢.<br/>
还有就是**虚拟机栈**中创建的变量,随着方法的运行结束而结束,不需要额外的垃圾回收.<br/>

### 6. 尽量处理好包装类型和基本类型两者的使用场所

虽然**包装类型**和**基本类型**在使用过程中是可以相互转换,<br/>
**自动拆箱**和**自动装箱**模糊了但并没有完全抹去这两种类型之间的区别,<br/>
而且它们两者所产生的内存区域也是不同的.<br/>
基本类型数据产生和处理都在**虚拟机栈**中处理,包装类型是对象,是在**堆**中产生实例.<br/>
在集合类对象,有对象方面需要的处理适用包装类型,其他的处理提倡使用基本类型.<br/>
``` ps. 基础类型为局部变量时,必须显式赋值,否则编译不通过.而且包装类型可以表示null值 ```

### 7. 慎用synchronized,尽量减小synchronize的作用域

实现同步是要系统开销作为代价的,甚至可能造成一些其它问题,所以尽量避免无谓的同步控制.<br/>
**synchronize**方法被调用时,直接会把当前对象锁了,在方法执行完之前其他线程无法调用当前对象的其他方法.<br/>
所以,synchronize的方法尽量少用,应尽量使用**方法同步**代替**代码块同步**.<br/>
而且synchronized作用域越大,则代表着其效率越低,甚至还会丧失并发的优势,<br/>
所以,若是必须使用synchronized的场景,<br/>
也尽可能使用同步代码块,避免对那些不需要进行同步的代码也进行了同步,影响了代码执行效率.<br/>

### 8. 尽量不要使用finalize方法/cleaner方法

**finalize方法**通常是不可预测、危险的,<br/>
一般使用此方法会导致行为不稳定、性能降低、以及可移植性的问题,<br/>
将资源清理放在finalize方法中完成是非常不好的选择,应该避免使用此方法.<br/>
同样还有**cleaner方法**(Java9),虽然其没有finalize方法这么危险,<br/>
但依旧是不可预测、运行缓慢的,所以一般情况下也是没有必要的.<br/>

### 9. 尽量使用基本数据类型代替对象

``` String str = new String("hello"); ```<br/>
上面这种方式会创建一个“hello”字符串,而且JVM的字符缓存池还会缓存这个字符串;<br/>
str所引用的String对象底层还包含一个char[]数组,这个char[]数组依次存放了h,e,l,l,o.<br/>

### 10. 多线程在未发生线程安全前提下应尽量使用HashMap等类

HashTable、Vector、StringBuffer等类使用了同步机制,降低了性能.<br/>

### 11. 尽量合理的创建HashMap等

当你要创建一个比较大的hashMap时,充分利用这个构造函数<br/>
``` public HashMap(int initialCapacity, float loadFactor); ```<br/>
避免**HashMap**多次进行了hash重构,扩容是一件很耗费性能的事,<br/>
需要多大的容量,最好能估计所需要的最佳大小.<br/>
同样的**Hashtable**,**Vectors**也是一样的道理.<br/>

### 12. 减少对变量的重复计算/避免不必要的创建

开发中,对方法的调用,即使方法中只有一句代码,也是有消耗的,<br/>
包括**创建栈帧**、调用方法时**保护现场**、调用方法完毕时**恢复现场**等.<br/>
并且在循环中应该**避免使用复杂的表达式**,在循环中,循环条件会被反复计算,<br/>
如果不使用复杂表达式,而使循环条件值不变的话,程序将会运行的更快.<br/>
所以例如下面的操作:<br/>
``` for (int i = 0; i < list.size(); i++) ```<br/>
建议替换为:<br/>
``` for (int i = 0, int length = list.size(); i < length; i++) ```<br/>
这样,在list.size()很大的时候,就减少了很多的消耗.<br/>
```java
for (int i = 1; i <= count; i++) {
    Object obj = new Object();
}
```
这种做法会导致内存中有count份Object对象引用存在,count很大的话,就耗费内存了,<br/>
建议替换为:<br/>
```java
Object obj = null;
for (int i = 0; i <= count; i++) {
    obj = new Object();
}
```
内存中只有一份Object对象引用,每次new Object()的时候,Object对象引用指向不同的Object罢了,<br/>
但是内存中只有一份,这样就大大节省了内存空间了.<br/>

### 13. 尽量在finally块中做一些必要处理

程序中使用到的资源应当被释放,以避免资源泄漏,这最好在finally块中去做.<br/>
不管程序执行的结果如何,finally块总是会执行的,以确保资源的正确关闭.<br/>

### 14. 尽量使用try-with-resources去关闭流

Java编程过程中,进行数据库连接、I/O流操作时务必小心,在使用完毕后,及时关闭以释放资源.<br/>
因为对这些大对象的操作会造成系统大的开销,稍有不慎,将会导致严重的后果<br/>
若是必须使用finally去关闭流,则建议将各资源close()分开操作,如:<br/>
``` try { xxx.close(); } catch (Exception e) { ... } try { yyy.close(); } catch (Exception e) { ... } ```<br/>
虽然有些麻烦,却能避免资源泄露.<br/>
万一xxx.close()抛异常了,那么就进入了cath块中了,yyy.close()不会执行,yyy这块资源就不会回收了,一直占用着,<br/>
这样的代码一多,是可能引起资源句柄泄露的.<br/>
而改为上面的写法之后,就保证了无论如何xxx和yyy都会被close.<br/>

### 15. 尽量使用移位来代替乘法和除法操作

使用**移位**的操作将会更快和更有效<br/>
如:<br/>
```java
int num = a * 8;
int num = a / 8;
```
建议改为:<br/>
```java
int num = a << 3;
int num = a >> 3;
```
移位操作虽然快,但是不直观,可能会使代码不太好理解,因此最好加上相应的注释.<br/>

### 16. 尽量早释放无用对象的引用

大部分时,方法**局部变量**所引用的对象会随着方法结束而变成垃圾,<br/>
因此,大部分时候,程序无需将局部引用变量显式设为null.<br/>
例如:
```Java
Public void test(){
    Object obj = new Object();
    // Doing something
    Obj=null;
}
```
上面这个就没必要了,随着方法test()的执行完成,程序中obj引用变量的作用域就结束了.但是如果是改成下面:<br/>
```Java
Public void test(){
    Object obj = new Object();
    // Doing something
    Obj=null;
    // Doing something
}
```
这时候就有必要将obj赋值为null,可以尽早的释放对Object对象的引用.<br/>

### 17.  尽量避免使用二维数组

二维数据占用的内存空间比一维数组多得多.<br/>

### 18. ArrayList & LinkedList

一个是**线性表**,一个是**链表**,<br/>
顺序插入和随机访问比较多的场景使用ArrayList,ArrayList优于LinkedList,LinkedList还要移动指针,<br/>
元素删除和中间插入比较多的场景使用LinkedList,ArrayList还要移动数据,<br/>
不过最好还是去理解好两者的数据结构,根据具体场景,具体分析.<br/>

### 19. 尽量使用System.arraycopy()代替通过来循环复制数组

System.arraycopy()比较贴近底层,其使用的是**内存复制**,省去了大量的数组寻址访问等时间,故效率较高.<br/>

### 20. 尽量缓存经常使用的对象

尽可能将经常使用的对象进行**缓存**,可以使用数组、HashMap等容器来进行缓存,<br/>
但这种方式可能导致系统占用过多的缓存,性能下降,<br/>
可以了解一些第三方的缓存操作开源工具.<br/>

### 21. 慎用异常

异常对性能不利.抛出异常首先要创建一个新的对象,<br/>
**Throwable接口**的构造函数调用名为fillInStackTrace()的本地同步方法,其方法检查堆栈,收集调用跟踪信息,<br/>
虚拟机栈跟踪不只包含运行时栈中的一两个元素,而是包含这个栈中的每一个元素.<br/>
从技术上讲,你甚至可以随意地抛出异常,而不用花费很大的代价.<br/>
招致性能损失的并不是throw操作,真正要花代价的是创建异常.<br/>
异常是为异常的情况而设计的,使用时也应该牢记这一原则,不应该用来控制程序流程.<br/>

### 22. 注意初始化变量

默认情况下,调用类的**构造函数**时,java会把变量初始化成确定的值,<br/>
所有的对象被设置成null,整数变量设置成0,float和double变量设置成0.0,boolean设置成false.<br/>
当一个类从另一个类**派生**时,这一点尤其应该注意,因为用new关键字创建一个对象时,构造函数链中的所有构造函数都会被自动调用.<br/>
这里有个注意,给成员变量设置初始值但需要调用其他方法的时候,最好放在一个方法中,<br/>
因为直接调用某方法赋值可能会因为类尚未初始化而抛空指针异常,如:<br/>
``` public int state = this.getState() ```

### 23. 使用数据库连接池和线程池

这两个池都是用于**重用对象**的,前者可以避免频繁地打开和关闭连接,后者可以避免频繁地创建和销毁线程.<br/>

### 24. 不要在循环中使用try/catch语句,应把其放在循环最外层

这个就不多说了..<br/>

### 25. 通过StringBuffer的构造函数来设定它的初始化容量

StringBuffer的默认容量为16,当StringBuffer的容量达到最大容量时,它会将自身容量增加到当前的2*n+2.<br/>
无论何时,只要StringBuffer到达它的最大容量,它就会创建一个**新的对象数组**,然后复制旧的对象数组,这会浪费很多时间.<br/>
所以给StringBuffer设置一个合理的初始化容量值,是很有必要的.<br/>

### 26.  不用new关键字创建对象的实例

用new关键词创建类的实例时,构造函数链中的所有构造函数都会被自动调用.<br/>
但如果一个对象实现了Cloneable接口,我们可以调用它的clone()方法,其不会调用任何类**构造函数**.<br/>

下面是Factory模式的一个典型实现:<br/>
```java
public static Credit getNewCredit() {
    return new Credit();
}
```
改进后的代码使用clone()方法:<br/>
```java
private static Credit BaseCredit = new Credit();
public static Credit getNewCredit() {
    return (Credit) BaseCredit.clone();
}
```

### 27. 不要将数组声明为public static final

这毫无意义,定义了引用为static final,数组的内容还是可以随意改变的,<br/>
将数组声明为public更是一个安全漏洞,这意味着这个数组可以被外部类所改变.<br/>

### 28. 使用最有效率的方式去遍历Map

遍历Map的方式有很多,通常场景下我们需要的是遍历Map中的Key和Value,那么推荐使用的、效率最高的方式是:<br/>
```java
public static void main(String[] args) {
    HashMap<String, String> hm = new HashMap<String, String>(){{
        put("a", "111");
        put("b", "222");
    }};
    Set<Map.Entry<String, String>> entrySet = hm.entrySet();
    Iterator<Map.Entry<String, String>> iter = entrySet.iterator(); 
    while (iter.hasNext()) {
        Map.Entry<String, String> entry = iter.next();
        System.out.println(entry.getKey() + "\t" + entry.getValue());
    }
}
```
如果你只是想遍历一下这个Map的key值,那用``` Set keySet = hm.keySet(); ```会比较合适一些<br/>

### 29. 数组和ArrayList的使用

**array**(数组)效率最高,但容量固定,无法动态改变,<br/>
**ArrayList**容量可以动态增长,但牺牲了效率.<br/>

### 30. 将常量声明为static final,并以大写命名

这样在编译期间就可以把这些内容放入常量池中,避免运行期间计算生成常量的值.<br/>
另外,将常量的名字以大写命名也可以方便区分出常量与变量.<br/>

### 30. 程序运行过程中避免使用反射

反射是Java提供给用户一个很强大的功能,但效率不高.<br/>
不建议在程序运行过程中使用尤其是频繁使用**反射机制**,特别是Method的invoke方法,<br/>
如果必须的话,<br/>
有种建议性就是将可以将需要通过反射加载的类在**项目启动**的时候通过反射**实例化**出一个对象并放入内存,<br/>
用户只关心和对端交互的时候获取最快的响应速度,并不关心对端的项目启动花多久时间.<br/>

### 31. 使用带缓冲的输入输出流进行IO操作

带缓冲的输入输出流,即BufferedReader/Writer、BufferedInput/OutputStream,这可以极大地提升IO效率.<br/>

### 32. 字符串变量和字符串常量equals的时候将字符串常量写在前面

这是一个比较常见的小技巧,如下:<br/>
```java
String str = "123";
if (str.equals("123")) { }
```
建议修改为:<br/>
```java
String str = "123";
if ("123".equals(str)) { }
```
这么做主要是可以避免空指针异常.<br/>

### 33. 不要对数组使用toString()方法

数组使用toString()打印出来的是什么:<br/>
```java 
[I@18a992f
```
本是想打印出数组内容,却输出类似以上的返回(也可能会存在因为**数组引用为空**而导致空指针异常).<br/>
不过虽然对数组toString()没有意义,但是对集合toString()是可以打印出集合里面的内容的,<br/>
因为集合的父类AbstractCollections重写了Object的toString()方法.<br/>

### 34. 公用的集合类中不使用的数据一定要及时remove

如果一个集合类是公用的,那么这个集合里面的元素是不会自动释放的,因为始终有引用指向它们.<br/>
所以,如果公用集合中的某些数据不使用而不去remove它们,<br/>
那么将会造成这个公用集合不断增大,使得系统有**内存泄露**的隐患.<br/>

### 35. 不要创建一些不使用的对象,不要导入一些不使用的类

<br/>
<br/>
<br/>
<br/>
<br/>

感谢以下文章给了我写作思路:<br/>
https://mp.weixin.qq.com/s/50mCaRbrrTt8DqOmp7JTwg<br/>
https://mp.weixin.qq.com/s/AiMz62n3pMOPLfWx-0KmHQ<br/>










