package com.yuckar.infra.common.bean.builder;

import java.util.List;

import com.google.common.collect.Lists;

public class ListBeanBuilder<T> extends BeanBuilder<List<T>> {

	public static <T> ListBeanBuilder<T> of() {
		return of(Lists.newArrayList());
	}

	public static <T> ListBeanBuilder<T> of(List<T> bean) {
		return new ListBeanBuilder<T>(bean);
	}

	public ListBeanBuilder(List<T> bean) {
		super(bean);
	}

	public ListBeanBuilder<T> add(@SuppressWarnings("unchecked") T... objs) {
		super.accept(bean -> bean.addAll(Lists.newArrayList(objs)));
		return this;
	}

	public ListBeanBuilder<T> add(List<T> objs) {
		super.accept(bean -> bean.addAll(objs));
		return this;
	}

}
