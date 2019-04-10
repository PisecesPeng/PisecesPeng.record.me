<h2> Maven, 基本命令与杂项 </h2>

### Maven常见命令

```bash
# 清理,项目产生的临时文件
mvn clean

# 编译,将Java 源程序编译成 class 字节码文件
mvn compile

# 打包,但是不会将最新的代码加载到mvn库
mvn package

# 安装,包含打包(详请搜索"maven生命周期")
mvn install 

# 测试,执行src/test/java/下的junit的单元测试用例
mvn test 

# 打印出项目的整个依赖关系树
mvn dependency:tree

# 显示maven依赖列表
mvn dependency:list

# 下载依赖包的源码
mvn dependency:sources

# 分析依赖关系, 用来取出无用、重复依赖
mvn dependency:analyze

# 在tomcat容器中运行web应用，需要在pom文件中配置tomcat插件
mvn tomcat:run

# 在jetty容器中运行web应用，需要在pom文件中配置jetty插件
mvn jetty:run

# 在springboot容器中运行web应用，需要在pom文件中配置springboot插件
mvn spring-boot:run
```

### Maven常用参数

```bash
# -D 传入属性参数

# -e 如果构建出现异常，能打印完整的StackTrace，方便分析错误原因

# -o 离线本地式执行命令，不去远程仓库更新包 (注意仓库中的*.lastUpdated文件)

# -U  强制检查所有SNAPSHOT依赖更新，确保集成基于最新的状态(默认以天为单位检查更新)

# -B 参数表示让Maven使用批处理模式构建项目，能够避免一些需要人工参与交互而造成的挂起状态
```

### Maven踩坑

1. jar目录下的'_remote.repositories'需要注意'jar'or'pom'的'镜像id'是否正确 ?
``` 
PIPE.RELEASE.jar>central=
PIPE.RELEASE.pom>central=
```

2. jar目录下的'*.lastUpdated'需要注意'.lastUpdated'的值(时间戳)是否为最近 ?
``` 
https\://*.lastUpdated=1234567890000
```