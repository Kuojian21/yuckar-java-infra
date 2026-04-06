package com.yuckar.infra.server.startup;

import org.slf4j.Logger;

import com.yuckar.infra.base.args.Args;
import com.yuckar.infra.base.args.MainArgs;
import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.base.spi.SpiFactory;
import com.yuckar.infra.base.term.SignalHelper;
import com.yuckar.infra.base.trace.TraceIDUtils;

import sun.misc.Signal;

public class StartableMain {

	private static final Logger logger = LoggerUtils.logger(StartableMain.class);

	public static void main(String[] args) throws Exception {
		try {
			TraceIDUtils.generate();
			MainArgs.args(Args.of(args));
			for (Startable startable : SpiFactory.stream(Startable.class).sorted().toList()) {
				logger.info("The startable:{} will start!!!", startable.getClass().getSimpleName());
				startable.startup();
				logger.info("The startable:{} have started successlly!!!", startable.getClass().getSimpleName());
			}
		} catch (Throwable e) {
			logger.error("StartablaMain throwable!!!", e);
			SignalHelper.raise(new Signal("TERM"));
		} finally {
			TraceIDUtils.clear();
		}
	}
}
