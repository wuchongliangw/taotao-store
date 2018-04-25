package com.taotao.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserLoginService;
/**
 * 用户登录的Service
 * <p>Title: UserLoginServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {

	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${USER_INFO}")
	private String USER_INFO;
	
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public TaotaoResult login(String username, String password) {
		//检验用户名是否正确
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		TbUser user = list.get(0);
		//检验密码是否正确
		if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		//登录成功后生成token,token相当于原来的jsessionid,字符串，可以使用uuid
		String token = UUID.randomUUID().toString();
		//把用户信息保存到redis,key就是token,value就是TbUser对象转换成json
		//使用String类型保存Session信息，可以使用“前缀：token”为key
		user.setPassword(null);
		jedisClient.set(USER_INFO+":"+token, JsonUtils.objectToJson(user));
		//设置key的过期时间
		jedisClient.expire(USER_INFO+":"+token, SESSION_EXPIRE);
		//返回TaotaoResult,包装token
		return TaotaoResult.ok(token);
	}

	//根据token查询用户信息
	@Override
	public TaotaoResult getUserByToken(String token) {
		//向缓存中查询token对应key的值
		String json = jedisClient.get(USER_INFO+":"+token);
		if (StringUtils.isBlank(json)) {
			return TaotaoResult.build(400, "此用户登录已经过期");
		}
		//重置token的过期时间
		jedisClient.expire(USER_INFO+":"+token, SESSION_EXPIRE);
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return TaotaoResult.ok(user);
	}

	//安全退出
	@Override
	public TaotaoResult logout(String token) {
		jedisClient.expire(USER_INFO+":"+token, 0);
		return TaotaoResult.ok();
	}
	
	
}
