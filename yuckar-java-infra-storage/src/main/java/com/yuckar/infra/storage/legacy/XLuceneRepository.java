package com.yuckar.infra.storage.legacy;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.annimon.stream.Stream;
import com.google.common.collect.Sets;
import com.yuckar.infra.base.lazy.LazyRunnable;
import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.base.term.TermHelper;
import com.yuckar.infra.base.thread.KrExecutorService;
import com.yuckar.infra.base.thread.KrExecutorServiceInfo;
import com.yuckar.infra.base.thread.KrExecutors;
import com.yuckar.infra.base.utils.StackUtils;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.context.YconfsFactory;

public class XLuceneRepository {

	private final Logger logger = LoggerUtils.logger(getClass());
	private final KrExecutorService service = KrExecutors.newExecutor(new KrExecutorServiceInfo());
	private final Set<String> keys = Sets.newConcurrentHashSet();

	private final LazyRunnable notify;

	public XLuceneRepository() {
		Yconfs<Long> yconfs = YconfsFactory.getContext(StackUtils.firstBusinessInvokerClassname())
				.getYconfs(Long.class);
		Runnable notifyTask = () -> {
			try {
				Stream.of(keys).forEach(key -> {
					keys.remove(key);
					yconfs.set(key, System.currentTimeMillis());
				});
			} catch (Throwable e) {
				logger.error("", e);
			}
		};
		notify = LazyRunnable.wrap(() -> {
			Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(notifyTask, 5, 5, TimeUnit.SECONDS);
			TermHelper.addTerm("lucene", () -> {
				service.shutdownBlocking();
				notifyTask.run();
			});
		});
	}

	public void execute(String key, Runnable runnable) {
		notify.run();
		this.service.execute(() -> {
			runnable.run();
			this.keys.add(key);
		});
	}

}
