package com.yuckar.infra.network.legacy;

import javax.mail.MessagingException;
import javax.mail.Service;
import org.slf4j.Logger;

import com.yuckar.infra.base.executor.PoolExecutor;
import com.yuckar.infra.base.logger.LoggerUtils;

public abstract class Mail<S extends Service> extends PoolExecutor<MailSessionHolder<S>, MailInfo<S>> {

	protected final Logger logger = LoggerUtils.logger(getClass());

	public Mail(MailInfo<S> info) {
		super(info);
	}

	@Override
	public void destroy(MailSessionHolder<S> bean) throws Exception {
		bean.close();
	}

	@Override
	public boolean validate(MailSessionHolder<S> bean) {
		try {
			return bean.getService().isConnected();
		} catch (MessagingException e) {
			logger.error("", e);
			return false;
		}
	}
}
