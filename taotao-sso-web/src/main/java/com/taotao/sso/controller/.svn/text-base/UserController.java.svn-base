package com.taotao.sso.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserLoginService;
import com.taotao.sso.service.UserRegisterService;

/**
 * 用户管理的Controller
 * <p>Title: UserController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class UserController {

	@Autowired
	private UserRegisterService userRegisterService;
	@Autowired
	private UserLoginService userLoginService;
	
	@Value("${TT_TOKEN}")
	private String TT_TOKEN;
	
	//检验数据是否正确
	@RequestMapping(value="/user/check/{param}/{type}",method=RequestMethod.GET)
	@ResponseBody
	public TaotaoResult checkUserInfo(@PathVariable String param,@PathVariable Integer type){
		TaotaoResult result = userRegisterService.checkUserInfo(param, type);
		return result;
	}
	
	//注册用户
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createUser(TbUser user){
		TaotaoResult result = userRegisterService.createUser(user);
		return result;
	}
	
	//登录
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(String username,String password,HttpServletRequest request,HttpServletResponse response){
		TaotaoResult result = userLoginService.login(username, password);
		//从返回结果中取token,存入cookie,cookie要跨域
		String token = result.getData().toString();
		System.out.println(token);
		CookieUtils.setCookie(request, response, "TT_TOKEN", token);
		//响应数据
		return result;
	}
	
	//传统支持jsonp的方案
		/*@RequestMapping(value="/user/token/{token}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
		@ResponseBody
		public String getUserByToken(@PathVariable String token, String callback) {
			TaotaoResult result = userLoginService.getUserByToken(token);
			if (StringUtils.isNotBlank(callback)) {
				//客户端为jsonp请求。需要返回js代码
				String jsonResutl = callback + "(" + JsonUtils.objectToJson(result) + ");";  
				return jsonResutl;
			}
			return JsonUtils.objectToJson(result);
		}*/
		//方法二，从4.1以后版本才可以使用。
		@RequestMapping(value="/user/token/{token}")
		@ResponseBody
		public Object getUserByToken(@PathVariable String token, String callback) {
			TaotaoResult result = userLoginService.getUserByToken(token);
			if (StringUtils.isNotBlank(callback)) {
				//设置要包装的数据
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				//设置回调方法
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			}
			return result;
		}
	
	@RequestMapping("/user/logout/{token}")
	@ResponseBody
	public TaotaoResult logout(@PathVariable String token){
		TaotaoResult result = userLoginService.logout(token);
		return result;
	}
}
