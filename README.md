# yuckar-java-infra

## 一 介绍
- yuckar-java-infra

## 二 软件架构

### 0. pom
```
<dependency>
    <groupId>yuckar-java-infra</groupId>
    <artifactId>yuckar-java-infra-pom</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <type>pom</type>
</dependency>
```

### 1. common:通用组件
- com.yuckar.infra.common.logger.LoggerUtils
    ```
    org.slf4j.Logger logger = LoggerUtils.logger(clazz);
    logger.info("");
    ```
- com.yuckar.infra.common.hook.HookHelper
    ```
    HookHelper.addHook(module,runnable);
    ```
- com.yuckar.infra.common.term.TermHelper,拦截TERM信号
    ```
    TermHelper.addTerm(module,runnable);
    ```

### 2. buffer:缓存组件
- disruptor
- com.yuckar.infra.buffer.trigger.BufferTrigger<E>
```
this.bufferTrigger = BufferTrigger.<PerfContext, Map<PerfLogTag, PerfLogMetrics>>simple() //
    .disableEnqueueLock()
	.setInterval(1, TimeUnit.MINUTES) //
	.setConsumer(this::handle) //
	.setContainer(Maps::newConcurrentMap, (container, builder) -> {
		container.merge(builder.getPerfLog(), new PerfLogMetrics(builder.getCount(), builder.getMicro()),
				(value1, value2) -> {
					value1.accept(value2.getTotalCount(), value2.getTotalMicro());
					return value1;
				});
		return true;
	}).build();
```

### 3. cluster:集群组件
- com.yuckar.infra.cluster.Cluster<R>
```
Cluster<MailSmtp> smtp = ClusterFactory.gcluster(SmtpClusterInfo.class, MailSmtpInfo.class,
            "network/smtp", info -> new MailSmtp(info.getInfo()), MailSmtp::close);
smtp.getResource().send(......);
```
- com.yuckar.infra.cluster.Master<R>
- com.yuckar.infra.cluster.MasterCluster<R>

### 4. code:代码自动化组件
### 5. crawler:爬虫
### 6. crypto:密码组件
- cipher
    - com.yuckar.infra.crypto.cipher.Decrypt
    - com.yuckar.infra.crypto.cipher.Encrypt
- com.yuckar.infra.crypto.digest.Digest
- com.yuckar.infra.crypto.mac.Mac
- signature
    - com.yuckar.infra.crypto.signature.SignatureSign
    - com.yuckar.infra.crypto.signature.SignatureVertify

### 7. dlock:分布式锁组件
- com.yuckar.infra.dlock.DLock

### 8. executor:执行器组件
- com.yuckar.infra.executor.Executor<T>

### 9. monitor:监控组件
- 系统监控：内存、线程等

### 10. network
- com.yuckar.infra.network.browser.Browser
- com.yuckar.infra.network.capture.ChromeCapture
- ftp
    - com.yuckar.infra.network.ftp.KftpClient
    - com.yuckar.infra.network.ftp.KftphttpClient
    - com.yuckar.infra.network.ftp.KftpsClient
- http
    - com.yuckar.infra.network.http.KhttpClient
    - com.yuckar.infra.network.http.KhttpAsyncClient
- com.yuckar.infra.network.jsch.sftp.JschSftp
- mail
    - com.yuckar.infra.network.mail.receiver.MailReceiver
    - com.yuckar.infra.network.mail.sender.MailSender
    - com.yuckar.infra.network.mail.sender.MailSmtp
- okhttp
    - com.yuckar.infra.network.okhttp.OkhttpAsync
    - com.yuckar.infra.network.okhttp.OkhttpSync
- com.yuckar.infra.network.retrofit.RetrofitUtils

### 11. perf:性能统计组件
- com.yuckar.infra.perf.utils.PerfUtils

### 12. register:配置组件
- com.yuckar.infra.register.context.IRegisterContext
- com.yuckar.infra.register.group.context.IGroupRegisterContext
- com.yuckar.infra.register.kcache.KLoadingCache<K, V>
- com.yuckar.infra.register.kconf.Kconf<T>
- com.yuckar.infra.register.resource.IResource<I, R>

### 13. runner
- com.yuckar.infra.runner.binlog.BinlogRunner
- com.yuckar.infra.runner.mq.kafka.KafkaRunner<K, V>
- com.yuckar.infra.runner.mq.rocket.RocketRunner
- com.yuckar.infra.runner.rpc.grpc.GrpcRunner
- com.yuckar.infra.runner.rpc.grpc.GrpcRunnerBindable
- com.yuckar.infra.runner.sch.ksch.KschRunner
- com.yuckar.infra.runner.sch.quatz.QuatzRunner
- com.yuckar.infra.runner.simple.SimpleRunner
- com.yuckar.infra.runner.server.RunnerServerMain

### 14. script
- com.yuckar.infra.script.utils.ScriptUtils

### 15. server
- com.yuckar.infra.server.jetty.JettyServer
- com.yuckar.infra.server.startup.Startable
- com.yuckar.infra.server.startup.StartableMain

### 16. storge
- db
    - com.yuckar.infra.storage.db.jdbc.Kjdbc<T>
    - com.yuckar.infra.storage.db.jdbc.KjdbcRepository
    - com.yuckar.infra.storage.db.jdbc.KjdbcRepositoryResource<I>
        - com.yuckar.infra.storage.db.jdbc.dbcp2.Dbcp2RepositoryResource
        - com.yuckar.infra.storage.db.jdbc.druid.DruidRepositoryResource
        - com.yuckar.infra.storage.db.jdbc.hikari.HikariRepositoryResource
    - com.yuckar.infra.storage.db.jdbc.cluster.MasterRepositoryResource<I, C>
        - com.yuckar.infra.storage.db.jdbc.dbcp2.Dbcp2MasterRepositoryResource
        - com.yuckar.infra.storage.db.jdbc.druid.DruidMasterRepositoryResource
        - com.yuckar.infra.storage.db.jdbc.hikari.HikariMasterRepositoryResource
    - com.yuckar.infra.storage.db.jdbc.cluster.MasterClusterRepositoryResource<I, C>
        - com.yuckar.infra.storage.db.jdbc.dbcp2.Dbcp2MasterClusterRepositoryResource
        - com.yuckar.infra.storage.db.jdbc.druid.DruidMasterClusterRepositoryResource
        - com.yuckar.infra.storage.db.jdbc.hikari.HikariMasterClusterRepositoryResource
    - 注解
        - com.yuckar.infra.storage.db.model.KdbTable
        - com.yuckar.infra.storage.db.model.KdbColumn
        - com.yuckar.infra.storage.db.model.KdbIndex
        - com.yuckar.infra.storage.db.model.KdbInsertTime
        - com.yuckar.infra.storage.db.model.KdbUpdateTime
    - sql
- es
    - com.yuckar.infra.storage.es.ElasticsearchRepository
- hbase
    - com.yuckar.infra.storage.hbase.HbaseRepository.HbaseRepository(Configuration)
- hdfs
    - com.yuckar.infra.storage.hdfs.HdfsRepository
- jedis
    - com.yuckar.infra.storage.jedis.JedisRepository
    - com.yuckar.infra.storage.jedis.JedisShardingRepository
- lucene
    - com.yuckar.infra.storage.lucene.Lucene
- mongo
    - com.yuckar.infra.storage.mongo.MongoRepository
- memcache
    - com.yuckar.infra.storage.spy.SpyRepository

### 17. text
- json
    - com.yuckar.infra.text.json.ConfigUtils
    - com.yuckar.infra.text.json.JsonUtils
- pinyin
    - com.yuckar.infra.text.pinyin.PinyinUtils
- tpl
    - com.yuckar.infra.text.tpl.beetl.Beetl
    - com.yuckar.infra.text.tpl.enjoy.Enjoy
    - com.yuckar.infra.text.tpl.freemarker.Freemarker
- xml
    - 

### 18. thread
- com.yuckar.infra.thread.pool.KrExecutors
- com.yuckar.infra.thread.utils.ThreadHelper

### 19. trace
- 跟踪

## 三 Maven依赖
```
<dependency>
	<groupId>yuckar-infra</groupId>
	<artifactId>yuckar-infra-pom</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<type>pom</type>
</dependency>
```
