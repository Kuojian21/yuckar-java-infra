package com.yuckar.infra.common.perf.logger;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.annimon.stream.Stream;
import com.google.common.collect.Maps;
import com.yuckar.infra.common.bean.simple.Pair;
import com.yuckar.infra.common.buffer.trigger.BufferTrigger;
import com.yuckar.infra.common.hook.HookHelper;
import com.yuckar.infra.common.perf.handler.IPerfHandler;
import com.yuckar.infra.common.perf.model.PerfLogHolder;
import com.yuckar.infra.common.perf.model.PerfLogMetrics;
import com.yuckar.infra.common.perf.model.PerfLogTag;
import com.yuckar.infra.common.spi.PkgSpiFactory;

public class PerfLogger {

	private final PkgSpiFactory<IPerfHandler> spi = PkgSpiFactory.of(IPerfHandler.class);
	private final BufferTrigger<PerfContext> bufferTrigger;

	public PerfLogger() {
		super();
		this.bufferTrigger = BufferTrigger.<PerfContext, Map<PerfLogTag, PerfLogMetrics>>simple() //
				.setContainer(Maps::newConcurrentMap, (container, builder) -> {
					container.merge(builder.getPerfLog(), new PerfLogMetrics(builder.getCount(), builder.getMicro()),
							(value1, value2) -> {
								value1.accept(value2.getTotalCount(), value2.getTotalMicro());
								return value1;
							});
				}) //
				.setConsumer(this::handle) //
				.setInterval(1, TimeUnit.MINUTES) //
				.disableEnqueueLock() //
				.build();
		HookHelper.addHook("perf", () -> {
			this.bufferTrigger.manuallyDoTrigger();
		});
	}

	public void logstash(PerfContext builder) {
		this.bufferTrigger.enqueue(builder);
	}

	protected void handle(Map<PerfLogTag, PerfLogMetrics> perfs) {
		Stream.of(perfs).map(e -> PerfLogHolder.of(e.getKey(), e.getValue())).flatMap(holder -> Stream
				.of(spi.getList(holder.getTag().getNamespace())).map(handler -> Pair.pair(handler, holder)))
				.groupBy(Pair::getKey).forEach(entry -> {
					entry.getKey().handle(Stream.of(entry.getValue()).map(Pair::getValue).toList());
				});
	}

}
