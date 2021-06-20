## Maven, shade-plugin的简单配置

> maven-shade-plugin可将依赖包放入进指定jar位置  

ps. 官方文档: https://maven.apache.org/plugins/maven-shade-plugin/

```xml
<!-- 模版数据来自 mybatis3 pom.xml (https://github.com/mybatis/mybatis-3/blob/master/pom.xml) -->
<...>
    <dependency>
      <groupId>ognl</groupId>
      <artifactId>ognl</artifactId>
      <...>
    </dependency>
    <dependency>
      <groupId>org.javassist</groupId>
      <artifactId>javassist</artifactId>
      <...>
    </dependency>
<...>
<build>
    <plugins>
        <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>shade</goal>
            </goals>
            <configuration>
              <!-- 禁止生成 dependency-reduced-pom.xml 文件 -->
              <createDependencyReducedPom>false</createDependencyReducedPom>
              <artifactSet>
                <!-- 为生成的jar包选择'包含/排除'依赖(支持通配符) -->
                <includes>
                  <include>org.mybatis:mybatis</include>
                  <include>ognl:ognl</include>
                  <include>org.javassist:javassist</include>
                </includes>
                 <excludes>
                  <exclude>...</exclude>
                </excludes>
              </artifactSet>
              <relocations>
                <!-- 将指定的资源, 打包进jar的指定目录 -->
                <relocation>
                  <pattern>ognl</pattern>
                  <shadedPattern>org.apache.ibatis.ognl</shadedPattern>
                </relocation>
                <relocation>
                  <pattern>javassist</pattern>
                  <shadedPattern>org.apache.ibatis.javassist</shadedPattern>
                  <excludes>
                    <!-- 不打入新的jar中, 保留在原package -->
                    <exclude>...</exclude>
                  </excludes>
                </relocation>
              </relocations>
              <transformers>
                <!-- 更多的transformer在 http://maven.apache.org/plugins/maven-shade-plugin/examples/resource-transformers.html -->
                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                  <mainClass>ThisisMainName</mainClass>
                </transformer>
              </transformers>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
</build>
```
