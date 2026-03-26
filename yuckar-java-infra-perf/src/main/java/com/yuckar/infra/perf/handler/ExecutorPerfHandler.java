package com.yuckar.infra.perf.handler;

import com.annimon.stream.function.Predicate;
import com.yuckar.infra.perf.utils.PerfUtils;

public class ExecutorPerfHandler extends DefaultPerfHandler {

	protected Predicate<Line> filter() {
		return line -> true;
	}

	@Override
	public String[] pkgs() {
		return new String[] { PerfUtils.N_executor.substring(0, PerfUtils.N_executor.length() - 1) };
	}

}
