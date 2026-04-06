package com.yuckar.infra.runner;

import com.yuckar.infra.base.args.Args;
import com.yuckar.infra.base.args.MainArgs;

public class RunnerArgs {

	private static Args args;

	public synchronized static void args(String[] args) {
		args(Args.of(args));
	}

	public synchronized static void args(Args args) {
		if (RunnerArgs.args == null) {
			RunnerArgs.args = args;
		} else {
			throw new RuntimeException("The args has already been setted!!!");
		}
	}

	public synchronized static Args args() {
		if (RunnerArgs.args == null) {
			return MainArgs.args();
		} else {
			return RunnerArgs.args;
		}
	}

}
