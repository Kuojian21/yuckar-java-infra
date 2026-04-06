package com.yuckar.infra.server.runner;

import java.util.List;

import org.slf4j.Logger;

import com.annimon.stream.Stream;
import com.yuckar.infra.base.args.Args;
import com.yuckar.infra.base.args.MainArgs;
import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.runner.Runner;
import com.yuckar.infra.runner.RunnerArgs;
import com.yuckar.infra.runner.RunnerGroup;
import com.yuckar.infra.spring.factory.SpringBeanFactory;

public class RunnerServerMain {

	private static final Logger logger = LoggerUtils.logger(RunnerServerMain.class);

	public static <R extends Runner> void main(String[] args) {
		try {
			MainArgs.args(Args.of(args));
			new RunnerGroup() {
				@Override
				public List<? extends Runner> jobList() {
					return Stream.of(RunnerArgs.args().value("bean").get()).flatMap(bn -> Stream.of(bn.split(",")))
							.map(bn -> SpringBeanFactory.getBean(bn, Runner.class)).toList();
				}
			}.execute();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}
