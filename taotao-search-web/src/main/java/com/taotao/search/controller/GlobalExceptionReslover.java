package com.taotao.search.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
/**
 * 全局异常处理
 * <p>Title: GlobalExceptionReslover</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
public class GlobalExceptionReslover implements HandlerExceptionResolver{

	Logger logger = Logger.getLogger(GlobalExceptionReslover.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception e) {
		//写日志文件
		logger.error("系统发生异常",e);
		//展示错误页面
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "网络发生异常，请稍后重试");
		modelAndView.setViewName("/error/exception");
		return modelAndView;
	}

}
