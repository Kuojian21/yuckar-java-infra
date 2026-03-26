package com.yuckar.infra.cluster.resource;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;
import com.yuckar.infra.cluster.Cluster;
import com.yuckar.infra.cluster.impl.ClusterFactory;
import com.yuckar.infra.cluster.info.ClusterInfo;
import com.yuckar.infra.common.lazy.LazySupplier;
import com.yuckar.infra.common.utils.TypeMapperUtils;
import com.yuckar.infra.register.Register;
import com.yuckar.infra.register.context.RegisterFactory;

@SuppressWarnings("unchecked")
class ClusterResourceHolder {

	static final ConcurrentMap<ClusterResource<?, ?, ?>, LazySupplier<?>> resources = Maps.newConcurrentMap();

	static <R, I, C extends ClusterInfo<I>> Cluster<R> get(ClusterResource<R, I, C> info) {
		return (Cluster<R>) resources.computeIfAbsent(info, k -> LazySupplier.wrap(() -> {
			Register<C> register = RegisterFactory.getContext(info.getClass())
					.getRegister((Class<C>) TypeMapperUtils.mapper(info.getClass()).get(ClusterResource.class)
							.get(ClusterResource.class.getTypeParameters()[2]));
			return ClusterFactory.cluster(register, info.ID(), info.mapper(), info::close);
		})).get();
	}

}
