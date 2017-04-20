package cn.blog2.redis;

import java.util.concurrent.Callable;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

public class SystemRedisCache implements Cache {
	/**
	 * Redis
	 */
	private RedisTemplate<String, Object> redisTemplate;
	/**
	 * 缓存名称
	 */
	private String name;

	/**
	 * 超时时间
	 */
	private long timeout;

	@Override
	public ValueWrapper get(Object key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		} else {
			final String finalKey;
			if (key instanceof String) {
				finalKey = (String) key;
			} else {
				finalKey = key.toString();
			}
			Object object = null;
			object = redisTemplate.execute(new RedisCallback<Object>() {
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					byte[] key = finalKey.getBytes();
					byte[] value = connection.get(key);
					if (value == null) {
						return null;
					}
					return SerializeUtils.deserialize(value);
				}
			});
			return (object != null ? new SimpleValueWrapper(object) : null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> type) {
		if (StringUtils.isEmpty(key) || null == type) {
			return null;
		} else {
			final String finalKey;
			final Class<T> finalType = type;
			if (key instanceof String) {
				finalKey = (String) key;
			} else {
				finalKey = key.toString();
			}
			final Object object = redisTemplate.execute(new RedisCallback<Object>() {
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					byte[] key = finalKey.getBytes();
					byte[] value = connection.get(key);
					if (value == null) {
						return null;
					}
					return SerializeUtils.deserialize(value);
				}
			});
			if (finalType != null && finalType.isInstance(object) && null != object) {
				return (T) object;
			} else {
				return null;
			}
		}
	}

	@Override
	public void put(final Object key, final Object value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
			return;
		} else {
			final String finalKey;
			if (key instanceof String) {
				finalKey = (String) key;
			} else {
				finalKey = key.toString();
			}
			if (!StringUtils.isEmpty(finalKey)) {
				final Object finalValue = value;
				redisTemplate.execute(new RedisCallback<Boolean>() {
					@Override
					public Boolean doInRedis(RedisConnection connection) {
						connection.set(finalKey.getBytes(), SerializeUtils.serialize(finalValue));
						// 设置超时间
						connection.expire(finalKey.getBytes(), timeout);
						return true;
					}
				});
			}
		}
	}

	/*
	 * 根据Key 删除缓存
	 */
	@Override
	public void evict(Object key) {
		if (null != key) {
			final String finalKey;
			if (key instanceof String) {
				finalKey = (String) key;
			} else {
				finalKey = key.toString();
			}
			if (!StringUtils.isEmpty(finalKey)) {
				redisTemplate.execute(new RedisCallback<Long>() {
					public Long doInRedis(RedisConnection connection) throws DataAccessException {
						return connection.del(finalKey.getBytes());
					}
				});
			}
		}
	}

	/*
	 * 清楚系统缓存
	 */
	@Override
	public void clear() {
		this.redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return "ok";
			}
		});
	}

	public Object getNativeCache() {
		return this.redisTemplate;
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return this.redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimeout() {
		return this.timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public <T> T get(Object key, Callable<T> value) {
		return null;
	}

	public ValueWrapper putIfAbsent(Object key, Object value) {
		return null;
	}
}