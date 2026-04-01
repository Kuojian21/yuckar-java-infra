package com.yuckar.infra.cluster.resource;

import org.slf4j.Logger;

import com.annimon.stream.function.Function;
import com.yuckar.infra.cluster.Cluster;
import com.yuckar.infra.cluster.info.ClusterInfo;
import com.yuckar.infra.cluster.info.InstanceInfo;
import com.yuckar.infra.common.logger.LoggerUtils;

public interface ClusterResource<R, I, C extends ClusterInfo<I>> {

	Logger logger = LoggerUtils.logger(ClusterResource.class);

	String path();

	Function<InstanceInfo<I>, R> mapper();

	default Cluster<R> getResource() {
		return ClusterResourceHolder.get(this);
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
