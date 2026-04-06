package com.yuckar.infra.base.bean.builder;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BeanBuilder<T> {

	public static <T> BeanBuilder<T> of(Supplier<T> bean) {
		return of(bean.get());
	}

	public static <T> BeanBuilder<T> of(T bean) {
		return new BeanBuilder<T>(bean);
	}

	protected final T bean;

	public BeanBuilder(T bean) {
		super();
		this.bean = bean;
	}

	public final BeanBuilder<T> accept(Consumer<T> consumer) {
		consumer.accept(bean);
		return this;
	}

	public final T build() {
		return bean;
	}

}
