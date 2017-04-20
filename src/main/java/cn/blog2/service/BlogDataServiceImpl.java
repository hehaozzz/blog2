package cn.blog2.service;

import cn.blog2.dao.ArticleMapper;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BlogDataServiceImpl implements BlogDataService {
	Logger logger = LoggerFactory.getLogger(BlogDataServiceImpl.class);

	@Autowired
	ArticleMapper articleMapper;

	public int[] regist(Map<String, Object> queryMap) {
		int[] array = new int[2];
		array[0] = this.articleMapper.userRegist(queryMap);
		array[1] = this.articleMapper.userRoleRegist(queryMap);
		return array;
	}

	@CacheEvict(value = { "common" }, key = "'id_'+#id")
	public int deleteByPrimaryKey(Long id) {
		return this.articleMapper.deleteByPrimaryKey(id);
	}

	public int insert(Map<String, Object> queryMap) {
		return this.articleMapper.insert(queryMap);
	}

	@Cacheable(value = { "common" }, key = "'id_'+#id")
	public Map<String, Object> selectByPrimaryKey(Long id) {
		this.logger.info("如果只打印了一次则说明后面是从缓存中读取的数据！");
		return this.articleMapper.selectByPrimaryKey(id);
	}

	public PageInfo<Map<String, Object>> selectAll(Map<String, Object> queryMap) {
		List<Map<String, Object>> list = this.articleMapper.selectAll(queryMap);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		return pageInfo;
	}

	@CachePut(value = { "common" }, key = "'id_'+#queryMap.get('id')")
	public Map<String, Object> updateByPrimaryKey(Map<String, Object> queryMap) {
		this.articleMapper.updateByPrimaryKey(queryMap);
		Long id = Long.valueOf(String.valueOf(queryMap.get("id")));
		return this.articleMapper.selectByPrimaryKey(id);
	}
}