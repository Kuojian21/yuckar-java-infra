package com.yuckar.infra.base.bean.builder;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class SetBeanBuilder<T> extends BeanBuilder<Set<T>> {

	public static <T> SetBeanBuilder<T> of() {
		return of(Sets.newLinkedHashSet());
	}

	public static <T> SetBeanBuilder<T> of(Set<T> bean) {
		return new SetBeanBuilder<T>(bean);
	}

	public SetBeanBuilder(Set<T> bean) {
		super(bean);
	}

	public SetBeanBuilder<T> add(@SuppressWarnings("unchecked") T... objs) {
		super.accept(bean -> bean.addAll(Lists.newArrayList(objs)));
		return this;
	}

	public SetBeanBuilder<T> add(List<T> objs) {
		super.accept(bean -> bean.addAll(objs));
		return this;
	}

}
