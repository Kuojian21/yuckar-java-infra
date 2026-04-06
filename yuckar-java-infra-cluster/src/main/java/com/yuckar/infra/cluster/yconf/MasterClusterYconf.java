package com.yuckar.infra.cluster.yconf;

import org.slf4j.Logger;

import com.annimon.stream.function.Function;
import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.cluster.MasterCluster;
import com.yuckar.infra.cluster.info.InstanceInfo;
import com.yuckar.infra.cluster.info.MasterClusterInfo;

public interface MasterClusterYconf<R, I, C extends MasterClusterInfo<I>> {

	Logger logger = LoggerUtils.logger(MasterClusterYconf.class);

	String path();

	Function<InstanceInfo<I>, R> mapper();

	default MasterCluster<R> getResource() {
		return MasterClusterYconfHolder.get(this);
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
