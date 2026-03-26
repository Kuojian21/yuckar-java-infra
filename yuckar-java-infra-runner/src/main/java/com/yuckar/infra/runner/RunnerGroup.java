package com.yuckar.infra.runner;

import java.util.List;

//import com.annimon.stream.Stream;
//import com.yuckar.infra.common.info.Pair;
//import com.yuckar.infra.runner.server.RunnerServer;
//import com.yuckar.infra.runner.server.RunnerServerFactory;
//import com.yuckar.infra.runner.utils.RunnerUtils;

public interface RunnerGroup {

	List<? extends Runner> jobList();

	default void execute() {
//		Stream.of(jobList()).map(job -> Pair.pair(RunnerServerFactory.server(job.getClass()), job))
//				.groupBy(Pair::getKey).forEach(entry -> {
//					RunnerServer<? extends Runner> server = entry.getKey();
//					List<? extends Runner> jobs = Stream.of(entry.getValue()).map(pair -> pair.getValue()).toList();
//					RunnerUtils.run(server, jobs);
//				});
		this.jobList().forEach(Runner::execute);
	}

}
