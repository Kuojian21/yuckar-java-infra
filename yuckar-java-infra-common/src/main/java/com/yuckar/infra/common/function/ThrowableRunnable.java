package com.yuckar.infra.common.function;

@FunctionalInterface
public interface ThrowableRunnable<X extends Throwable> {

	void run() throws X;

}
