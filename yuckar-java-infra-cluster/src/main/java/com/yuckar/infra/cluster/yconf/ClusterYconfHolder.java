package com.yuckar.infra.cluster.yconf;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.base.utils.TypeMapperUtils;
import com.yuckar.infra.cluster.Cluster;
import com.yuckar.infra.cluster.impl.ClusterFactory;
import com.yuckar.infra.cluster.info.ClusterInfo;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.context.YconfsFactory;

@SuppressWarnings("unchecked")
class ClusterYconfHolder {

	static final ConcurrentMap<ClusterYconf<?, ?, ?>, LazySupplier<?>> yconfMap = Maps.newConcurrentMap();

	static <R, I, C extends ClusterInfo<I>> Cluster<R> get(ClusterYconf<R, I, C> info) {
		return (Cluster<R>) yconfMap.computeIfAbsent(info, k -> LazySupplier.wrap(() -> {
			Yconfs<C> yconfs = YconfsFactory.getContext(info.getClass())
					.getYconfs((Class<C>) TypeMapperUtils.mapper(info.getClass()).get(ClusterYconf.class)
							.get(ClusterYconf.class.getTypeParameters()[2]));
			return ClusterFactory.cluster(yconfs, info.path(), info.mapper(), info::close);
		})).get();
	}

}
