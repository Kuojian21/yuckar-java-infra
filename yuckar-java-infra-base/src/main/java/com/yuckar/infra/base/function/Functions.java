package com.yuckar.infra.base.function;

public interface Functions {

	static <T> Function<T, T> identity() {
		return t -> t;
	}

	static interface Function<T, R>
			extends com.annimon.stream.function.Function<T, R>, com.google.common.base.Function<T, R> {

	}

}
