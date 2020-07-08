<h2> Maven, assembly-plugin的简单配置 </h2>

> 有时候需要启动一些小服务(类似脚本, 处理完结束任务). <br/>
> 那么这时候, 我们不会再用SpringBoot那么大费周章. <br/>
> 所以就需要用'main'方法启动服务了. <br/>

如何指定'主类', 并将外部引用的依赖一齐打入jar包呢? <br/>
可参考下面的``` pom.xml ```中的``` <build/> ```片段:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-assembly-plugin</artifactId>
            <version>3.2.0</version>
            <configuration>
                <archive>
                    <manifest>
                        <mainClass>xxx.xxx.MainClassName</mainClass>
                    </manifest>
                    <manifestEntries>
                        <Class-Path>.</Class-Path>
                    </manifestEntries>
                </archive>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
            </configuration>
            <executions>
                <execution>
                    <id>make-assembly</id> <!-- this is used for inheritance merges -->
                    <phase>package</phase> <!-- bind to the packaging phase -->
                    <goals>
                        <goal>single</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

