# 一个简单易用的agent sdk
> 简易Javaagent开发sdk包，不需要学习字节码、不需要学习双亲委派、也不需要关心类加载方式。
> 引入核心包后继承相关类快速实现jvm级aop，无视final、private、protect。

# 开始使用
1. 引入核心依赖(暂未上传到maven仓库，自行install)
    ```xml
      <dependency>
            <groupId>com.gxhunter.agent</groupId>
            <artifactId>easy-agent-core</artifactId>
            <version>1.0.1-SNAPSHOT</version>
            <scope>provided</scope>
        </dependency>
    ```
2. maven pom.xml 新增一下几个核心配置
    ```
        <build>
        <finalName>${name}</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.7.0</version>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                    <encoding>UTF-8</encoding>
                    <compilerArgument>-XDignore.symbol.file</compilerArgument>
                    <fork>true</fork>

                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.1.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                        </manifest>
                        <manifestEntries>
                            <Manifest-Version>${version}</Manifest-Version>
                            <Plugin-Name>${name}</Plugin-Name>
                            <Built-By>${author}</Built-By>
                            <Hunter-Agent-Plugin-Entry>${entryClassPath}</Hunter-Agent-Plugin-Entry>
                        </manifestEntries>
                        <addMavenDescriptor>false</addMavenDescriptor>
                    </archive>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>3.0.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                        </manifest>
                        <manifestEntries>
                            <Manifest-Version>${version}</Manifest-Version>
                            <Plugin-Name>${name}</Plugin-Name>
                            <Built-By>${author}</Built-By>
                            <Hunter-Agent-Plugin-Entry>${entryClassPath}</Hunter-Agent-Plugin-Entry>
                        </manifestEntries>
                    </archive>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                </configuration>
                <executions>
                    <execution>
                        <id>make-assembly</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
   ```
   其中：
   1. name: 项目名称
   2. author: 作者
   3. entryClassPath: 入口类
   4. version: 版本
3. AOP方法拦截
   1. 随便创建一个类
   2. 指定拦截Class: 在类上添加注解 `@ClassWeaver("com.xx")`,指定要拦截的对象全路径包名
   3. 指定拦截的method: 编写一个**静态方法**，签名必须是`()Lcom/gxhunter/agent/core/asm/MethodAdvice;`，即: 返回值是MethodAdvice，参数为空
   4. 编写方法体实现拦截，如: 
      ```
        return methodVisitor -> {
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitFieldInsn(PUTFIELD, "org/springframework/cloud/netflix/eureka/EurekaInstanceConfigBean", "metadataMap", "Ljava/util/Map;");
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "com/gxhunter/utils/EurekaUtils", "interceptMetadata", "(Ljava/util/Map;)V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 2);
            methodVisitor.visitEnd();
        };
      ```
4. 编写入口类
   上述配置的 ${entryClassPath} ,实现接口 `com.gxhunter.agent.core.plugin.PluginEntry`
   1. `com.gxhunter.agent.core.plugin.PluginEntry.weavers` : 方法拦截器(就是上述的AOP类)
   2. `com.gxhunter.agent.core.plugin.PluginEntry.init`: 初始化回调(一些初始化的操作放在这里，一般什么都不需要做)
5. 运行，启动参数添加
   `-javaagent:xx\easy-agent-core-jar-with-dependencies.jar=插件uri`
   等号后面是使用url编码 你编译插件的uri地址。例如，开发的插件在`C:\Users\hunter\Desktop\x.zip`，就添加`file%3A%2FC%3A%2FUsers%2Fhunter%2FDesktop%2Fx.zip`即可
6. 完成