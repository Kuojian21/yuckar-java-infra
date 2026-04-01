package com.yuckar.infra.common.perf.logger;

import com.yuckar.infra.common.perf.model.PerfLogTag;

public class PerfFactory {

	public static final PerfFactory DEFAULT = new PerfFactory(new PerfLogger());

	private final PerfLogger perfLogger;

	public PerfFactory(PerfLogger perfLogger) {
		super();
		this.perfLogger = perfLogger;
	}

	public PerfContext perfContext(PerfLogTag tag) {
		return new PerfContext(this.perfLogger, tag);
	}

}
