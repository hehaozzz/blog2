package cn.blog2.service;

import com.github.pagehelper.PageInfo;
import java.util.Map;

public abstract interface BlogDataService {
	public abstract int[] regist(Map<String, Object> paramMap);

	public abstract int deleteByPrimaryKey(Long paramLong);

	public abstract int insert(Map<String, Object> paramMap);

	public abstract Map<String, Object> selectByPrimaryKey(Long paramLong);

	public abstract PageInfo<Map<String, Object>> selectAll(Map<String, Object> paramMap);

	public abstract Map<String, Object> updateByPrimaryKey(Map<String, Object> paramMap);
}