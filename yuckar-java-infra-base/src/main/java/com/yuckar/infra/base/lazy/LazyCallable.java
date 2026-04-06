package com.yuckar.infra.base.lazy;

import java.util.concurrent.Callable;

public class LazyCallable<T> extends LazySupplier<T> implements Callable<T> {

	public static <T> LazyCallable<T> wrap(Callable<T> callbable) {
		return new LazyCallable<T>(callbable);
	}

	public LazyCallable(Callable<T> delegate) {
		super(() -> {
			try {
				return delegate.call();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	@Override
	public T call() throws Exception {
		return super.get();
	}

}
