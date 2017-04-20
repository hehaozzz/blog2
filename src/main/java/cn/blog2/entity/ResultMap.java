package cn.blog2.entity;

import java.util.HashMap;
import oracle.sql.CLOB;

public class ResultMap<K, V> extends HashMap<K, V> {
	private static final long serialVersionUID = 5142605572912276752L;

	public V put(K key, V value) {
		return super.put(toLowerCase(key), clobToString(value));
	}

	@SuppressWarnings("unchecked")
	private K toLowerCase(Object key) {
		if ((key != null) && (key instanceof String)) {
			key = ((String) key).toLowerCase();
		}
		return (K) key;
	}

	@SuppressWarnings("unchecked")
	private V clobToString(Object value) {
		if ((value != null) && (value instanceof CLOB)) {
			try {
				int size = (int) ((CLOB) value).length();
				value = ((CLOB) value).getSubString(1L, size);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return (V) value;
	}
}