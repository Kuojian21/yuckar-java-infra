package com.yuckar.infra.runner.mq.kafka.holder;

import java.time.Duration;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.common.util.concurrent.Uninterruptibles;
import com.yuckar.infra.common.json.JsonUtils;
import com.yuckar.infra.common.lazy.LazyRunnable;
import com.yuckar.infra.common.logger.LoggerUtils;
import com.yuckar.infra.common.term.TermHelper;
import com.yuckar.infra.common.utils.RunUtils;
import com.yuckar.infra.register.context.RegisterFactory;
import com.yuckar.infra.runner.common.RunnerHolder;
import com.yuckar.infra.runner.mq.kafka.KafkaRunner;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class KafkaRunnerHolder extends RunnerHolder<KafkaRunner> {

	public static KafkaRunnerHolder of(KafkaRunner runner) {
		return new KafkaRunnerHolder(runner);
	}

	private static final Logger logger = LoggerUtils.logger(KafkaRunnerHolder.class);
	private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat("kafka-consumer-%d")
			.setDaemon(false).build();

	private final LazyRunnable runnable;
	private final AtomicBoolean stopping = new AtomicBoolean(true);
	private volatile KafkaConsumer<?, ?> consumer;

	private KafkaRunnerHolder(KafkaRunner runner) {
		super(runner, RegisterFactory.getContext(runner.topic().getClass()));
		this.runnable = LazyRunnable.wrap(() -> {
			THREAD_FACTORY.newThread(() -> {
				try {
					while (!TermHelper.isStopping()) {
						if (stopping.get()) {
							Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
						}
						RunUtils.catching(() -> {
							ConsumerRecords<?, ?> records = consumer.poll(Duration.ofMinutes(1));
							if (records != null && records.count() > 0) {
								for (ConsumerRecord record : records) {
									runner.handle(record.key(), record.value());
								}
								consumer.commitAsync((offsets, exception) -> {
									logger.info("offsets:{}", JsonUtils.toJson(offsets), exception);
								});
							}
						});
					}
				} finally {
					try {
						consumer.commitSync();
					} finally {
						consumer.close();
					}
				}
			}).start();
		});
	}

	public KafkaConsumer<?, ?> consumer() {
		return this.consumer;
	}

	public void consumer(KafkaConsumer<?, ?> consumer) {
		this.consumer = consumer;
		this.stopping.set(false);
		this.runnable.run();
	}

	public void close() {
		this.stopping.set(true);
		if (this.consumer != null) {
			this.consumer.close(Duration.ofMinutes(1));
		}
	}

}
