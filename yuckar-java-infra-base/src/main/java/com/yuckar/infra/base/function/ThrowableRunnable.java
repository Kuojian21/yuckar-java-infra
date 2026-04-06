package com.yuckar.infra.base.function;

@FunctionalInterface
public interface ThrowableRunnable<X extends Throwable> {

	void run() throws X;

}
