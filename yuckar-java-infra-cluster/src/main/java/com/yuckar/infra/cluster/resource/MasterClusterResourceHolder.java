package com.yuckar.infra.cluster.resource;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;
import com.yuckar.infra.cluster.MasterCluster;
import com.yuckar.infra.cluster.impl.MasterClusterFactory;
import com.yuckar.infra.cluster.info.MasterClusterInfo;
import com.yuckar.infra.common.lazy.LazySupplier;
import com.yuckar.infra.common.utils.TypeMapperUtils;
import com.yuckar.infra.register.Register;
import com.yuckar.infra.register.context.RegisterFactory;

@SuppressWarnings("unchecked")
class MasterClusterResourceHolder {

	static final ConcurrentMap<MasterClusterResource<?, ?, ?>, LazySupplier<?>> resources = Maps.newConcurrentMap();

	static <R, I, C extends MasterClusterInfo<I>> MasterCluster<R> get(MasterClusterResource<R, I, C> info) {
		return (MasterCluster<R>) resources.computeIfAbsent(info, k -> LazySupplier.wrap(() -> {
			Register<C> register = RegisterFactory.getContext(info.getClass())
					.getRegister((Class<C>) TypeMapperUtils.mapper(info.getClass()).get(MasterClusterResource.class)
							.get(MasterClusterResource.class.getTypeParameters()[2]));
			return MasterClusterFactory.cluster(register, info.ID(), info.mapper(), info::close);
		})).get();
	}

}
