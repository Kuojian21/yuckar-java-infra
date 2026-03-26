package com.yuckar.infra.runner.server;

import com.yuckar.infra.runner.Runner;

public interface RunnerServer<R extends Runner> {

	void run(R runner);

}
