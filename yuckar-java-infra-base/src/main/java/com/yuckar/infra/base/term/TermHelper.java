package com.yuckar.infra.base.term;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import com.yuckar.infra.base.function.ThrowableRunnable;
import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.base.trace.TraceIDUtils;
import com.yuckar.infra.base.utils.ClassUtils;
import com.yuckar.infra.base.utils.N_humanUtils;

public class TermHelper {

	public static void addFirstTerm(String module, ThrowableRunnable<? extends Throwable> runnable) {
		addTerm(module, terms.stream().sorted((a, b) -> a.getPriority() - b.getPriority()).findFirst()
				.map(t -> t.getPriority() - 10).orElseGet(() -> defTermsPriority.getAndAdd(10)), runnable);
	}

	public static void addTerm(String module, ThrowableRunnable<? extends Throwable> runnable) {
		addTerm(module, defTermsPriority.getAndAdd(10), runnable);
	}

	public static void addTerm(String module, int priority, ThrowableRunnable<? extends Throwable> runnable) {
		terms.add(new Term(module, priority, runnable));
	}

	public static boolean isStopping() {
		return stopping.get();
	}

	private static final Logger logger = LoggerUtils.logger(TermHelper.class);
	private static final AtomicBoolean stopping = new AtomicBoolean(false);
	private static final Set<Term> terms = Sets.newConcurrentHashSet();
	private static final AtomicInteger defTermsPriority = new AtomicInteger(0);
	static {
		SignalHelper.handle("TERM", signal -> {
			try {
				TraceIDUtils.generate();
				stopping.set(true);
				Stopwatch st = Stopwatch.createStarted();
				terms.stream().sorted((a, b) -> Integer.compare(a.getPriority(), b.getPriority())).forEach(term -> {
					Stopwatch stopwatch = Stopwatch.createStarted();
					try {
						term.getRunnable().run();
						logger.info("The term module:{} priority:{} run completely,elapsed:{} class:{}!!!",
								term.getModule(), term.getPriority(), N_humanUtils.formatMills(stopwatch),
								ClassUtils.simple_name(term.getRunnable().getClass()));
					} catch (Throwable e) {
						logger.error(String.format("The term module:%s priority:%s run wrongly,elapsed:%s class:%s!!!",
								term.getModule(), term.getPriority(), N_humanUtils.formatMills(stopwatch),
								ClassUtils.simple_name(term.getRunnable().getClass())), e);
					}
				});
				logger.info("The terms has been executed finishlly,elapsed:{}!!!",
						N_humanUtils.formatMills(st.elapsed(TimeUnit.MILLISECONDS)));
				TraceIDUtils.clear();
			} finally {
				System.exit(signal.getNumber());
			}
		});
	}

}
