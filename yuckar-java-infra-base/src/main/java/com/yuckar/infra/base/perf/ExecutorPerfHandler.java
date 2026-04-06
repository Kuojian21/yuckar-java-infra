package com.yuckar.infra.base.perf;

import com.annimon.stream.function.Predicate;

public class ExecutorPerfHandler extends DefaultPerfHandler {

	protected Predicate<Line> filter() {
		return line -> true;
	}

	@Override
	public String[] pkgs() {
		return new String[] { PerfUtils.N_executor.substring(0, PerfUtils.N_executor.length() - 1) };
	}

}
