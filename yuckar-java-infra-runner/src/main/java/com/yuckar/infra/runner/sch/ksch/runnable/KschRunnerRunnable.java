package com.yuckar.infra.runner.sch.ksch.runnable;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.google.common.base.Stopwatch;
import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.base.perf.PerfUtils;
import com.yuckar.infra.base.term.TermHelper;
import com.yuckar.infra.base.trace.TraceIDUtils;
import com.yuckar.infra.base.utils.N_humanUtils;
import com.yuckar.infra.runner.sch.ksch.KschRunner;

public class KschRunnerRunnable implements Runnable {

	private final Logger logger = LoggerUtils.logger(KschRunner.class);
	private final KschRunner job;

	public KschRunnerRunnable(KschRunner job) {
		super();
		this.job = job;
	}

	@Override
	public void run() {
		while (!TermHelper.isStopping()) {
			TraceIDUtils.generate();
			Stopwatch stopwatch = Stopwatch.createStarted();
			String jobID = job.module() + "." + job.ID();
			long sleep = TimeUnit.SECONDS.toMillis(5);
			try {
				sleep = job.run();
				PerfUtils.perf(PerfUtils.N_runner_ksch, "exec", jobID).count(1)
						.micro(stopwatch.elapsed(TimeUnit.MICROSECONDS)).logstash();
				logger.debug("job:" + jobID + " class:" + job.getClass().getName() + " elapsed:"
						+ N_humanUtils.formatMicros(stopwatch.elapsed(TimeUnit.MICROSECONDS)));
			} catch (Exception e) {
				PerfUtils.perf(PerfUtils.N_runner_ksch, e.getClass().getSimpleName(), jobID).count(1)
						.micro(stopwatch.elapsed(TimeUnit.MICROSECONDS)).logstash();
				logger.error("job:" + jobID + " class:" + job.getClass().getName() + " elapsed:"
						+ N_humanUtils.formatMicros(stopwatch.elapsed(TimeUnit.MICROSECONDS)), e);
			} finally {
				TraceIDUtils.clear();
			}
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

}
