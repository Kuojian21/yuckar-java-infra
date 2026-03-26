package com.yuckar.infra.runner.common;

import com.yuckar.infra.register.context.RegisterContext;
import com.yuckar.infra.register.context.RegisterFactory;
import com.yuckar.infra.runner.Runner;

public abstract class RunnerHolder<R extends Runner> implements AutoCloseable {

	private final R runner;
	private final RegisterContext context;

	protected RunnerHolder(R runner) {
		this(runner, RegisterFactory.getContext(runner.getClass()));
	}

	protected RunnerHolder(R runner, RegisterContext context) {
		super();
		this.runner = runner;
		this.context = context;
	}

	public R runner() {
		return this.runner;
	}

	public RegisterContext context() {
		return this.context;
	}

}
