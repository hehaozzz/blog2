package cn.blog2.web;

import cn.blog2.service.BlogDataService;
import cn.blog2.util.ParameterUtil;
import com.github.pagehelper.PageInfo;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlogAction {

	@Autowired
	BlogDataService blogDataServiceImpl;
	Logger logger = LoggerFactory.getLogger(BlogAction.class);

	/**
	 * 删除博客
	 * 
	 * @author hehao
	 * @param request
	 * @return
	 */
	@RequestMapping({ "/delete" })
	public String delete(@RequestParam Long id, HttpServletRequest request) {
		this.blogDataServiceImpl.deleteByPrimaryKey(id);
		return "redirect:/view?oper=list&author=mine";
	}

	/**
	 * 添加博客页面
	 * 
	 * @author hehao
	 * @param request
	 * @return
	 */
	@RequestMapping({ "/add" })
	public String add(HttpServletRequest request) {
		return "addBlog";
	}

	/**
	 * 添加博客
	 * 
	 * @author hehao
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/add" }, params = { "oper=do" })
	public String doAdd(HttpServletRequest request) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		ParameterUtil.getAndPutAttr(request, queryMap, "title");
		ParameterUtil.getAndPutAttr(request, queryMap, "description");
		queryMap.put("author", SecurityUtils.getSubject().getPrincipal());
		ParameterUtil.getAndPutAttr(request, queryMap, "content");
		ParameterUtil.getAndPutAttr(request, queryMap, "md");
		this.blogDataServiceImpl.insert(queryMap);
		return "redirect:/view?oper=list&author=mine";
	}

	/**
	 * 通过ID查看博客详情
	 * 
	 * @author hehao
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/view" }, params = { "oper=detail" })
	public String viewDetail(@RequestParam Long id, Model model, HttpServletRequest request) {
		Map<String, Object> data = this.blogDataServiceImpl.selectByPrimaryKey(id);
		data.put("user_name", SecurityUtils.getSubject().getPrincipal());
		model.addAttribute("data", data);
		return "blogDetail";
	}

	/**
	 * 查看博客列表
	 * 
	 * @author hehao
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/view" }, params = { "oper=list" })
	public String viewSelf(@RequestParam(defaultValue = "1") String start,
			@RequestParam(defaultValue = "10") String limit, @RequestParam(defaultValue = "") String author,
			Model model, HttpServletRequest request) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("start", start);
		queryMap.put("limit", limit);
		if (author.equals("mine"))
			author = String.valueOf(SecurityUtils.getSubject().getPrincipal());
		queryMap.put("author", author);
		PageInfo<Map<String, Object>> pageInfo = this.blogDataServiceImpl.selectAll(queryMap);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("author", author);
		return "blogList";
	}

	/**
	 * 编辑博客页面
	 * 
	 * @author hehao
	 * @param request
	 * @return
	 */
	@RequestMapping({ "/edit" })
	public String edit(@RequestParam Long id, Model model, HttpServletRequest request) {
		Map<String, Object> data = this.blogDataServiceImpl.selectByPrimaryKey(id);
		if (data.get("author").equals(SecurityUtils.getSubject().getPrincipal())) {
			model.addAttribute("data", data);
			return "etitBlog";
		}
		return "unauthorized";
	}

	/**
	 * 提交博客编辑
	 * 
	 * @author hehao
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/edit" }, params = { "oper=do" })
	public String doEdit(@RequestParam Long id, HttpServletRequest request) {
		Map<String, Object> data = this.blogDataServiceImpl.selectByPrimaryKey(id);
		if (data.get("author").equals(SecurityUtils.getSubject().getPrincipal())) {
			HashMap<String, Object> queryMap = new HashMap<String, Object>();
			ParameterUtil.getAndPutAttr(request, queryMap, "title");
			ParameterUtil.getAndPutAttr(request, queryMap, "description");
			queryMap.put("author", SecurityUtils.getSubject().getPrincipal());
			ParameterUtil.getAndPutAttr(request, queryMap, "content");
			ParameterUtil.getAndPutAttr(request, queryMap, "md");
			queryMap.put("id", id);
			this.blogDataServiceImpl.updateByPrimaryKey(queryMap);
		}
		return "redirect:/view?oper=detail&id=" + id;
	}
}