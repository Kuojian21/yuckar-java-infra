package com.yuckar.infra.base.args;

public class MainArgs {

	private static Args args;

	public synchronized static void args(String[] args) {
		args(Args.of(args));
	}

	public synchronized static void args(Args args) {
		if (MainArgs.args == null) {
			MainArgs.args = args;
		} else {
			throw new RuntimeException("The args has already been setted!!!");
		}
	}

	public synchronized static Args args() {
		if (MainArgs.args == null) {
			return Args.of(new String[0]);
		} else {
			return MainArgs.args;
		}
	}

}
