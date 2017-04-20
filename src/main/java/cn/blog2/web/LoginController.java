package cn.blog2.web;

import cn.blog2.service.BlogDataService;
import cn.blog2.util.ParameterUtil;
import cn.blog2.util.VerifyCodeUtil;
import com.alibaba.fastjson.JSON;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

	@Autowired
	BlogDataService blogDataServiceImpl;
	Logger logger = LoggerFactory.getLogger(LoginController.class);

	/**
	 * 无权限页面
	 * 
	 * @author hehao
	 * @param request
	 * @return
	 */
	@RequestMapping({ "/unauthorized" })
	public String unauthorized(HttpServletRequest request) {
		return "unauthorized";
	}

	/**
	 * 登录退出
	 * 
	 * @author hehao
	 * @param request
	 * @return
	 */
	@RequestMapping({ "/logout" })
	public String doLogout(HttpServletRequest request, Model model) {
		SecurityUtils.getSubject().logout();
		return "redirect:/view?oper=list";
	}

	/**
	 * 检查是否已登录
	 * 
	 * @author hehao
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/checkLogined" })
	public String checkLogined(HttpServletRequest request) {
		String status;
		if (SecurityUtils.getSubject().isAuthenticated())
			status = (String) SecurityUtils.getSubject().getPrincipal();
		else
			status = "unlogin";
		return status;
	}

	/**
	 * 获取验证码图片和文本(验证码文本会保存在HttpSession中)
	 * 
	 * @author hehao
	 */
	@RequestMapping({ "/getVerifyCodeImage" })
	public void getVerifyCodeImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 设置页面不缓存
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		String verifyCode = VerifyCodeUtil.generateTextCode(VerifyCodeUtil.TYPE_ALL_MIXED, 4, null);
		// 将验证码放到Session里面
		SecurityUtils.getSubject().getSession().setAttribute("verifyCode", verifyCode);
		logger.info("本次生成的验证码为[" + verifyCode + "],已存放到Session中");
		// 设置输出的内容的类型为JPEG图像
		response.setContentType("image/jpeg");
		BufferedImage bufferedImage = VerifyCodeUtil.generateImageCode(verifyCode, 90, 30, 3, true, Color.WHITE,
				Color.BLACK, null);
		// 写给浏览器
		ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
	}

	/**
	 * 实际的登录代码 如果登录成功，跳转至首页；登录失败，则将失败信息反馈对用户
	 * 
	 * @author hehao
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/login" }, params = { "oper=do" }, produces = { "text/html;charset=UTF-8;" })
	public String doLogin(HttpServletRequest request, HttpServletResponse response, Model model) {
		String msg = "";
		boolean success;
		Map<String, Object> result = new HashMap<String, Object>();
		String user_name = request.getParameter("user_name");
		String password = request.getParameter("password");
		String verifyCode = request.getParameter("verifyCode");
		this.logger.info("登录信息:" + user_name + ";" + password + ";" + verifyCode);
		UsernamePasswordToken token = new UsernamePasswordToken(user_name, password);
		token.setRememberMe(false);
		Subject subject = SecurityUtils.getSubject();
		String realVerifyCode = (String) subject.getSession().getAttribute("verifyCode");
		if (!verifyCode.equalsIgnoreCase(realVerifyCode)) {
			msg = "验证码错误.";
			success = false;
			this.logger.info(msg);
		} else {
			try {
				subject.login(token);
				// 判断该用户是否已经被验证通过
				if (subject.isAuthenticated()) {
					SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
					// 获取保存的URL
					if ((savedRequest == null) || (savedRequest.getRequestUrl() == null)) {
						success = true;
					} else {
						this.logger.info("转发地址:" + savedRequest.getRequestUrl());
						// response.sendRedirect(savedRequest.getRequestUrl());
						success = true;
					}
				} else {
					msg = "登录失败.";
					success = false;
					this.logger.info(msg);
				}
			} catch (IncorrectCredentialsException e) {
				msg = "登录密码错误.";
				success = false;
				this.logger.info(msg);
			} catch (ExcessiveAttemptsException e) {
				msg = "登录失败次数过多";
				success = false;
				this.logger.info(msg);
			} catch (LockedAccountException e) {
				msg = "帐号已被锁定.";
				success = false;
				this.logger.info(msg);
			} catch (DisabledAccountException e) {
				msg = "帐号已被禁用.";
				success = false;
				this.logger.info(msg);
			} catch (ExpiredCredentialsException e) {
				msg = "帐号已过期.";
				success = false;
				this.logger.info(msg);
			} catch (UnknownAccountException e) {
				msg = "帐号不存在.";
				success = false;
				this.logger.info(msg);
			} catch (UnauthorizedException e) {
				msg = "您没有得到相应的授权.";
				success = false;
				this.logger.info(msg);
			}
		}
		result.put("msg", msg);
		result.put("success", Boolean.valueOf(success));
		return JSON.toJSONString(result);
	}

	/**
	 * 实际的注册代码 如果注册成功，跳转至登录页面；注册失败，则将失败信息反馈对用户
	 * 
	 * @author hehao
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/regist" }, params = { "oper=do" }, produces = { "text/html;charset=UTF-8;" })
	public String doRegist(HttpServletRequest request, HttpServletResponse response, Model model) {
		String msg = "";
		boolean success;
		Map<String, Object> result = new HashMap<String, Object>();
		String verifyCode = request.getParameter("verifyCode");
		Subject subject = SecurityUtils.getSubject();
		String realVerifyCode = (String) subject.getSession().getAttribute("verifyCode");
		if (!verifyCode.equalsIgnoreCase(realVerifyCode)) {
			msg = "验证码错误.";
			success = false;
			this.logger.info(msg);
		} else {
			Map<String, Object> queryMap = new HashMap<String, Object>();
			ParameterUtil.getAndPutAttr(request, queryMap, "user_name");
			String password = request.getParameter("password");
			SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
			String salt = secureRandom.nextBytes(3).toHex();// 一个Byte占两个字节，此处生成的3字节，字符串长度为6
			Md5Hash hash = new Md5Hash(password, salt, 2);
			this.logger.info("加密前:" + password + ";加密后:" + hash.toString() + ";盐:" + salt);
			queryMap.put("password", hash.toString());
			queryMap.put("salt", salt);
			queryMap.put("role_name", "members");
			this.blogDataServiceImpl.regist(queryMap);
			success = true;
		}
		result.put("msg", msg);
		result.put("success", Boolean.valueOf(success));
		return JSON.toJSONString(result);
	}
}