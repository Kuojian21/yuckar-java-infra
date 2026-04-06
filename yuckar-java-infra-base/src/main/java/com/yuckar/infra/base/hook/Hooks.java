package com.yuckar.infra.base.hook;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.yuckar.infra.base.function.ThrowableRunnable;
import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.base.trace.TraceIDUtils;
import com.yuckar.infra.base.utils.ClassUtils;
import com.yuckar.infra.base.utils.N_humanUtils;

public class Hooks {

	public static Hooks of(String module) {
		return new Hooks(module);
	}

	private final Logger logger = LoggerUtils.logger(this.getClass());
	private final List<ThrowableRunnable<? extends Throwable>> hooks = Lists.newCopyOnWriteArrayList();

	public Hooks(String module) {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				Stopwatch st = Stopwatch.createStarted();
				TraceIDUtils.generate();
				AtomicInteger no = new AtomicInteger(0);
				hooks.forEach(hook -> {
					Stopwatch stopwatch = Stopwatch.createStarted();
					no.incrementAndGet();
					try {
						hook.run();
						logger.debug("The hook:{}-{} run completely,elapsed:{} class:{}!!!", module, no.get(),
								N_humanUtils.formatMills(stopwatch), ClassUtils.simple_name(hook.getClass()));
					} catch (Throwable e) {
						logger.error(
								String.format("The hook:%s-%s run wrongly,elapsed:%s class:%s!!!", module, no.get(),
										N_humanUtils.formatMills(stopwatch), ClassUtils.simple_name(hook.getClass())),
								e);
					}
				});
				TraceIDUtils.clear();
				logger.info("The hooks:{} has been executed finishlly,elapsed:{}!!!", module,
						N_humanUtils.formatMills(st.elapsed(TimeUnit.MILLISECONDS)));
			}
		}));
	}

	public void add(ThrowableRunnable<? extends Throwable> hook) {
		this.hooks.add(hook);
	}

}
