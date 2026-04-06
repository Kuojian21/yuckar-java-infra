package com.yuckar.infra.network.mail;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.sun.mail.smtp.SMTPTransport;
import com.yuckar.infra.base.executor.PoolExecutorInfo;

public class MailSmtpInfo extends MailSenderInfo implements PoolExecutorInfo<SMTPTransport> {

	private GenericObjectPoolConfig<SMTPTransport> poolConfig;

	@Override
	public GenericObjectPoolConfig<SMTPTransport> getPoolConfig() {
		return poolConfig;
	}

	@Override
	public void setPoolConfig(GenericObjectPoolConfig<SMTPTransport> poolConfig) {
		this.poolConfig = poolConfig;
	}

}
