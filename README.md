# dynamic-evaluation-system
本项目通过sprintboot框架完成接口相应的五个功能，查询用户影响力，查询帖子热度，查询用户榜单，查询帖子榜单，上传数据。
项目分为[model层](springboot_01_02_quickstart/src/main/java/com/itheima/model)，[repository层](springboot_01_02_quickstart/src/main/java/com/itheima/repository)，[service层](springboot_01_02_quickstart/src/main/java/com/itheima/service)和[controller层](springboot_01_02_quickstart/src/main/java/com/itheima/controller)，各层具体内容如下。


1.[model层](springboot_01_02_quickstart/src/main/java/com/itheima/model)为图数据库实体层，包含两个部分，分别是[节点实体](springboot_01_02_quickstart/src/main/java/com/itheima/model/node)和[关系实体](springboot_01_02_quickstart/src/main/java/com/itheima/model/relationship)。


2.[repository层](springboot_01_02_quickstart/src/main/java/com/itheima/repository)使用了neo4j自带的repository框架，对每个节点实体建立了相应的操作数据库的类。


3.[service层](springboot_01_02_quickstart/src/main/java/com/itheima/service)主要实现的是调用repository层的数据库操作实现接口功能。比如在上传数据这个接口中，就实现了将传入数据写入数据库并调用算法文件[getScore.py](springboot_01_02_quickstart/src/main/resources/bin/getScore.py)计算新一轮的用户影响力和帖子热度值的功能。


4.[controller层](springboot_01_02_quickstart/src/main/java/com/itheima/controller)主要实现的是对前端传入数据的处理以及调用service层实现对应功能，并返回给前端处理结果。


除以上内容以外，本项目还实现了[异常处理](springboot_01_02_quickstart/src/main/java/com/itheima/exception)和[写入日志](springboot_01_02_quickstart/src/main/resources/logback.xml)的功能。为了更好的写入前端访问和后端返回结果的日志，我还构造了[拦截器](springboot_01_02_quickstart/src/main/java/com/itheima/log/LoggingInterceptor.java)对接收和返回的数据进行拦截。同时在[配置文件](springboot_01_02_quickstart/src/main/java/com/itheima/log/Neo4jConfig.java)中配置了该拦截器并实现了跨域访问。
