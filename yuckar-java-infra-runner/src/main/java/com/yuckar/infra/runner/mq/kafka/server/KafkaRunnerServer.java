package com.yuckar.infra.runner.mq.kafka.server;

import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.google.common.collect.Lists;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.base.term.TermHelper;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.runner.mq.kafka.KafkaRunner;
import com.yuckar.infra.runner.mq.kafka.holder.KafkaRunnerHolder;
import com.yuckar.infra.runner.server.AbstractRunnerServer;

@SuppressWarnings("rawtypes")
public class KafkaRunnerServer extends AbstractRunnerServer<KafkaRunner> {

	@Override
	protected void doRun(KafkaRunner runner) {
		KafkaRunnerHolder holder = KafkaRunnerHolder.of(runner);
		String path = YconfsNamespaceUtils.kafka(runner.topic().topic() + "/consumer");
		Yconfs<Properties> yconfs = holder.context().getYconfs(Properties.class);

		LazySupplier<KafkaRunnerHolder> holder_supplier = LazySupplier.wrap(() -> {
			Properties properties = yconfs.get(path);
			properties.setProperty(CommonClientConfigs.GROUP_ID_CONFIG, runner.group().group());
			KafkaConsumer<?, ?> consumer = new KafkaConsumer<>(properties);
			consumer.subscribe(Lists.newArrayList(runner.topic().topic()));
			holder.consumer(consumer);
			return holder;
		});
		yconfs.addListener(path, event -> {
			holder_supplier.refresh(KafkaRunnerHolder::close);
			holder_supplier.get();
		});
		TermHelper.addTerm(runner.module(), () -> holder_supplier.refresh(KafkaRunnerHolder::close));
		holder_supplier.get();
	}

	@Override
	protected boolean nlock() {
		return false;
	}

}
