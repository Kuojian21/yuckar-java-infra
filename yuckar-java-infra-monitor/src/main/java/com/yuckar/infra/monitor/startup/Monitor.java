package com.yuckar.infra.monitor.startup;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Uninterruptibles;
import com.yuckar.infra.base.lazy.LazyRunnable;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.base.spi.SpiFactory;
import com.yuckar.infra.base.trace.TraceIDUtils;
import com.yuckar.infra.base.utils.RunUtils;
import com.yuckar.infra.monitor.IMonitor;

public class Monitor {

	private static final Logger logger = LoggerUtils.logger(Monitor.class);
	private static final LazySupplier<Set<IMonitor>> monitors = LazySupplier
			.wrap(() -> Sets.newConcurrentHashSet(SpiFactory.load(IMonitor.class)));
	private static final LazyRunnable startup = LazyRunnable.wrap(() -> {
		Thread thread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				RunUtils.catching(() -> {
					Uninterruptibles.sleepUninterruptibly(1, TimeUnit.MINUTES);
					TraceIDUtils.generate();
					monitors.get().forEach(m -> {
						m.monitor();
					});
					TraceIDUtils.clear();
				});
			}
		}, "monitor");
		thread.setDaemon(true);
		thread.start();
	});

	public static void start() {
		startup.run();
	}

	public static <M extends IMonitor> void register(Class<M> clazz) {
		try {
			register(clazz.getConstructor(new Class<?>[] {}).newInstance(new Object[] {}));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			logger.error("", e);
		}
	}

	public static <M extends IMonitor> void register(IMonitor monitor) {
		monitors.get().add(monitor);
	}

}
