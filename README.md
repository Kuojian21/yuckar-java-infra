<center><h1>yuckar-java-infra</h1></center>

[toc]


# 一 介绍
1. yuckar-java-infra
2. pom
    ```
    <dependency>
        <groupId>yuckar-java-infra</groupId>
        <artifactId>yuckar-java-infra-pom</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <type>pom</type>
    </dependency>
    ```

# 二 组件

## 1. base:基础组件
### (1) 简介
通用组件是在依赖基本的jar包的基础上开发的一些常用功能，其依赖的jar包含：
- commons-pool2：对象池
- commons-collections4：集合
- commons-text：字符串
- commons-io：io
- logback-classic/slf4j-api：日志
- guava
- cglib：代理
- stream：函数式编程
- com.fasterxml.jackson*：json

### (2) args：参数解析
1. com.yuckar.infra.base.args.Args
2. com.yuckar.infra.base.args.MainArgs：存储入口参数
3. 示例
    ```
    java cp .. Main key1=val1 --key2 val2
    public static void main(String[] args) {
        MainArgs.args(Args.args(args));
        MainArgs.args().value("key1").orElse("")
        MainArgs.args().value("key2").orElse("")
    }
    ```
    
### (3) bean：bean相关
1. 集合Builder
    - com.yuckar.infra.base.bean.builder.ListBeanBuilder<T>
        ```
        ListBeanBuilder.of(list?).add(val1).add(val2).build();
        ```
    - com.yuckar.infra.base.bean.builder.SetBeanBuilder<T>
        ```
        SetBeanBuilder.of(set?).add(val1).add(val2).build();
        ```
    - com.yuckar.infra.base.bean.builder.MapBeanBuilder<K, V>
        ```
        MapBeanBuilder.of(map?).put(key1,val1).add(key2,val2).build();
        ```
2. BeanInfo
    - com.yuckar.infra.base.bean.info.BeanInfoHelper
        ```
        PropertyDescriptor pd = BeanInfoHelper.beanInfo(clazz).descriptor("val");
        ```
3. simple
    - com.yuckar.infra.base.bean.simple.Pair<K, V>
    - com.yuckar.infra.base.bean.simple.Tuple<V1, V2>
4. singleton
    - com.yuckar.infra.base.bean.singleton.SingletonBeans.bean(Class<T>)：实例化单例。
    - com.yuckar.infra.base.bean.singleton.SingletonBeans.beans(Class<T>)：该类及其子类已经实例化的所有单例。

### (4) BufferTrigger：缓存并异步执行
1. com.yuckar.infra.base.buffer.trigger.BufferTrigger<E>
2. simple
    ```
    BufferTrigger.<PerfContext, Map<PerfLogTag, PerfLogMetrics>>simple() //
        .setContainer(Maps::newConcurrentMap, (container, builder) -> {
            container.merge(builder.getPerfLog(), new PerfLogMetrics(builder.getCount(), builder.getMicro()),
                (value1, value2) -> {
                    value1.accept(value2.getTotalCount(), value2.getTotalMicro());
                    return value1;
                });
            }) //
        .setConsumer(this::handle) //
        .setInterval(1, TimeUnit.MINUTES) //
        .disableEnqueueLock() //
        .build();
    ```
2. batch
    ```
    BufferTrigger.<Model>batch().setBatchConsumer(100, models -> handle(models)).build();
    ```

### (5) Executor：执行模板
- com.yuckar.infra.base.executor.Executor<T>

### (6) file：文件创建与监控
1. com.yuckar.infra.base.file.utils.FileUtils
    - FileUtils.createFileIfNoExists
    - FileUtils.createDirIfNoExists
2. com.yuckar.infra.base.file.watch.IWatch
    - com.yuckar.infra.base.file.watch.IWatch.monitor(long)
    - com.yuckar.infra.base.file.watch.IWatch.service()
    - 示例
        ```
        IWatch.monitor(10_000).watch(path, new FileAlterationListenerAdaptor(){
            @Override
            public void onDirectoryChange(final File directory) {
                System.out.println("onDirectoryChange:" + directory.getAbsolutePath());
            }
        });
        ```

### (7) hook：系统hook
1. com.yuckar.infra.base.hook.HookHelper
2. 示例
    ```
    HookHelper.addHook(module, () -> {
        
    });
    ```
### (8) json：json解析与格式化
1. com.yuckar.infra.base.json.ConfigUtils
    ```
    T model ...
    Map<?,?> map ...
    ConfigUtils.config(model, map);
    ```

2. com.yuckar.infra.base.json.JsonUtils
    ```
    JsonUtils.toJson(Object)
    JsonUtils.toPrettyJson(Object)
    JsonUtils.fromJson(String, Class<T>)
    JsonUtils.fromListJson(String, Class<E>)
    JsonUtils.fromSetJson(String, Class<E>)
    JsonUtils.fromMapJson(String, Class<K>, Class<V>)
    ```

### (9) lazy：Lazy*
- com.yuckar.infra.base.lazy.LazyCallable<T>
- com.yuckar.infra.base.lazy.LazyConsumer<T>
- com.yuckar.infra.base.lazy.LazyFunction<T, R>
- com.yuckar.infra.base.lazy.LazyRunnable
- com.yuckar.infra.base.lazy.LazySupplier<T>

### (10) logger：日志
1. com.yuckar.infra.base.logger.LoggerUtils
    ```
    logger = LoggerUtils.logger(clazz);
    logger.info("");
    ```
2. com.yuckar.infra.base.logger.IKLoggerFactory：定制化LoggerFactory
    - 配置
        ```
        META-INF > services > com.yuckar.infra.base.logger.IKLoggerFactory  
        ```
    - 示例
        ```
        public class AppKLoggerFactory extends Slf4jKLoggerFactory {
            static {
                System.setProperty("LOG_PATH", "${LOG_PATH}");
                LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
                JoranConfigurator configurator = new JoranConfigurator();
                configurator.setContext(context);
                context.reset();
                try {
                    configurator.doConfigure("${logback.xml}");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ```

### (11) perf：性能统计
1. com.yuckar.infra.base.perf.PerfUtils
    - 方法
        ```
        PerfUtils.perf("${namespace}", "${tag}","${extra}" ).count(1).micro(stopwatch.elapsed(TimeUnit.MICROSECONDS)).logstash();
        ```
    - 结果
        ```
        2026-04-04 19:51:24.671
        name                                       total count       max     min      avg variance   top-95   top-99
        ORDER-BY-TOTAL
        Executor.KhttpClient_done_s1.fengbao9.com 84.08s   344 1027.82ms 53.13ms 244.42ms   294.10 568.60ms 870.10ms
        Executor.FFmpegMediaInfo_done             25.36s   377  147.40ms 16.20ms  67.28ms     3.64 106.30ms 126.80ms
        ORDER-BY-AVG
        Executor.KhttpClient_done_s1.fengbao9.com 84.08s   344 1027.82ms 53.13ms 244.42ms   294.10 568.60ms 870.10ms
        Executor.FFmpegMediaInfo_done             25.36s   377  147.40ms 16.20ms  67.28ms     3.64 106.30ms 126.80ms
        ```
2. com.yuckar.infra.base.perf.IPerfHandler：自定义日志输出
    ```
    public class DefaultPerfHandler implements IPerfHandler {
        @Override
        public void handle(List<PerfLogHolder> perfs) {
            ......  
        }
    }
    
    ```

### (12) process：创建进程
- com.yuckar.infra.base.process.ProcessExecutor
    ```
    ProcessExecutor.of(workdir,charset,add_sh_or_cmd).exec("ffmpeg.exe -i video.mp4 -vf \"select='eq(pict_type,I)'\" -vsync vfr -frame_pts 1 " + out + "/frame%08d.jpg")
    ```

### (13) scanner：文件扫描
1. com.yuckar.infra.base.scanner.Scanner<T>
    ```
    Scanner.of(str -> {
        String cls = str.replace("/", ".");
        try {
            Class<?> clazz = Class.forName(cls);
            if (Module.class.isAssignableFrom(clazz) && Module.class != clazz) {
                registerModule((Module) clazz.getDeclaredConstructor(new Class<?>[0]).newInstance(new Object[0]));
                LOGGER.info("module:{} succ", cls);
            }
        } catch (Throwable e) {
            LOGGER.info("module:" + cls + " fail");
            LOGGER.debug("module:" + cls + " fail", e);
        }
    }, ThreadHelper.getContextClassLoader()) //
        .scan("com/fasterxml/jackson", "(com/fasterxml/jackson.+Module)\\.class$")
        .scan("com/hubspot/jackson/datatype/protobuf",
                "(com/hubspot/jackson/datatype/protobuf.+Module)\\.class$");
    ```
2. com.yuckar.infra.base.scanner.ClazzScanner
3. com.yuckar.infra.base.scanner.FileScanner

### (14) term：拦截TERM信号
- com.yuckar.infra.base.term.TermHelper,拦截TERM信号
- 示例
    ```
    TermHelper.addTerm(module,() -> {
        
    });
    ```

### (15) thread：线程相关
1. com.yuckar.infra.base.thread.ThreadHelper
    - ThreadHelper.getContextClassLoader()：获取ClassLoader
    - ThreadHelper.wrap()：封装Runnable/Callable，增加TraceID
2. com.yuckar.infra.base.thread.KrExecutors
    - 在 java.util.concurrent.Executorsde的基础上增加TraceID支持
3. com.yuckar.infra.base.thread.MapperFuture<V, T>
    - 在 get 结果后，执行mapper函数

### (16) traceID：追踪ID
1. logger:TRACEID
2. 示例
    ```
    TraceIDUtils.generate(TraceIDUtils.get());
    try {
        ((Runnable) obj).run();
    } finally {
        TraceIDUtils.clear();
    }
    ```
3. com.yuckar.infra.base.trace.TraceIDFactory：个性化traceID生成函数
    - 示例
    ```
    public class AbstractTraceIDFactory implements TraceIDFactory {
    
        private final AtomicLong no = new AtomicLong(0L);
        private final String perfix;
        private final String no_format;
        private final long no_mod;
  
        @Override
        public final String generate() {
            return this.perfix + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")
                    + String.format(this.no_format, no.incrementAndGet() % this.no_mod);
        }
    
    }

    ```

### (17) utils：其他工具
1. com.yuckar.infra.base.utils.ClassUtils
2. com.yuckar.infra.base.utils.LockUtils：锁
3. com.yuckar.infra.base.utils.N_humanUtils：格式化
    - formatNumber
    - formatMills
    - formatMicros
    - formatNanos
    - formatByte
4. com.yuckar.infra.base.utils.N_zhUtils：数字转中文
5. com.yuckar.infra.base.utils.ProxyUtils：代理
6. com.yuckar.infra.base.utils.RetryUtils：重试
7. com.yuckar.infra.base.utils.RunUtils：异常处理
8. com.yuckar.infra.base.utils.StackUtils：线程栈分析

## 2. conf:配置
1. 说明
    配置与代码分离。
2. info
    - com.yuckar.infra.conf.info.CommonYconf<I, R>
    - com.yuckar.infra.conf.info.DatabaseYconf<I, R>
2. loadingcache
    - com.yuckar.infra.conf.loadingcache.KLoadingCache<K, V>
    - 示例
        ```
        KLoadingCacheHelper.wrap(String key, LoadingCache<K, V> loadingcache);
        ```
3. simple
    - com.yuckar.infra.conf.simple.SimpleYconf<T>
    - 示例
        ```
        SimpleYconf.of("crypto/digest/MD5", DigestInfo.class, info -> new Digest(info))
        ```
4. 实现方式
    - com.yuckar.infra.conf.yconfs.impl.FileYconfs<V>
    - com.yuckar.infra.conf.yconfs.impl.FileYconfsGroup<V, I>
    - com.yuckar.infra.conf.yconfs.impl.CuratorYconfs<V>
    - com.yuckar.infra.conf.yconfs.impl.CuratorYconfsGroup<V, I>
5. 项目配置
    - spi接口
        - com.yuckar.infra.conf.yconfs.context.YconfsContext
        - com.yuckar.infra.conf.yconfs.context.YconfsGroupContext
    - 示例
        ```
        package com.yuckar.apps.demo;

        import com.yuckar.apps.app.App;
        import com.yuckar.infra.conf.yconfs.Yconfs;
        import com.yuckar.infra.conf.yconfs.context.AbstractYconfsContext;
        import com.yuckar.infra.conf.yconfs.impl.FileYconfs;
        
        public class DemoYconfsContext extends AbstractYconfsContext {
        
            @Override
            public <I> Yconfs<I> newYconfs(Class<I> clazz) {
                return new FileYconfs<>(App.demo.app_confs(), clazz);
            }
        
            @Override
            public String pkg() {
                return "com.yuckar.apps.demo";
            }
        
        }

        ```

## 3. cluster:集群组件
- 接口
    - com.yuckar.infra.cluster.Cluster<R>
    - com.yuckar.infra.cluster.Master<R>
    - com.yuckar.infra.cluster.MasterCluster<R>
- Yconf
    - com.yuckar.infra.cluster.yconf.ClusterYconf<R, I, C>
    - com.yuckar.infra.cluster.yconf.ClusterGroupYconf<R, I, C>
    - com.yuckar.infra.cluster.yconf.MasterClusterYconf<R, I, C>
    - com.yuckar.infra.cluster.yconf.MasterYconf<R, I, C>
- 示例
    ```
    package com.yuckar.stock.base.smtp;

    import com.annimon.stream.function.Function;
    import com.yuckar.infra.cluster.info.InstanceInfo;
    import com.yuckar.infra.cluster.yconf.ClusterGroupYconf;
    import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
    import com.yuckar.infra.network.mail.sender.MailSmtp;
    import com.yuckar.infra.network.mail.sender.MailSmtpInfo;
    
    public enum StmpGroupYconf implements ClusterGroupYconf<MailSmtp, MailSmtpInfo, SmtpClusterInfo> {
        notify;
    
        @Override
        public String path() {
            return YconfsNamespaceUtils.common("network/" + name());
        }
    
        @Override
        public Function<InstanceInfo<MailSmtpInfo>, MailSmtp> mapper() {
            return ins -> new MailSmtp(ins.getInfo());
        }
    
    }
    
    StmpGroupYconf.notify.getResource().getResource().send(title, Lists.newArrayList(""),
                    subject, content);
    ```

## 4. code:代码自动化组件
1. com.yuckar.infra.code.model.GenerateModel
2. com.yuckar.infra.code.sql.GenerateSql

## 5. crypto:密码组件
1. 加解密
    - com.yuckar.infra.crypto.cipher.Decrypt
    - com.yuckar.infra.crypto.cipher.Encrypt
2. 摘要 Digest
    - com.yuckar.infra.crypto.digest.Digest
3. 摘要 Mac
    - com.yuckar.infra.crypto.mac.Mac
4. 签名与验证
    - com.yuckar.infra.crypto.signature.SignatureSign
    - com.yuckar.infra.crypto.signature.SignatureVertify
5. Yconf
    - com.yuckar.infra.crypto.yconf.DecryptYconf
    - com.yuckar.infra.crypto.yconf.EncryptYconf
    - com.yuckar.infra.crypto.yconf.DigestYconf
    - com.yuckar.infra.crypto.yconf.MacYconf
    - com.yuckar.infra.crypto.yconf.SignatureSignYconf
    - com.yuckar.infra.crypto.yconf.SignatureVertifyYconf

## 6. dlock:分布式锁组件
1. 接口 
    - com.yuckar.infra.dlock.DLock
        ```
        package com.yuckar.infra.dlock;

        import java.util.concurrent.TimeUnit;
        
        public interface DLock {
        
            void lock();
        
            boolean tryLock();
        
            boolean tryLock(long timeout, TimeUnit unit);
        
            void unlock();
        }

        ```
2. 实现
    - com.yuckar.infra.dlock.impl.FileDLock
    - com.yuckar.infra.dlock.impl.CuratorDLock
    - com.yuckar.infra.dlock.nolock.NoDLock
3. 项目配置
    - spi接口：com.yuckar.infra.dlock.context.DLockContext
    - 示例：
        ```
        package com.yuckar.stock.base.context;

        import com.yuckar.apps.app.App;
        import com.yuckar.infra.dlock.DLock;
        import com.yuckar.infra.dlock.context.AbstractDLockContext;
        import com.yuckar.infra.dlock.impl.FileDLock;
        
        public class StockDLockContext extends AbstractDLockContext {
        
            @Override
            public DLock newLock(String key) {
                return new FileDLock(key, App.stock.app_dlock());
            }
        
            public String[] pkgs() {
                return new String[] { "com.yuckar.stock" };
            }
        
        }
        Dlock lock = DLockFactory.getContext(clazz).get(key);
        lock.lock();
        ...
        lock.unlock();
        ```

## 7. monitor:监控组件
1. 启动
    ```
    com.yuckar.infra.monitor.startup.Monitor.startup();
    ```
2. 监控
    - SPI接口
        - com.yuckar.infra.monitor.IMonitor
        - com.yuckar.infra.monitor.mxbean.IMxbeanHandler<D>
    - monitor
        - com.yuckar.infra.monitor.mxbean.monitor.ClassLoadingMxbeanMonitor：类加载
        - com.yuckar.infra.monitor.mxbean.monitor.CompilationMxbeanMonitor：类编译
        - com.yuckar.infra.monitor.mxbean.monitor.GarbageCollectorMxbeanMonitor：垃圾收集
        - com.yuckar.infra.monitor.mxbean.monitor.MemoryManagerMxbeanMonitor：内存
        - com.yuckar.infra.monitor.mxbean.monitor.MemoryMxbeanMonitor：内存
        - com.yuckar.infra.monitor.mxbean.monitor.MemoryPoolMxbeanMonitor：内存
        - com.yuckar.infra.monitor.mxbean.monitor.OperatingSystemMxbeanMonitor：操作系统
        - com.yuckar.infra.monitor.mxbean.monitor.OperatingSystemMxbeanMonitor2：操作系统
        - com.yuckar.infra.monitor.mxbean.monitor.OperatingSystemUnixMxbeanMonitor：操作系统
        - com.yuckar.infra.monitor.mxbean.monitor.RuntimeMxbeanMonitor：运行相关
        - com.yuckar.infra.monitor.mxbean.monitor.ThreadMxbeanMonitor：线程相关

## 8. network：网络相关
1. ftp
    - com.yuckar.infra.network.ftp.KftpClient
    - com.yuckar.infra.network.ftp.KftphttpClient
    - com.yuckar.infra.network.ftp.KftpsClient
2. http
    - com.yuckar.infra.network.http.KhttpClient
    - com.yuckar.infra.network.http.KhttpAsyncClient
3. jsch
    - com.yuckar.infra.network.jsch.JschSftp
4. mail
    - com.yuckar.infra.network.mail.MailReceiver
    - com.yuckar.infra.network.mail.MailSender
    - com.yuckar.infra.network.mail.MailSmtp
5. okhttp
    - com.yuckar.infra.network.okhttp.OkhttpAsync
    - com.yuckar.infra.network.okhttp.OkhttpSync
6. retrofit
    - com.yuckar.infra.network.retrofit.RetrofitUtils
7. web
    - com.yuckar.infra.network.web.browser.WebBrowser
    - com.yuckar.infra.network.web.capture.WebChromeCapture
8. yconf
    - ftp
        - com.yuckar.infra.network.yconf.KftpClientYconf
        - com.yuckar.infra.network.yconf.KftphttpClientYconf
        - com.yuckar.infra.network.yconf.KftpsClientYconf
    - http
        - com.yuckar.infra.network.yconf.KhttpAsyncClientYconf
        - com.yuckar.infra.network.yconf.KhttpClientYconf
    - jsch
        - com.yuckar.infra.network.yconf.JschSftpYconf
    - mail
        - com.yuckar.infra.network.yconf.MailReceiverYconf
        - com.yuckar.infra.network.yconf.MailSenderYconf
        - com.yuckar.infra.network.yconf.MailSmtpYconf
    - okhttp
        - com.yuckar.infra.network.yconf.OkhttpAsyncYconf
        - com.yuckar.infra.network.yconf.OkhttpSyncYconf
    - web
        - com.yuckar.infra.network.yconf.WebBrowserYconf
        - com.yuckar.infra.network.yconf.WebCaptureYconf

## 13. runner
- com.yuckar.infra.runner.binlog.BinlogRunner
- com.yuckar.infra.runner.mq.kafka.KafkaRunner<K, V>
- com.yuckar.infra.runner.mq.rocket.RocketRunner
- com.yuckar.infra.runner.rpc.grpc.GrpcRunner
- com.yuckar.infra.runner.rpc.grpc.GrpcRunnerBindable
- com.yuckar.infra.runner.sch.ksch.KschRunner
- com.yuckar.infra.runner.sch.quatz.QuatzRunner
- com.yuckar.infra.runner.simple.SimpleRunner
- com.yuckar.infra.runner.server.RunnerServerMain

## 14. script
- com.yuckar.infra.script.utils.ScriptUtils

## 15. server
- com.yuckar.infra.server.jetty.JettyServer
- com.yuckar.infra.server.startup.Startable
- com.yuckar.infra.server.startup.StartableMain

## 16. storge
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

## 17. text
- pinyin
    - com.yuckar.infra.text.pinyin.PinyinUtils
- tpl
    - com.yuckar.infra.text.tpl.beetl.Beetl
    - com.yuckar.infra.text.tpl.enjoy.Enjoy
    - com.yuckar.infra.text.tpl.freemarker.Freemarker


## 19. trace
- 跟踪

# 三 Maven依赖
```
<dependency>
    <groupId>yuckar-infra</groupId>
    <artifactId>yuckar-infra-pom</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <type>pom</type>
</dependency>
```