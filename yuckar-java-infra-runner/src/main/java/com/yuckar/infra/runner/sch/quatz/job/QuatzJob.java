package com.yuckar.infra.runner.sch.quatz.job;

import java.util.concurrent.TimeUnit;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import com.google.common.base.Stopwatch;
import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.base.perf.PerfUtils;
import com.yuckar.infra.base.term.TermHelper;
import com.yuckar.infra.base.trace.TraceIDUtils;
import com.yuckar.infra.base.utils.N_humanUtils;
import com.yuckar.infra.runner.sch.quatz.QuatzRunner;

public class QuatzJob implements Job {

	private final Logger logger = LoggerUtils.logger(QuatzJob.class);
	private final QuatzRunner job;

	public QuatzJob(QuatzRunner job) {
		this.job = job;
	}

	@Override
	public final void execute(JobExecutionContext context) throws JobExecutionException {
		if (TermHelper.isStopping()) {
			return;
		}
		Stopwatch stopwatch = Stopwatch.createStarted();
		String jobID = job.module() + "." + job.ID();
		try {
			TraceIDUtils.generate();
			this.job.run();
			PerfUtils.perf(PerfUtils.N_runner_quatz, "exec", jobID).count(1)
					.micro(stopwatch.elapsed(TimeUnit.MICROSECONDS)).logstash();
			logger.debug("job:" + jobID + " class:" + job.getClass().getName() + " elapsed:"
					+ N_humanUtils.formatMicros(stopwatch.elapsed(TimeUnit.MICROSECONDS)));
		} catch (Throwable e) {
			PerfUtils.perf(PerfUtils.N_runner_quatz, e.getClass().getSimpleName(), jobID).count(1)
					.micro(stopwatch.elapsed(TimeUnit.MICROSECONDS)).logstash();
			logger.error("job:" + jobID + " class:" + job.getClass().getName() + " elapsed:"
					+ N_humanUtils.formatMicros(stopwatch.elapsed(TimeUnit.MICROSECONDS)), e);
		} finally {
			TraceIDUtils.clear();
		}
	}

	public QuatzRunner getJob() {
		return job;
	}
}
