package com.yuckar.infra.cluster.yconf;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.base.utils.TypeMapperUtils;
import com.yuckar.infra.cluster.MasterCluster;
import com.yuckar.infra.cluster.impl.MasterClusterFactory;
import com.yuckar.infra.cluster.info.MasterClusterInfo;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.context.YconfsFactory;

@SuppressWarnings("unchecked")
class MasterClusterYconfHolder {

	static final ConcurrentMap<MasterClusterYconf<?, ?, ?>, LazySupplier<?>> yconfMap = Maps.newConcurrentMap();

	static <R, I, C extends MasterClusterInfo<I>> MasterCluster<R> get(MasterClusterYconf<R, I, C> info) {
		return (MasterCluster<R>) yconfMap.computeIfAbsent(info, k -> LazySupplier.wrap(() -> {
			Yconfs<C> yconfs = YconfsFactory.getContext(info.getClass())
					.getYconfs((Class<C>) TypeMapperUtils.mapper(info.getClass()).get(MasterClusterYconf.class)
							.get(MasterClusterYconf.class.getTypeParameters()[2]));
			return MasterClusterFactory.cluster(yconfs, info.path(), info.mapper(), info::close);
		})).get();
	}

}
