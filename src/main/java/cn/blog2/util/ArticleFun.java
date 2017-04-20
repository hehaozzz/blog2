package cn.blog2.util;

import cn.blog2.service.BlogDataService;
import cn.blog2.service.BlogDataServiceImpl;
import com.github.pagehelper.PageInfo;
import java.util.HashMap;
import java.util.Map;

public class ArticleFun {
	public PageInfo<Map<String, Object>> show(int start, int limit) {
		BlogDataService blogDataServiceImpl = (BlogDataServiceImpl) SpringContextUtil.getBean("blogDataServiceImpl");
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("start", Integer.valueOf(start == 0 ? 1 : start));
		queryMap.put("limit", Integer.valueOf(limit == 0 ? 10 : limit));
		PageInfo<Map<String, Object>> pageInfo = blogDataServiceImpl.selectAll(queryMap);
		return pageInfo;
	}
}