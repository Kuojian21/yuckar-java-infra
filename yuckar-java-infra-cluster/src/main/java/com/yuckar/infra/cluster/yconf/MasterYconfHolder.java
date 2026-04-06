package com.yuckar.infra.cluster.yconf;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.base.utils.TypeMapperUtils;
import com.yuckar.infra.cluster.Master;
import com.yuckar.infra.cluster.impl.MasterFactory;
import com.yuckar.infra.cluster.info.MasterInfo;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.context.YconfsFactory;

@SuppressWarnings("unchecked")
class MasterYconfHolder {

	static final ConcurrentMap<MasterYconf<?, ?, ?>, LazySupplier<?>> yconfMap = Maps.newConcurrentMap();

	static <R, I, C extends MasterInfo<I>> Master<R> get(MasterYconf<R, I, C> info) {
		return (Master<R>) yconfMap.computeIfAbsent(info, k -> LazySupplier.wrap(() -> {
			Yconfs<C> yconfs = YconfsFactory.getContext(info.getClass())
					.getYconfs((Class<C>) TypeMapperUtils.mapper(info.getClass()).get(MasterYconf.class)
							.get(MasterYconf.class.getTypeParameters()[2]));
			return MasterFactory.master(yconfs, info.path(), info.mapper(), info::close);
		})).get();
	}

}
