package com.yuckar.infra.storage.db.jdbc;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;

import com.annimon.stream.Optional;
import com.google.common.base.Stopwatch;
import com.yuckar.infra.base.perf.PerfUtils;
import com.yuckar.infra.storage.db.model.KdbModel;

public abstract class KjdbcImpl<T> implements Kjdbc<T> {

	private static final StringSubstitutor format = new StringSubstitutor(key -> "?", ":v", "v",
			StringSubstitutor.DEFAULT_ESCAPE);

	private final KdbModel model;
	private final String table;
	private final RowMapper<T> mapper;

	public KjdbcImpl(Class<T> clazz) {
		this(clazz, null);
	}

	public KjdbcImpl(Class<T> clazz, String suffix) {
		this.model = KdbModel.of(clazz);
		this.table = model.table()
				+ Optional.ofNullable(suffix).filter(StringUtils::isNotEmpty).map(s -> "_" + s).orElse("");
		this.mapper = new BeanPropertyRowMapper<>(clazz);
	}

	@Override
	public List<T> executeQuery(String sql, Map<String, ?> valueMap, boolean master) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		KjdbcHolder holder = this.holder(master);
		String f_sql = format(sql, valueMap);
		List<T> rtn;
		try {
			logger.debug("query-sql:{}", f_sql);
			rtn = holder.jdbcTemplate().query(sql, valueMap, this.mapper);
			PerfUtils.perf(PerfUtils.N_storage_kjdbc, holder.tag() + "exec", f_sql, master + "").count(1)
					.micro(stopwatch.elapsed(TimeUnit.MICROSECONDS)).logstash();
		} catch (DataAccessException e) {
			PerfUtils.perf(PerfUtils.N_storage_kjdbc, holder.tag() + e.getClass().getSimpleName(), f_sql, master + "")
					.count(1).micro(stopwatch.elapsed(TimeUnit.MICROSECONDS)).logstash();
			throw e;
		}
		return rtn;
	}

	@Override
	public int executeUpdate(String sql, Map<String, ?> valueMap) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		KjdbcHolder holder = this.holder(true);
		String f_sql = format(sql, valueMap);
		int rtn;
		try {
			logger.debug("update-sql:{}", f_sql);
			rtn = holder.jdbcTemplate().update(sql, valueMap);
			PerfUtils.perf(PerfUtils.N_storage_kjdbc, holder.tag() + "exec", f_sql).count(1)
					.micro(stopwatch.elapsed(TimeUnit.MICROSECONDS)).logstash();
		} catch (DataAccessException e) {
			PerfUtils.perf(PerfUtils.N_storage_kjdbc, holder.tag() + e.getClass().getSimpleName(), f_sql).count(1)
					.micro(stopwatch.elapsed(TimeUnit.MICROSECONDS)).logstash();
			throw e;
		}
		return rtn;
	}

	private String format(String sql, Map<String, ?> valueMap) {
		String f_sql = sql;
		if (f_sql.indexOf(":v") >= 0) {
			f_sql = format.replace(sql);
		}
		if (f_sql.indexOf(":") >= 0) {
			f_sql = NamedParameterUtils.substituteNamedParameters(sql, new MapSqlParameterSource(valueMap));
		}
		return f_sql;
	}

	@Override
	public final KdbModel model() {
		return this.model;
	}

	@Override
	public final String table() {
		return this.table;
	}
}