package com.yuckar.infra.cluster.yconf;

import org.slf4j.Logger;

import com.annimon.stream.function.Function;
import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.cluster.Cluster;
import com.yuckar.infra.cluster.info.ClusterInfo;
import com.yuckar.infra.cluster.info.InstanceInfo;

public interface ClusterGroupYconf<R, I, C extends ClusterInfo<I>> {

	Logger logger = LoggerUtils.logger(ClusterGroupYconf.class);

	String path();

	Function<InstanceInfo<I>, R> mapper();

	default Cluster<R> getResource() {
		return ClusterGroupYconfHolder.get(this);
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
