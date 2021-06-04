# Java, SerialVersionUID该如何声明

- [Java, SerialVersionUID该如何声明](#java-serialversionuid该如何声明)
    - [为什么声明固定值为'1L'的SerialVersionUID](#为什么声明固定值为1l的serialversionuid)
    - [为什么需要显式声明每次'不唯一'的SerialVersionUID](#为什么需要显式声明每次不唯一的serialversionuid)

> 在开发中,序列化UID的生成有三种情况:  
> 1. 不声明SerialVersionUID(编译器在编译的时候生成).  
> 2. 显式的声明SerialVersionUID(使用JDK工具生成).  
> 3. 显式的声明固定值为'1L'的SerialVersionUID.  

`ps. 养成良好的习惯,请显式声明UID`  
> 那么,生成SerialVersionUID对序列化有什么帮助呢?  
> SerialVersionUID又叫做'**流标识符(Stream Unique Identifier)**',也就是类的版本定义.  
> 即.JVM在反序列化时,会将'数据流'与'类'的SerialVersionUID比较是否相同.  

---

### 为什么声明固定值为'1L'的SerialVersionUID

> 建议,没什么特殊要求的话,每次都显式声明固定值'1L'的SerialVersionUID  


> 上面有说到,每次在反序列化的时候,都会比较'数据流'和'类'的SerialVersionUID.  
> **如果相同**,那么认为类没有改变,把'数据流'加载为'类'.  
> **如果不同**,那么认为类发生了改变,JVM则会抛出异常(为了保证类的一致性).  

若是声明固定值为'1L'则相当于每次都获取最新的'流'.  
```java
public class Utils {
    private static String FILE_NAME = "/tmp/tmp.bin";
    // 序列化
    public static void writeObject(Serializable s) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 反序列化
    public static Object readObject() {
        Object obj = null;
        try (ObjectInput input = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            obj = input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
```


```java
// 序列化类 version_1.0
public class A implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
```


```java
// 序列化
public class Producer {
    public static void main(String[] args) throws Exception {
        A a = new A();
        a.setName("SerialVersionUID");
        // 序列化,模拟网络传输
        Utils.writeObject(a);
    }
}
```


```java
// 反序列化类 version_1.0
public class A implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
```


```java
// 反序列化
public class Consumer {
    public static void main(String[] args) throws Exception {
        // 反序列化
        A a = (A) Utils.readObject();
    }
}
```

---

### 为什么需要显式声明每次'不唯一'的SerialVersionUID  

> 在以上的代码基础上,使用JDK生成的SerialVersionUID,并对类进行简单修改.  

```java
// 序列化类 version_2.0
public class A implements Serializable {
    private static final long serialVersionUID = 20000L;
    private String name;
    private String address;
    // set/get方法略
}
```

```java
// 反序列化类 version_1.0
public class A implements Serializable {
    private static final long serialVersionUID = 10000L;
    private String name;
    // set/get方法略
}
```

> 大家都知道SerialVersionUID不一致会导致JVM抛出异常.  
> 假设,反序列化类暂时没法更新到version_2.0,那么怎么样能不让JVM抛出异常呢?  
> 将序列化类,使用version_1.0的SerialVersionUID.  

```java
// 序列化类 version_2.0
public class A implements Serializable {
    private static final long serialVersionUID = 10000L;
    private String name;
    private String address;
    // set/get方法略
}
```

> 注意,这样修改后,JVM不会再抛出异常,反序列化也可以运行(但,其无法读取到新增的属性:address)  

`ps. 显式声明可以避免对象不一致问题,但还是不建议这样'欺骗'JVM`
