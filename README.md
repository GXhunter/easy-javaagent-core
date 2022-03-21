# 一个简单易用的agent sdk
> 简易Javaagent开发sdk包，不需要学习字节码、不需要学习双亲委派、也不需要关心类加载方式。
> 引入核心包后继承相关类快速实现jvm级aop，无视final、private、protect。

# 开始使用
1. 创建空maven项目
    
2. 在pom.xml,引入parent包,并配置属性
    ```xml
        <parent>
            <artifactId>hunter-agent-parent</artifactId>
            <groupId>io.github.gxhunter</groupId>
            <version>仓库新版</version>
        </parent>
        <properties>
            <author>hunter</author>
            <agent.name>cloud-debug-agent</agent.name>
            <agent.entry.class>com.gxhunter.InitAgent</agent.entry.class>
        </properties>
    ```
    其中：
    
   | 属性              | 描述     | 是否必须 |
    | ----------------- | -------- | -------- |
    | author            | 项目作者 | 是       |
    | agent.name        | 项目名称 | 是       |
    | agent.entry.class | 项目入口 | 是       |
    
    
    
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

# 能做什么？

## 端云联调

​	只要你有一台跳板机，通过angent拦截NIO和BIO的方法，转到跳板机地址，即可实现端云联调。

​	**此项目已初步开发完成，等待稳定后开源**

## java软件破解

​	使用java编写的收费软件（包括idea）理论上都可以通过拦截 `java.net.SocksSocketImpl` 的方法实现破解，但拦截那些url，伪装成什么数据，请自行抓包分析

## 其他增强

无侵入的日志收集、性能分析等等

