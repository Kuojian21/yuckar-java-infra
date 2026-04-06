package com.yuckar.infra.base.perf;

public class PerfLogHolder {

	public static PerfLogHolder of(PerfLogTag tag, PerfLogMetrics metrics) {
		return new PerfLogHolder(tag, metrics);
	}

	private final PerfLogTag tag;
	private final PerfLogMetrics metrics;

	public PerfLogHolder(PerfLogTag tag, PerfLogMetrics metrics) {
		super();
		this.tag = tag;
		this.metrics = metrics;
	}

	public PerfLogTag getTag() {
		return tag;
	}

	public PerfLogMetrics getMetrics() {
		return metrics;
	}
}
