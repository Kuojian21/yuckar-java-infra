package com.yuckar.infra.cluster.resource;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;
import com.yuckar.infra.cluster.Master;
import com.yuckar.infra.cluster.impl.MasterFactory;
import com.yuckar.infra.cluster.info.MasterInfo;
import com.yuckar.infra.common.lazy.LazySupplier;
import com.yuckar.infra.common.utils.TypeMapperUtils;
import com.yuckar.infra.register.Register;
import com.yuckar.infra.register.context.RegisterFactory;

@SuppressWarnings("unchecked")
class MasterResourceHolder {

	static final ConcurrentMap<MasterResource<?, ?, ?>, LazySupplier<?>> resources = Maps.newConcurrentMap();

	static <R, I, C extends MasterInfo<I>> Master<R> get(MasterResource<R, I, C> info) {
		return (Master<R>) resources.computeIfAbsent(info, k -> LazySupplier.wrap(() -> {
			Register<C> register = RegisterFactory.getContext(info.getClass())
					.getRegister((Class<C>) TypeMapperUtils.mapper(info.getClass()).get(MasterResource.class)
							.get(MasterResource.class.getTypeParameters()[2]));
			return MasterFactory.master(register, info.path(), info.mapper(), info::close);
		})).get();
	}

}
