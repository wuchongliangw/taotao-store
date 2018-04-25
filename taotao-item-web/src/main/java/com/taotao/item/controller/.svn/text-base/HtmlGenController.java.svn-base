package com.taotao.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class HtmlGenController {
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@RequestMapping("/genhtml")
	@ResponseBody
	public String genHtml() throws Exception{
		//1、从spring容器中获得FreeMarkerConfigurer对象。
		//2、从FreeMarkerConfigurer对象中获得Configuration对象。
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		//3、使用Configuration对象获得Template对象。
		Template template = configuration.getTemplate("hello.ftl");
		//4、创建数据集
		Map dataModel = new HashMap<>();
		dataModel.put("hello", "1000");
		//5、创建输出文件的Writer对象。
		FileWriter writer = new FileWriter(new File("E:/temp/spring-freemarker.html"));
		//6、调用模板对象的process方法，生成文件。
		template.process(dataModel, writer);
		//7、关闭流。
		writer.close();
		return "OK";

	}
}
