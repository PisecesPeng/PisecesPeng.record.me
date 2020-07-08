<h2> 单例模式示例 </h2>

- [饿汉型](#饿汉型)
- [懒汉式](#懒汉式)
- [懒汉式 + DCL + volatile](#懒汉式--dcl--volatile)
- [Holder式](#holder式)
- [枚举式](#枚举式)

> 仅列出常见单例模式写法

```java
// 实例化类
class Instance {
    Instance() {
        System.out.println("实例化..");
    }
}
```
<br/>

### 饿汉型

```java
class HazardousTypeSingleton {
    // 私有构造方法
    private HazardousTypeSingleton() {}

    // 类加载时立即实例化对象, 仅实例化一次, 线程安全的
    private static final Instance intance = new Instance();

    /**
     * 类加载时已初始化，不会有多线程的问题
     */
    public static Instance getInstance() {
        System.out.println("饿汉型模式");
        return intance;
    }
}
```
<br/>


### 懒汉式

```java
class LazyTypeSingleton {
    // 私有构造方法
    private LazyTypeSingleton() {}

    // 静态私用成员，没有初始化
    private static Instance intance = null;

    /***
     * 直接加synchronized关键字
     */
    public synchronized static Instance getIntance() {
        if (null == intance) {
            System.out.println("懒汉型模式");
            return new Instance();
        }
        return intance;
    }
}
```
<br/>


### 懒汉式 + DCL + volatile

```java
// 懒汉式 双重检查锁DCL(double-checked locking) + volatile
class LazyDCLTypeSingleton {
    // 私有构造方法
    private LazyDCLTypeSingleton() {}

    // volatile关键字修饰，防止指令重排
    private volatile static Instance intance = null;

    /***
     * Double Check Lock(DCL) 双重锁校验
     */
    public static Instance getInstance() {
        if (null == intance) {
            synchronized (LazyDCLTypeSingleton.class) {
                if (null == intance) {
                    System.out.println("懒汉式 DCL双重锁校验");
                    return new Instance();
                }
            }
        }
        return intance;
    }
}
```
<br/>


### Holder式

```java
// 静态内部类 Holder方式
class InnerTypeSingleton {
    // 私有构造方法
    private InnerTypeSingleton(){
        throw new IllegalStateException();
    }

    // 静态内部类方式，类似饿汉保证天然的线程安全
    private static class SingletonHolder{
        private final static Instance instance = new Instance();
    }

    static Instance getInstance(){
        System.out.println("静态内部类方式");
        return SingletonHolder.instance;
    }
}
```
<br/>


### 枚举式 

```java
enum  EnumSingleton {
    INSTANCE;

    private Instance instance;

    EnumSingleton() {
        instance = new Instance();
    }

    public Instance getInstance() {
        System.out.println("枚举方式");
        return instance;
    }
}
```
<br/>

<br/>

> ps. 可使用**main**方法, 观察实例化顺序

```java
public static void main(String[] args) {
    System.out.println("------------------");
    Instance s1 = HazardousTypeSingleton.getInstance();
    System.out.println("------------------");
    Instance s2 = LazyTypeSingleton.getIntance();
    System.out.println("------------------");
    Instance s3 = LazyDCLTypeSingleton.getInstance();
    System.out.println("------------------");
    Instance s4 = InnerTypeSingleton.getInstance();
    System.out.println("------------------");
    Instance s5 = EnumSingleton.INSTANCE.getInstance();
    System.out.println("------------------");
}
/*
输出:
------------------
实例化..
饿汉型模式
------------------
懒汉型模式
实例化..
------------------
懒汉式 DCL双重锁校验
实例化..
------------------
静态内部类方式
实例化..
------------------
实例化..
枚举方式
------------------
*/
```

