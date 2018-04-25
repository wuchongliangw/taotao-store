package com.taotao.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.jedis.JedisClient;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

/**
 * 创建订单的Service
 * <p>Title: OrderServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbOrderMapper tbOrderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	
	@Value("${ORDER_GEN_KEY}")
	private String ORDER_GEN_KEY;
	@Value("${ORDER_ID_BEGIN}")
	private String ORDER_ID_BEGIN;
	@Value("${ORDER_ITEM_ID_GEN_KEY}")
	private String ORDER_ITEM_ID_GEN_KEY;
	
	@Override
	public TaotaoResult createOrder(OrderInfo orderInfo) {
		//生成订单id
		//如果订单号不存在
		if (!jedisClient.exists(ORDER_GEN_KEY)) {
			//设置订单号的初始值
			jedisClient.set(ORDER_GEN_KEY, ORDER_ID_BEGIN);
		}
		
		Long orderId = jedisClient.incr(ORDER_GEN_KEY);
		//设置订单号
		orderInfo.setOrderId(orderId.toString());
		//设置邮费
		orderInfo.setPostFee("0");
		//设置订单状态
		orderInfo.setStatus(1);
		Date date = new Date();
		orderInfo.setCreateTime(date);
		orderInfo.setUpdateTime(date);
		//向订单表中插入数据
		tbOrderMapper.insert(orderInfo);
		//获取订单明细，向订单明细表中插入数据
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		
		//这里有个bug,取出来的orderItems是空的
		
		for (TbOrderItem tbOrderItem : orderItems) {
			//设置商品id
			Long id = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY);
			tbOrderItem.setId(id.toString());
			//设置商品的订单id
			tbOrderItem.setOrderId(orderId.toString());
			//向订单明细表中插入数据
			orderItemMapper.insert(tbOrderItem);
		}
		//向订单物流数据表插入数据
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId.toString());
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		orderShippingMapper.insert(orderShipping);
		return TaotaoResult.ok(orderId.toString());
	}

}
