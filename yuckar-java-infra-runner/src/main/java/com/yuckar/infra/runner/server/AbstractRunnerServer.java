package com.yuckar.infra.runner.server;

import java.util.concurrent.ThreadFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yuckar.infra.base.lazy.LazyRunnable;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.base.utils.RunUtils;
import com.yuckar.infra.dlock.DLock;
import com.yuckar.infra.dlock.context.DLockFactory;
import com.yuckar.infra.dlock.utils.DLockNamespaceUtils;
import com.yuckar.infra.runner.Runner;
import com.yuckar.infra.runner.RunnerArgs;

public abstract class AbstractRunnerServer<R extends Runner> implements RunnerServer<R> {

	private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat("runner-dlock-%d")
			.setDaemon(false).build();

	protected final Logger logger = LoggerUtils.logger(this.getClass());

	protected final LazySupplier<CommandLine> commandLine = LazySupplier.wrap(() -> RunUtils.throwing(() -> {
		return new DefaultParser().parse(this.args_options(), RunnerArgs.args().prefix(this.args_prefix()), true);
	}));
	private final LazyRunnable init = LazyRunnable.wrap(AbstractRunnerServer.this::init);

	@Override
	public final void run(R runner) {
		init.run();
		THREAD_FACTORY.newThread(() -> {
			if (nlock() && StringUtils.isNotEmpty(runner.ID())) {
				DLock lock = DLockFactory.getContext(runner.getClass())
						.getLock(DLockNamespaceUtils.runner(runner.module() + "/" + runner.ID()));
				lock.lock();
			}
			RunUtils.throwing(() -> this.doRun(runner));
		}).start();
	}

	protected void init() {

	}

	protected String args_prefix() {
		String prefix = this.getClass().getName().replace(".server." + this.getClass().getSimpleName(), "");
		return prefix.substring(prefix.lastIndexOf(".") + 1);
	}

	protected Options args_options() {
		return new Options();
	}

	protected abstract void doRun(R runner) throws Exception;

	protected abstract boolean nlock();

}
