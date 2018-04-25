package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserRegisterService;
/**
 * 用户注册数据检验的Service
 * <p>Title: UserRegisterServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class UserRegisterServiceImpl implements UserRegisterService {

	@Autowired
	private TbUserMapper tbUserMapper;
	
	@Override
	public TaotaoResult checkUserInfo(String param, int type) {
		//从tb_user中查询数据
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//查询条件根据参数动态生成
		//1,2,3代表username,phone,email
		if(type == 1){
			criteria.andUsernameEqualTo(param);
		}else if(type == 2){
			criteria.andPhoneEqualTo(param);
		}else if(type == 3){
			criteria.andEmailEqualTo(param);
		}
		//执行查询
		List<TbUser> list = tbUserMapper.selectByExample(example);
		//判断查询结果，如果查询到数据返回false
		if(list == null || list.size() == 0){
			//如果没有返回true
			return TaotaoResult.ok(true);
		}
		return TaotaoResult.ok(false);
	}

	//用户注册
	@Override
	public TaotaoResult createUser(TbUser user) {
		//1、使用TbUser接收提交的请求。
		if(StringUtils.isBlank(user.getUsername())){
			return TaotaoResult.build(400, "用户名不能为空");
		}
		if(StringUtils.isBlank(user.getPassword())){
			return TaotaoResult.build(400, "密码不能为空");
		}
		//检验用户名是否可用
		TaotaoResult result = checkUserInfo(user.getUsername(), 1);
		if (!(boolean) result.getData()) {
			return TaotaoResult.build(400, "用户名重复");
		}
		//检验手机号是否可用
		result = checkUserInfo(user.getPhone(), 2);
		if (!(boolean) result.getData()) {
			return TaotaoResult.build(400, "此手机号已经被使用");
		}
		//检验邮箱是否可用
		result = checkUserInfo(user.getEmail(), 3);
		if (!(boolean) result.getData()) {
			return TaotaoResult.build(400, "此邮箱已经被使用");
		}
		//2、补全TbUser其他属性。
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//3、密码要进行MD5加密。
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		//4、把用户信息插入到数据库中。
		tbUserMapper.insert(user);
		//5、返回TaotaoResult。
		return TaotaoResult.ok();
	}

}
