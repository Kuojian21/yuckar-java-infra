package com.yuckar.infra.conf.info;

import com.annimon.stream.function.Function;
import com.yuckar.infra.base.utils.RunUtils;
import com.yuckar.infra.base.utils.TypeMapperUtils;

public interface CacheableInfoYconf<I, R> extends CacheableYconf<I, R> {

	@SuppressWarnings("unchecked")
	default Function<I, R> mapper() {
		Class<I> i_clazz = (Class<I>) TypeMapperUtils.mapper(this.getClass()).get(CacheableInfoYconf.class)
				.get(CacheableInfoYconf.class.getTypeParameters()[0]);
		Class<R> r_clazz = (Class<R>) TypeMapperUtils.mapper(this.getClass()).get(CacheableInfoYconf.class)
				.get(CacheableInfoYconf.class.getTypeParameters()[1]);
		return info -> RunUtils.throwing(
				() -> r_clazz.getDeclaredConstructor(new Class<?>[] { i_clazz }).newInstance(new Object[] { info }));
	}

}
