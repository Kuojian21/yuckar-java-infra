package com.yuckar.infra.runner.common;

import com.yuckar.infra.conf.yconfs.context.YconfsContext;
import com.yuckar.infra.conf.yconfs.context.YconfsFactory;
import com.yuckar.infra.runner.Runner;

public abstract class RunnerHolder<R extends Runner> implements AutoCloseable {

	private final R runner;
	private final YconfsContext context;

	protected RunnerHolder(R runner) {
		this(runner, YconfsFactory.getContext(runner.getClass()));
	}

	protected RunnerHolder(R runner, YconfsContext context) {
		super();
		this.runner = runner;
		this.context = context;
	}

	public R runner() {
		return this.runner;
	}

	public YconfsContext context() {
		return this.context;
	}

}
