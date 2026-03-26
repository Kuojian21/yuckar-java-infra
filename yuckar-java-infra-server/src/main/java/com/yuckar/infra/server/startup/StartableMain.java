package com.yuckar.infra.server.startup;

import org.slf4j.Logger;

import com.yuckar.infra.common.args.Args;
import com.yuckar.infra.common.logger.LoggerUtils;
import com.yuckar.infra.common.spi.SpiFactory;
import com.yuckar.infra.common.term.SignalHelper;
import com.yuckar.infra.common.trace.TraceIDUtils;
import com.yuckar.infra.server.args.ServerArgs;

import sun.misc.Signal;

public class StartableMain {

	private static final Logger logger = LoggerUtils.logger(StartableMain.class);

	public static void main(String[] args) throws Exception {
		try {
			TraceIDUtils.generate();
			ServerArgs.args(Args.of(args));
			for (Startable startable : SpiFactory.stream(Startable.class).sorted().toList()) {
				logger.info("The startable:{} will start!!!", startable.getClass().getSimpleName());
				startable.startup();
				logger.info("The startable:{} have started success!!!", startable.getClass().getSimpleName());
			}
		} catch (Exception e) {
			logger.error("StartablaMain exception!!!", e);
			SignalHelper.raise(new Signal("TERM"));
		} finally {
			TraceIDUtils.clear();
		}
	}
}
