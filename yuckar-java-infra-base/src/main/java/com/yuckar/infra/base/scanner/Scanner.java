package com.yuckar.infra.base.scanner;

import java.io.File;
import java.net.JarURLConnection;
import java.util.List;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.ThrowableConsumer;
import com.annimon.stream.function.ThrowableFunction;
import com.google.common.collect.Lists;
import com.yuckar.infra.base.utils.RunUtils;

public class Scanner<T> {

	public static Scanner<Void> of(ThrowableConsumer<String, Throwable> mapper, ClassLoader loader) {
		return of(str -> {
			mapper.accept(str);
			return null;
		}, o -> false, loader);
	}

	public static <T> Scanner<T> of(ThrowableFunction<String, T, Throwable> mapper, Predicate<T> filter,
			ClassLoader clazzLoader) {
		return new Scanner<>(mapper, filter, clazzLoader);
	}

	private final ThrowableFunction<String, T, Throwable> mapper;
	private final Predicate<T> filter;
	private final ClassLoader loader;
	private final List<T> datas = Lists.newArrayList();

	public Scanner(ThrowableFunction<String, T, Throwable> mapper, Predicate<T> filter, ClassLoader loader) {
		this.mapper = mapper;
		this.filter = filter;
		this.loader = loader;
	}

	public Scanner<T> scan(String path, String regex) {
		Pattern pattern = Pattern.compile(regex);
		RunUtils.catching(() -> {
			Stream.of(loader.getResources(path).asIterator()).forEach(url -> {
				switch (url.getProtocol()) {
				case "file":
					scan(new File(RunUtils.catching(() -> url.toURI())), pattern);
					break;
				case "jar":
					scan(RunUtils.catching(() -> ((JarURLConnection) url.openConnection()).getJarFile()), pattern);
					break;
				default:
				}
			});
		});
		return this;
	}

	public List<T> get() {
		return this.datas;
	}

	private void scan(File file, Pattern pattern) {
		if (file.isDirectory()) {
			Stream.of(file.listFiles()).forEach(f -> scan(f, pattern));
		} else if (file.isFile()) {
			data(file.getAbsolutePath().replace(File.separator, "/"), pattern);
		}
	}

	private void scan(JarFile jar, Pattern pattern) {
		Stream.of(jar.entries().asIterator()).filter(entry -> !entry.isDirectory()).forEach(entry -> {
			data(jar.getName() + "?" + entry.getName(), pattern);
		});
	}

	private void data(String name, Pattern pattern) {
		Matcher matcher = pattern.matcher(name);
		if (matcher.find()) {
			RunUtils.catching(() -> {
				T data = mapper.apply(matcher.groupCount() == 0 ? matcher.group() : matcher.group(1));
				if (filter.test(data)) {
					datas.add(data);
				}
			});
		}
	}

}
