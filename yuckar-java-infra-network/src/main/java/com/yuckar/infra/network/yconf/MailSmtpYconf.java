package com.yuckar.infra.network.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.network.mail.MailSmtp;
import com.yuckar.infra.network.mail.MailSmtpInfo;

public interface MailSmtpYconf extends CacheableInfoYconf<MailSmtpInfo, MailSmtp> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.mail(key());
	}

}
