package com.yuckar.infra.monitor.mxbean.monitor;

import static java.util.stream.Collectors.toList;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.PlatformManagedObject;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.yuckar.infra.common.bean.builder.MapBeanBuilder;
import com.yuckar.infra.common.scanner.ClazzScanner;
import com.yuckar.infra.common.spi.ParamSpiFactory;
import com.yuckar.infra.common.utils.RunUtils;
import com.yuckar.infra.common.utils.TypeMapperUtils;
import com.yuckar.infra.monitor.IMonitor;
import com.yuckar.infra.monitor.mxbean.IMxbeanHandler;
import com.yuckar.infra.monitor.mxbean.IMxbeanHolder;
import com.yuckar.infra.monitor.mxbean.handler.AbstractMxbeanHandler;
import com.yuckar.infra.monitor.mxbean.holder.AbstractMxbeanHolder;
import com.sun.management.UnixOperatingSystemMXBean;

public abstract class AbstractMxbeanMonitor<D extends PlatformManagedObject> implements IMonitor {

	private static final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
	private static final Map<Class<?>, String> mxbean_name_map;
	private static final Map<Class<?>, Class<?>> mxbean_holder_map;
	private static final Map<Class<?>, Class<?>> mxbean_handler_map;

	private static final ParamSpiFactory<?> factory;

	static {
		Stream.of(server.getDomains()).forEach(domain -> logger.info("domain:{}", domain));
		mxbean_name_map = MapBeanBuilder.<Class<?>, String>of()
				.put(ClassLoadingMXBean.class, ManagementFactory.CLASS_LOADING_MXBEAN_NAME)
				.put(CompilationMXBean.class, ManagementFactory.COMPILATION_MXBEAN_NAME)
				.put(OperatingSystemMXBean.class, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME)
				.put(com.sun.management.OperatingSystemMXBean.class, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME)
				.put(UnixOperatingSystemMXBean.class, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME)
				.put(RuntimeMXBean.class, ManagementFactory.RUNTIME_MXBEAN_NAME)
				.put(ThreadMXBean.class, ManagementFactory.THREAD_MXBEAN_NAME)
				.put(GarbageCollectorMXBean.class, ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE)
				.put(MemoryMXBean.class, ManagementFactory.MEMORY_MXBEAN_NAME)
				.put(MemoryManagerMXBean.class, ManagementFactory.MEMORY_MANAGER_MXBEAN_DOMAIN_TYPE)
				.put(MemoryPoolMXBean.class, ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE) //
				.build();
		mxbean_holder_map = Stream.of(ClazzScanner
				.of(cls -> !AbstractMxbeanHolder.class.equals(cls) && AbstractMxbeanHolder.class.isAssignableFrom(cls))
				.scan("com.yuckar.infra.monitor.mxbean").get()).collect(Collectors.toMap(cls -> {
					return (Class<?>) TypeMapperUtils.mapper(cls).get(AbstractMxbeanHolder.class)
							.get(AbstractMxbeanHolder.class.getTypeParameters()[0]);
				}, cls -> cls));
		mxbean_handler_map = Stream.of(ClazzScanner.of(
				cls -> !AbstractMxbeanHandler.class.equals(cls) && AbstractMxbeanHandler.class.isAssignableFrom(cls))
				.scan("com.yuckar.infra.monitor.mxbean").get()).collect(Collectors.toMap(cls -> cls, cls -> {
					return (Class<?>) TypeMapperUtils.mapper(cls).get(AbstractMxbeanHandler.class)
							.get(AbstractMxbeanHandler.class.getTypeParameters()[1]);
				}));
		factory = ParamSpiFactory.of(IMxbeanHandler.class);

	}

	private final Class<D> mxbean;
	private final String mxbean_name;
	private final List<IMxbeanHandler<IMxbeanHolder>> mxbean_handlers;

	@SuppressWarnings("unchecked")
	protected AbstractMxbeanMonitor() {
		this.mxbean = (Class<D>) TypeMapperUtils.mapper(this.getClass()).get(AbstractMxbeanMonitor.class)
				.get(AbstractMxbeanMonitor.class.getTypeParameters()[0]);
		this.mxbean_name = mxbean_name_map.get(mxbean);
		this.mxbean_handlers = Optional
				.ofNullable((List<IMxbeanHandler<IMxbeanHolder>>) factory
						.getList((Class<? extends IMxbeanHolder>) mxbean_holder_map.get(this.mxbean)))
				.filter(l -> !l.isEmpty()).orElseGet(() -> {
					return Stream.of(mxbean_handler_map)
							.filter(entry -> entry.getValue().equals(mxbean_holder_map.get(mxbean)))
							.map(Map.Entry::getKey)
							.map(cls -> (IMxbeanHandler<IMxbeanHolder>) RunUtils.catching(
									() -> cls.getDeclaredConstructor(new Class<?>[] {}).newInstance(new Object[] {})))
							.toList();
				});
	}

	public final void monitor() {
		List<D> mxbeans = RunUtils.catching(() -> server.queryNames(new ObjectName(mxbean_name + ",*"), null).stream()
				.map(objectName -> RunUtils.throwing(
						() -> ManagementFactory.newPlatformMXBeanProxy(server, objectName.toString(), mxbean)))
				.collect(toList()));
		IMxbeanHolder bean = (IMxbeanHolder) RunUtils.catching(() -> mxbean_holder_map.get(mxbean)
				.getConstructor(new Class<?>[] { List.class }).newInstance(new Object[] { mxbeans }));
		mxbean_handlers.forEach(handler -> handler.handle(bean));
	}
}
