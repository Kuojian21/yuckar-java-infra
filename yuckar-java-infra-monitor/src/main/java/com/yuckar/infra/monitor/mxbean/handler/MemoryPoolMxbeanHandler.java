package com.yuckar.infra.monitor.mxbean.handler;

import java.lang.management.MemoryPoolMXBean;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.yuckar.infra.monitor.mxbean.holder.MemoryPoolMxbeanHolder;
import com.yuckar.infra.monitor.mxbean.utils.MxbeanUtils;

public class MemoryPoolMxbeanHandler extends AbstractMxbeanHandler<MemoryPoolMXBean, MemoryPoolMxbeanHolder> {

	@Override
	public Map<String, Object> doHandle(MemoryPoolMXBean bean) {
		return ImmutableMap.of("type", bean.getType(), //
				"memoryManagerNames", bean.getMemoryManagerNames(), //
				"usage", MxbeanUtils.toMap(bean.getUsage()), //
				"collectionUsage", MxbeanUtils.toMap(bean.getCollectionUsage()), //
				"peakUsage", MxbeanUtils.toMap(bean.getPeakUsage()) //
		);
	}

}
