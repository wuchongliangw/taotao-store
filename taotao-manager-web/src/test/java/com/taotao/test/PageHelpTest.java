package com.taotao.test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;

public class PageHelpTest {
		
	@Test
	public void test() throws Exception{
//		//初始化spring容器
//		ApplicationContext applicationContext = 
//				new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
//		//获得mapper的代理对象
//		TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);
//		//设置分页信息
//		PageHelper.startPage(1, 30);
//		//执行查询
//		TbItemExample example = new TbItemExample();
//		List<TbItem> list = itemMapper.selectByExample(example);
//		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
//		System.out.println("总记录数："+pageInfo.getTotal());
//		System.out.println("总页数："+pageInfo.getPages());
//		System.out.println(list.size());
//		
		
	}
}
