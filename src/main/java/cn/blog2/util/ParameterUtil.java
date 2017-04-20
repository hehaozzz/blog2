package cn.blog2.util;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class ParameterUtil {
	public static Map<String, Object> getAndPutAttr(HttpServletRequest request, Map<String, Object> map, String key) {
		String value = request.getParameter(key);
		if ((null != value) && (!"".equals(value)))
			map.put(key, value);
		return map;
	}
}