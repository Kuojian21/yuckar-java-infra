package com.yuckar.infra.base.term;

import com.yuckar.infra.base.function.ThrowableRunnable;

public class Term {
	private final String module;
	private final int priority;
	private final ThrowableRunnable<? extends Throwable> runnable;

	public Term(String module, int priority, ThrowableRunnable<? extends Throwable> runnable) {
		super();
		this.module = module;
		this.priority = priority;
		this.runnable = runnable;
	}

	public String getModule() {
		return module;
	}

	public int getPriority() {
		return priority;
	}

	public ThrowableRunnable<? extends Throwable> getRunnable() {
		return runnable;
	}

}
