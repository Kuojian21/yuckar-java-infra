package com.yuckar.infra.conf.yconfs.context;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;
import com.yuckar.infra.conf.yconfs.YconfsGroup;

public abstract class AbstractYconfsGroupContext implements YconfsGroupContext {

	private final ConcurrentMap<Class<?>, YconfsGroup<?, ?>> yconfsGroupMap = Maps.newConcurrentMap();

	@SuppressWarnings("unchecked")
	@Override
	public final <V, I> YconfsGroup<V, I> getYconfsGroup(Class<V> vclass, Class<I> clazz) {
		return (YconfsGroup<V, I>) yconfsGroupMap.computeIfAbsent(vclass, cl -> newYconfsGroup(vclass, clazz));
	}

	public abstract <V, I> YconfsGroup<V, I> newYconfsGroup(Class<V> vclass, Class<I> clazz);

}