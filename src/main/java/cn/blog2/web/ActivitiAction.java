package cn.blog2.web;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class ActivitiAction {
	Logger logger = LoggerFactory.getLogger(ActivitiAction.class);

	/**
	 * 使用框架提供的自动建表（提供配置文件）---配置文件activiti-context.xml可以从框架提供的例子程序中获取
	 */
	// @Test
	public void test2() {
		String resource = "activiti-context.xml";// 配置文件名称
		String beanName = "processEngineConfiguration";// 配置id值
		ProcessEngineConfiguration conf = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource(resource, beanName);
		@SuppressWarnings("unused")
		ProcessEngine processEngine = conf.buildProcessEngine();
	}
}