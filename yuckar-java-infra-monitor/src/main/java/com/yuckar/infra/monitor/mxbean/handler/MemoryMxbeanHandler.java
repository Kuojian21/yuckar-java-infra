package com.yuckar.infra.monitor.mxbean.handler;

import java.lang.management.MemoryMXBean;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.yuckar.infra.monitor.mxbean.holder.MemoryMxbeanHolder;
import com.yuckar.infra.monitor.mxbean.utils.MxbeanUtils;

public class MemoryMxbeanHandler extends AbstractMxbeanHandler<MemoryMXBean, MemoryMxbeanHolder> {

	@Override
	public Map<String, Object> doHandle(MemoryMXBean bean) {
		return ImmutableMap.of("objectPendingFinalizationCount", bean.getObjectPendingFinalizationCount(), //
				"heapMemoryUsage", MxbeanUtils.toMap(bean.getHeapMemoryUsage()), //
				"nonHeapMemoryUsage", MxbeanUtils.toMap(bean.getNonHeapMemoryUsage()) //
		);
	}

}
