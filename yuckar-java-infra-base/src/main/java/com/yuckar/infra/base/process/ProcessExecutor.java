package com.yuckar.infra.base.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;

import com.annimon.stream.Stream;
import com.annimon.stream.function.ThrowableConsumer;
import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yuckar.infra.base.bean.simple.Pair;
import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.base.thread.MapperFuture;
import com.yuckar.infra.base.utils.N_humanUtils;

public class ProcessExecutor {

	public static ProcessExecutor of() {
		return of(System.getProperty("user.dir"));
	}

	public static ProcessExecutor of(String workdir) {
		return of(workdir, System.getProperty("native.encoding", "UTF-8"));
	}

	public static ProcessExecutor of(String workdir, String charset) {
		return of(workdir, charset, true);
	}

	public static ProcessExecutor of(String workdir, String charset, boolean add_sh_or_cmd) {
		return new ProcessExecutor(workdir, charset, add_sh_or_cmd);
	}

	private final Logger logger = LoggerUtils.getLogger(getClass());
	private final String workdir;
	private final String charset;
	private final boolean add_sh_or_cmd;

	public ProcessExecutor(String workdir, String charset, boolean add_sh_or_cmd) {
		this.workdir = workdir;
		this.charset = charset;
		this.add_sh_or_cmd = add_sh_or_cmd;
	}

	public Integer exec_inheritIO(String command) {
		return exec_inheritIO(new String[] { command });
	}

	public Integer exec_inheritIO(String[] commands) {
		return exec_inheritIO(Lists.newArrayList(commands));
	}

	public Integer exec_inheritIO(List<String> commands) {
		return exec(commands, null);
	}

	public Pair<Integer, List<String>> exec(String command) {
		return exec(new String[] { command });
	}

	public Pair<Integer, List<String>> exec(String[] commands) {
		return exec(Lists.newArrayList(commands));
	}

	public Pair<Integer, List<String>> exec(List<String> commands) {
		List<String> list = Lists.newArrayList();
		return Pair.pair(exec(commands, list::add), list);
	}

	public <X extends Throwable> Integer exec(String command, ThrowableConsumer<String, X> handler) throws X {
		return exec(new String[] { command }, handler);
	}

	public <X extends Throwable> Integer exec(String[] commands, ThrowableConsumer<String, X> handler) throws X {
		return exec(Lists.newArrayList(commands), handler);
	}

	public <X extends Throwable> Integer exec(List<String> commands, ThrowableConsumer<String, X> handler) throws X {
		Stopwatch stopwatch = Stopwatch.createStarted();
		Process process = process(commands, handler);
		Integer exit = null;
		try {
			exit = process.waitFor();
		} catch (InterruptedException e) {
			logger.info("interrupt!!!");
			Thread.interrupted();
		} finally {
			logger.info("workdir:[{}] command:[{}] pid:{} exit:{} elapsed:{}", workdir, Joiner.on(" ").join(commands),
					process.pid(), exit, N_humanUtils.formatMills(stopwatch.elapsed(TimeUnit.MILLISECONDS)));
			if (process.isAlive()) {
				process.destroy();
			}
		}
		return exit;
	}

	private static final ThreadFactory thread_factory = new ThreadFactoryBuilder()
			.setNameFormat("process-async-exec-%d").setDaemon(true).build();

	public <X extends Throwable> Future<Integer> async(List<String> commands, ThrowableConsumer<String, X> handler)
			throws X {
		if (handler != null) {
			CompletableFuture<Integer> future = new CompletableFuture<>();
			Thread thread = thread_factory.newThread(() -> {
				try {
					future.complete(exec(commands, handler));
				} catch (Throwable e) {
					future.completeExceptionally(e);
				}
			});
			thread.start();
			return new MapperFuture<Integer, Integer>(future, i -> i) {

				@Override
				public boolean cancel(boolean mayInterruptIfRunning) {
					thread.interrupt();
					super.cancel(mayInterruptIfRunning);
					return true;
				}

			};
		} else {
			Process process = process(commands, handler);
			AtomicBoolean cancelled = new AtomicBoolean(false);
			return new Future<Integer>() {

				@Override
				public boolean cancel(boolean mayInterruptIfRunning) {
					if (process.isAlive()) {
						process.destroy();
						cancelled.set(true);
					}
					return true;
				}

				@Override
				public boolean isCancelled() {
					return cancelled.get();
				}

				@Override
				public boolean isDone() {
					return !process.isAlive();
				}

				@Override
				public Integer get() throws InterruptedException, ExecutionException {
					return process.waitFor();
				}

				@Override
				public Integer get(long timeout, TimeUnit unit)
						throws InterruptedException, ExecutionException, TimeoutException {
					process.waitFor(timeout, unit);
					return process.waitFor();
				}

			};
		}
	}

	@SuppressWarnings("unchecked")
	public <X extends Throwable> Process process(List<String> commands, ThrowableConsumer<String, X> handler) throws X {
		commands = Lists.newArrayList(commands);
		Process process = null;
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.redirectErrorStream(true);
			if (StringUtils.isNotEmpty(workdir)) {
				builder.directory(new File(workdir));
			}
			if (add_sh_or_cmd) {
				if (SystemUtils.IS_OS_WINDOWS) {
					commands.add(0, "cmd.exe");
					commands.add(1, "/c");
				} else {
					commands.add(0, "sh");
					commands.add(1, "-c");
				}
			}
			builder.command().addAll(Stream.of(commands).flatMap(c -> Stream.of(c.split(" "))).toList());
			if (handler == null) {
				builder.inheritIO();
			}
			process = builder.start();
			logger.info("workdir:[{}] command:[{}] pid:{}", workdir, Joiner.on(" ").join(commands), process.pid());
			if (handler == null) {

			} else {
				/**
				 * 当日志量过大时，如果不处理的的话，waitFor会阻塞。
				 */
				try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), charset))) {
					String line;
					while ((line = br.readLine()) != null && !Thread.currentThread().isInterrupted()) {
						handler.accept(line);
					}
				}
			}
			return process;
		} catch (Throwable e) {
			logger.error("workdir:[" + workdir + "][" + Joiner.on(" ").join(commands) + "]", e);
			if (process != null && process.isAlive()) {
				process.destroy();
			}
			if (e instanceof IOException) {
				throw new RuntimeException(e);
			}
			throw (X) e;
		}
	}
}