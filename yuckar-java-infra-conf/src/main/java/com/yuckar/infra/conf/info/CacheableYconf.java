package com.yuckar.infra.conf.info;

import org.slf4j.Logger;

import com.annimon.stream.function.Function;
import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.conf.Yconf;

public interface CacheableYconf<I, R> extends Yconf<R> {

	Logger logger = LoggerUtils.logger(CacheableYconf.class);

	String path();

	Function<I, R> mapper();

	default R get() {
		return CacheableYconfHolder.get(this);
	}

	default void refresh() {
		CacheableYconfHolder.refresh(this);
	}

}
