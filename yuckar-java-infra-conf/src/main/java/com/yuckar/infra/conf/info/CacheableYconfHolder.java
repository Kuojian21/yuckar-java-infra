package com.yuckar.infra.conf.info;

import java.util.concurrent.ConcurrentMap;

import com.annimon.stream.Optional;
import com.google.common.collect.Maps;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.base.utils.TypeMapperUtils;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.context.YconfsFactory;

@SuppressWarnings("unchecked")
class CacheableYconfHolder {

	private static final ConcurrentMap<CacheableYconf<?, ?>, LazySupplier<?>> yconfMap = Maps.newConcurrentMap();

	static <I, R> R get(CacheableYconf<I, R> info) {
		LazySupplier<R> yconf = (LazySupplier<R>) yconfMap.get(info);
		if (yconf == null) {
			Yconfs<I> yconfs = YconfsFactory.getContext(info.getClass())
					.getYconfs((Class<I>) TypeMapperUtils.mapper(info.getClass()).get(CacheableYconf.class)
							.get(CacheableYconf.class.getTypeParameters()[0]));
			if (yconfMap.putIfAbsent(info, LazySupplier.wrap(() -> {
				return info.mapper().apply(yconfs.get(info.path()));
			})) == null) {
				yconfs.addListener(info.path(), event -> {
					refresh(info);
				});
			}
			yconf = (LazySupplier<R>) yconfMap.get(info);
		}
		return yconf.get();
	}

	static <I, R> void refresh(CacheableYconf<I, R> info) {
		Optional.ofNullable(yconfMap.get(info)).ifPresent(lr -> lr.refresh(r -> {
			if (r instanceof AutoCloseable) {
				((AutoCloseable) r).close();
			}
		}));
	}

}
