package com.yuckar.infra.cluster.yconf;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.base.utils.TypeMapperUtils;
import com.yuckar.infra.cluster.Cluster;
import com.yuckar.infra.cluster.impl.ClusterFactory;
import com.yuckar.infra.cluster.info.ClusterInfo;
import com.yuckar.infra.conf.yconfs.YconfsGroup;
import com.yuckar.infra.conf.yconfs.context.YconfsGroupFactory;

@SuppressWarnings("unchecked")
class ClusterGroupYconfHolder {

	static final ConcurrentMap<ClusterGroupYconf<?, ?, ?>, LazySupplier<?>> yconfMap = Maps.newConcurrentMap();

	static <R, I, C extends ClusterInfo<I>> Cluster<R> get(ClusterGroupYconf<R, I, C> info) {
		return (Cluster<R>) yconfMap.computeIfAbsent(info, k -> LazySupplier.wrap(() -> {
			Class<I> i_clazz = (Class<I>) TypeMapperUtils.mapper(info.getClass()).get(ClusterGroupYconf.class)
					.get(ClusterGroupYconf.class.getTypeParameters()[1]);
			Class<C> c_clazz = (Class<C>) TypeMapperUtils.mapper(info.getClass()).get(ClusterGroupYconf.class)
					.get(ClusterGroupYconf.class.getTypeParameters()[2]);
			YconfsGroup<C, I> yconfs = YconfsGroupFactory.getContext(info.getClass()).getYconfsGroup(c_clazz, i_clazz);
			return ClusterFactory.cluster(yconfs, info.path(), info.mapper(), info::close);
		})).get();
	}

}