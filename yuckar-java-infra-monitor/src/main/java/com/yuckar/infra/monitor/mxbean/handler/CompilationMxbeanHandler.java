package com.yuckar.infra.monitor.mxbean.handler;

import java.lang.management.CompilationMXBean;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.yuckar.infra.base.utils.N_humanUtils;
import com.yuckar.infra.monitor.mxbean.holder.CompilationMxbeanHolder;

public class CompilationMxbeanHandler extends AbstractMxbeanHandler<CompilationMXBean, CompilationMxbeanHolder> {

	@Override
	public Map<String, Object> doHandle(CompilationMXBean bean) {
		return ImmutableMap.of("totalCompilationTime", N_humanUtils.formatMills(bean.getTotalCompilationTime()));
	}

}
