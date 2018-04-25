package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

/*
 * 商品列表查询的service
 */

@Service
public class ItemServiceImpl implements ItemService{

	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource(name="topicDestination")
	private Topic topicDestination;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY;
	@Value("${REDIS_ITEM_EXPIRE}")
	private Integer REDIS_ITEM_EXPIRE;
	
	@Override
	public EasyUIResult getItemList(int page, int rows) {
		//设置分页处理信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIResult result = new EasyUIResult();
		result.setTotal((int)pageInfo.getTotal());
		result.setRows(list);
		return result;
	}

	/*
	 * 添加商品
	 */
	@Override
	public TaotaoResult addItem(TbItem tbItem, String desc) {
		final long itemId = IDUtils.genItemId();
		//设置商品的主键
		tbItem.setId(itemId);
		//设置商品的状态
		tbItem.setStatus((byte) 1);
		Date date = new Date();
		tbItem.setCreated(date);
		tbItem.setUpdated(date);
		//向商品表插入数据
		tbItemMapper.insert(tbItem);
		//创建一个TbItemDesc对象
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(date);
		tbItemDesc.setUpdated(date);
		tbItemDescMapper.insert(tbItemDesc);
		//添加完商品后向MQ发送消息
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId +"");
				return textMessage;
			}
		});
		return TaotaoResult.ok();
	}

	//根据商品id查询商品信息
	@Override
	public TbItem selectItemById(long itemId) {
		//先查询缓存
		try {
			String itemJson = jedisClient.get(REDIS_ITEM_KEY+":"+itemId+":BASE");
			if(StringUtils.isNotBlank(itemJson)){
				return JsonUtils.jsonToPojo(itemJson, TbItem.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
		//查询不到缓存后，将数据库查询到的信息添加到缓存中
		try {
			//将数据保存到缓存中
			jedisClient.set(REDIS_ITEM_KEY+":"+itemId+":BASE",JsonUtils.objectToJson(item));
			//设置缓存的过期时间
			jedisClient.expire(REDIS_ITEM_KEY+":"+itemId+":BASE", REDIS_ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	//根据商品id查询商品详情
	@Override
	public TbItemDesc selectItemDescById(long itemId) {
		//先查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY+":"+itemId+":DESC");
			if(StringUtils.isNotBlank(json)){
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return itemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		//查询不到缓存后，将数据库查询到的信息添加到缓存中
		try {
			jedisClient.set(REDIS_ITEM_KEY+":"+itemId+":DESC", JsonUtils.objectToJson(itemDesc));
			jedisClient.expire(REDIS_ITEM_KEY+":"+itemId+":DESC", REDIS_ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

}
