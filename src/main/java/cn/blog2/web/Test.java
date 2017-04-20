package cn.blog2.web;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Test {
	Logger logger = LoggerFactory.getLogger(Test.class);

	@RequestMapping({ "/velocity.shtml" })
	public String vmtest(Model model, HttpServletRequest request) {
		model.addAttribute("hello", "hello,velocity");
		return "velocity.vm";
	}
}