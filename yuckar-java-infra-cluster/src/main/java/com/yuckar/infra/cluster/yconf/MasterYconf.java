package com.yuckar.infra.cluster.yconf;


import org.slf4j.Logger;

import com.annimon.stream.function.Function;
import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.cluster.Master;
import com.yuckar.infra.cluster.info.InstanceInfo;
import com.yuckar.infra.cluster.info.MasterInfo;

public interface MasterYconf<R, I, C extends MasterInfo<I>> {

	Logger logger = LoggerUtils.logger(MasterYconf.class);

	String path();

	Function<InstanceInfo<I>, R> mapper();

	default Master<R> getResource() {
		return MasterYconfHolder.get(this);
	}

	default void close(R resource) {
		if (resource != null && resource instanceof AutoCloseable) {
			try {
				((AutoCloseable) resource).close();
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

}
