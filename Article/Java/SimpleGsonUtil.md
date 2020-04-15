<h2> 说一说Gson有哪些常用方法 </h2>

- [1. 基础的toJson()](#1-%E5%9F%BA%E7%A1%80%E7%9A%84tojson)
- [2. 序列化空值](#2-%E5%BA%8F%E5%88%97%E5%8C%96%E7%A9%BA%E5%80%BC)
- [3. @SerializedName指定名称](#3-serializedname%E6%8C%87%E5%AE%9A%E5%90%8D%E7%A7%B0)
- [4. @Expose限制返回结果](#4-expose%E9%99%90%E5%88%B6%E8%BF%94%E5%9B%9E%E7%BB%93%E6%9E%9C)
- [5. 版本控制输出结果](#5-%E7%89%88%E6%9C%AC%E6%8E%A7%E5%88%B6%E8%BE%93%E5%87%BA%E7%BB%93%E6%9E%9C)
- [6. 格式化日期输出](#6-%E6%A0%BC%E5%BC%8F%E5%8C%96%E6%97%A5%E6%9C%9F%E8%BE%93%E5%87%BA)
- [7. 格式化Json输出](#7-%E6%A0%BC%E5%BC%8F%E5%8C%96json%E8%BE%93%E5%87%BA)
- [8. 禁止转义HTML标签](#8-%E7%A6%81%E6%AD%A2%E8%BD%AC%E4%B9%89html%E6%A0%87%E7%AD%BE)
- [9. 通过ExclusionStrategy忽略指定类型的字段](#9-%E9%80%9A%E8%BF%87exclusionstrategy%E5%BF%BD%E7%95%A5%E6%8C%87%E5%AE%9A%E7%B1%BB%E5%9E%8B%E7%9A%84%E5%AD%97%E6%AE%B5)
- [10. 通过Modifiers忽略指定类型的字段](#10-%E9%80%9A%E8%BF%87modifiers%E5%BF%BD%E7%95%A5%E6%8C%87%E5%AE%9A%E7%B1%BB%E5%9E%8B%E7%9A%84%E5%AD%97%E6%AE%B5)
- [11. 设置命名规则](#11-%E8%AE%BE%E7%BD%AE%E5%91%BD%E5%90%8D%E8%A7%84%E5%88%99)
- [12. 自定义命名规则](#12-%E8%87%AA%E5%AE%9A%E4%B9%89%E5%91%BD%E5%90%8D%E8%A7%84%E5%88%99)
- [13. 使用Json输出特殊double/float值](#13-%E4%BD%BF%E7%94%A8json%E8%BE%93%E5%87%BA%E7%89%B9%E6%AE%8Adoublefloat%E5%80%BC)
- [14. 将Long序列化为其他类型](#14-%E5%B0%86long%E5%BA%8F%E5%88%97%E5%8C%96%E4%B8%BA%E5%85%B6%E4%BB%96%E7%B1%BB%E5%9E%8B)
- [15. other](#15-other)

> **Gson**是Google提供的用来在**Java对象**和**JSON数据**之间进行映射的Java类库.<br/>
> Gson提供了fromJson()和toJson()两个直接用于解析和生成的方法,前者实现反序列化,后者实现了序列化.<br/>
> 本文主要是说说常用的**序列化**方法.<br/>

<hr>

```java
// 本文操作使用的Entity
public class GsonEntity {

    // @Expose(serialize = true, deserialize = true)
    private final String Name = "Gson";

    // @SerializedName("sex")
    private String address;

    @Since(1.0)
    private int age = 0;

    @Until(0.9)
    private Date time;

    private long l_ong;

    private boolean _this_IsA_flag;

    private float f_loat;

    private static String staticStr;

    // get/set等方法略

}
```

### 1. 基础的toJson()
```java
String gsonString;

{
    // 未设置任何值
    // 对象 -> json
    GsonEntity entity = new GsonEntity();
    gsonString = new Gson().toJson(entity);
    System.out.println(gsonString);
    // 输出
    // {"Name":"Gson","age":0,"l_ong":0,"_this_IsA_flag":false,"f_loat":0.0}
}
{
    // json -> 对象
    GsonEntity entity = new Gson().fromJson(gsonString, GsonEntity.class);
    System.out.println(entity);
    // 输出
    // GsonEntity(Name=Gson, address=null, age=0, time=null, l_ong=0, _this_IsA_flag=false, f_loat=0.0)
}
{
    // 类似map结构输出
    JsonObject jsonObject = (JsonObject) new JsonParser().parse(gsonString);
    System.out.println(jsonObject);
    // 输出
    // {"Name":"Gson","age":0,"l_ong":0,"_this_IsA_flag":false,"f_loat":0.0}
}
```

### 2. 序列化空值
```java
// 未设置任何值
GsonEntity entity = new GsonEntity();

// 设置未处理的值返回
String gsonString2 = new GsonBuilder().serializeNulls().create().toJson(entity);
System.out.println(gsonString2);

// 输出
// {"Name":"Gson","address":null,"age":0,"time":null,"l_ong":0,"_this_IsA_flag":false,"f_loat":0.0}

```

### 3. @SerializedName指定名称
```java
GsonEntity entity = new GsonEntity();
entity.setAddress("这是地址?");

// 去掉Entity中@SerializedName的注释,将返回名称设置为"sex"
String gsonString3 = new GsonBuilder().create().toJson(entity);
System.out.println(gsonString3);

// 输出
// {"Name":"Gson","sex":"这是地址?","age":0,"l_ong":0,"_this_IsA_flag":false,"f_loat":0.0}
```

### 4. @Expose限制返回结果
```java
GsonEntity entity = new GsonEntity();

// 去掉Entity中@Expose的注释(序列化与反序列化默认都为true)
String gsonString4 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(entity);
System.out.println(gsonString4);

// 输出
// {"Name":"Gson"}
```

### 5. 版本控制输出结果
```java
GsonEntity entity = new GsonEntity();
entity.setTime(new Date());

// 加上版本控制@Since&@Until后可以不同版本不同数据

String gsonString5 = new GsonBuilder().setVersion(0.8).create().toJson(entity);
System.out.println(gsonString5);
// 输出 : {"Name":"Gson","time":"May 19, 2019 3:58:16 PM","l_ong":0,"_this_IsA_flag":false,"f_loat":0.0}

String gsonString6 = new GsonBuilder().setVersion(1.1).create().toJson(entity);
System.out.println(gsonString6);
// 输出 : {"Name":"Gson","age":0,"l_ong":0,"_this_IsA_flag":false,"f_loat":0.0}
```

### 6. 格式化日期输出
```java
GsonEntity entity = new GsonEntity();
entity.setTime(new Date());

// 设置日期输出格式
String gsonString7 = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(entity);
System.out.println(gsonString7);

// 输出
// {"Name":"Gson","age":0,"time":"2019-05-19","l_ong":0,"_this_IsA_flag":false,"f_loat":0.0}
```

### 7. 格式化Json输出
```java
GsonEntity entity = new GsonEntity();

// 格式化输出Json
String gsonString8 = new GsonBuilder().setPrettyPrinting().create().toJson(entity);
System.out.println(gsonString8);

// 输出
/*
{
  "Name": "Gson",
  "age": 0,
  "l_ong": 0,
  "_this_IsA_flag": false,
  "f_loat": 0.0
}
*/
```

### 8. 禁止转义HTML标签
```java
GsonEntity entity = new GsonEntity();
entity.setAddress("<!DOCTYPE html>\n" +
                "<html lang=\"zh-cn\">\n" +
                "    <head>\n" +
                "        <meta charset=\"utf-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <a name=\"top\"></a>\n" +
                "    </body>\n" +
                "</html>");

// 设置禁止转义HTML标签
String gsonString9 = new GsonBuilder().disableHtmlEscaping().create().toJson(entity);
System.out.println(gsonString9);

// 输出
// {"Name":"Gson","address":"<!DOCTYPE html>\n<html lang=\"zh-cn\">\n    <head>\n        <meta charset=\"utf-8\">\n        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n    </head>\n    <body>\n        <a name=\"top\"></a>\n    </body>\n</html>","age":0,"l_ong":0,"_this_IsA_flag":false,"f_loat":0.0}
```

### 9. 通过ExclusionStrategy忽略指定类型的字段
```java
GsonEntity entity = new GsonEntity();

String gsonString10 = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                if (f.getAnnotation(Expose.class) != null) return true;  // 忽略掉带有注解为@Expose的字段
                if (f.getName().contains("_")) return true;  // 忽略掉带有"_"的字段
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return clazz == Date.class || clazz == boolean.class;  // 忽略掉Date&boolean类型的字段
            }
        }).create().toJson(entity);
System.out.println(gsonString10);

// 输出
// {"age":0}
```

### 10. 通过Modifiers忽略指定类型的字段
```java
GsonEntity entity = new GsonEntity();

// 指定忽略的类型
String gsonString11 = new GsonBuilder()
        .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.STATIC)  // 忽略掉final&static修饰的字段
        .create().toJson(entity);
System.out.println(gsonString11);

// 输出
// {"age":0,"l_ong":0,"_this_IsA_flag":false,"f_loat":0.0}
```

### 11. 设置命名规则
> **@SerializedName**注解拥有最高优先级,在加有@SerializedName注解的字段上FieldNamingStrategy不生效
```java
GsonEntity entity = new GsonEntity();

// 直接匹配JavaModel的字段名称
String gsonString12 = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create().toJson(entity);
System.out.println(gsonString12);
// 输出 : {"Name":"Gson","age":0,"l_ong":0,"_this_IsA_flag":false,"f_loat":0.0}

// 将大写字母都变成小写,并且在修改部分前用下划线隔开
String gsonString13 = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create().toJson(entity);
System.out.println(gsonString13);
// 输出 : {"name":"Gson","age":0,"l_ong":0,"_this__is_a_flag":false,"f_loat":0.0}

// 将大写字母都变成小写,并且在修改部分前用破折号隔开(除首字母)
String gsonString14 = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create().toJson(entity);
System.out.println(gsonString14);
// 输出 : {"name":"Gson","age":0,"l_ong":0,"_this_-is-a_flag":false,"f_loat":0.0}

// 将大写字母都变成小写,并且在修改部分前用点隔开
String gsonString15 = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DOTS).create().toJson(entity);
System.out.println(gsonString15);
// 输出 : {"name":"Gson","age":0,"l_ong":0,"_this_.is.a_flag":false,"f_loat":0.0}

// 首字母大写
String gsonString16 = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create().toJson(entity);
System.out.println(gsonString16);
// 输出 : {"Name":"Gson","Age":0,"L_ong":0,"_This_IsA_flag":false,"F_loat":0.0}

// 首字母大写，且之后的每个大写字母前用空格隔开
String gsonString17 = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES).create().toJson(entity);
System.out.println(gsonString17);
// 输出 : {"Name":"Gson","Age":0,"L_ong":0,"_This_ Is A_flag":false,"F_loat":0.0}
```

### 12. 自定义命名规则
```java
GsonEntity entity = new GsonEntity();

// 自定义命名规则
String gsonString18 = new GsonBuilder().setFieldNamingStrategy(new FieldNamingStrategy() {
    @Override
    public String translateName(Field f) {
        // 实现自定义命名规则
        return f.getName().replaceAll("_", ""); // 将下划线去掉
    }
}).create().toJson(entity);
System.out.println(gsonString18);

// 输出
// {"Name":"Gson","age":0,"long":0,"thisIsAflag":false,"float":0.0}
```

### 13. 使用Json输出特殊double/float值
```java
GsonEntity entity = new GsonEntity();
entity.setF_loat(Float.POSITIVE_INFINITY);

// JSON规范不允许特殊的double值(NaN，Infinity，-Infinity),所以我们需要防止Gson抛出异常
String gsonString19 = new GsonBuilder().serializeSpecialFloatingPointValues().create().toJson(entity);
System.out.println(gsonString19);

// 输出
// {"Name":"Gson","age":0,"l_ong":0,"_this_IsA_flag":false,"f_loat":Infinity}
```

### 14. 将Long序列化为其他类型
```java
GsonEntity entity = new GsonEntity();
entity.setL_ong(Long.MAX_VALUE);

// 将Long类型转换为String类型,或其他类型
String gsonString20 = new GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).create().toJson(entity);
System.out.println(gsonString20);

// 输出
// {"Name":"Gson","age":0,"l_ong":"9223372036854775807","_this_IsA_flag":false,"f_loat":0.0}
```

### 15. other
```java
// disableInnerClassSerialization(): 禁止序列化内部类
// generateNonExecutableJson(): 生成不可执行的Json(插入一些)]}'特殊字符)
// enableComplexMapKeySerialization(): 允许序列化复合型key(如一个自定义对象等)
// 等等方法..
```
