package com.yuckar.infra.runner;

import com.yuckar.infra.runner.server.RunnerServer;
import com.yuckar.infra.runner.server.RunnerServerFactory;

public interface Runner {

	default String module() {
		return RunnerServerFactory.server(this.getClass()).getClass().getSimpleName().replace("RunnerServer", "")
				.toLowerCase();
	}

	default String ID() {
		return this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1).replace("$", "_");
	}

	@SuppressWarnings("unchecked")
	default <R extends Runner> void execute() {
		((RunnerServer<R>) RunnerServerFactory.server(this.getClass())).run((R) this);
	}

}
