package com.yuckar.infra.conf.yconfs.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.lang3.StringUtils;

import com.annimon.stream.function.Function;
import com.google.common.io.Files;
import com.yuckar.infra.base.file.utils.FileUtils;
import com.yuckar.infra.base.json.JsonUtils;
import com.yuckar.infra.conf.yconfs.AbstractYconfs;
import com.yuckar.infra.conf.yconfs.YconfsListener;
import com.yuckar.infra.conf.yconfs.utils.YconfsFileUtils;

public class FileYconfs<V> extends AbstractYconfs<V> {

	private final String workspace;

	public FileYconfs(Class<V> clazz) {
		this(System.getProperty("user.dir") + File.separator + "confs", clazz);
	}

	public FileYconfs(String workspace, Class<V> clazz) {
		this(workspace, clazz, null);
	}

	public FileYconfs(String workspace, Class<V> clazz, Function<String, Set<YconfsListener<V>>> listeners) {
		super(clazz, listeners);
		this.workspace = workspace;
	}

	@Override
	public void set(String path, V value) {
		try {
			File file = new File(YconfsFileUtils.toFile(this.workspace, path) + File.separator + "main.json");
			FileUtils.createDirIfNoExists(file.getParentFile());
			String json = "";
			if (value == null) {

			} else {
				json = JsonUtils.toPrettyJson(value);
			}
			Files.asCharSink(file, StandardCharsets.UTF_8).write(json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void init(String path) {
		File file = new File(YconfsFileUtils.toFile(this.workspace, path) + File.separator + "main.json");
		FileUtils.createDirIfNoExists(file.getParentFile());
		YconfsFileUtils.monitor(file.getParent(), new FileAlterationListenerAdaptor() {
			@Override
			public void onFileCreate(final File ifile) {
				if (file.equals(ifile) && FileYconfs.this.get(path) == null) {
					refresh(path);
				}
			}

			@Override
			public void onFileChange(final File ifile) {
				if (file.equals(ifile)) {
					refresh(path);
				}
			}

			@Override
			public void onFileDelete(final File ifile) {
				if (file.equals(ifile)) {
					refresh(path);
				}
			}
		});
	}

	@Override
	protected Object json(String path) {
		try {
			File file = new File(YconfsFileUtils.toFile(this.workspace, path) + File.separator + "main.json");
			if (!file.exists()) {
				return null;
			}
			String json = StringUtils.join(Files.readLines(file, StandardCharsets.UTF_8), "\n").trim();
//			if (StringUtils.isEmpty(json)) {
//				json = defString();
//			}
			if (StringUtils.isNotEmpty(json)
					&& (json.startsWith("{") && json.endsWith("}") || json.startsWith("[") && json.endsWith("]"))) {
				return JsonUtils.fromJson(json, Object.class);
			} else {
				return json;
			}
		} catch (IOException e) {
			logger.error("path:" + path, e);
			throw new RuntimeException(e);
		}
	}

}
