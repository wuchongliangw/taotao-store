package com.taotao.service;

import com.taotao.common.pojo.EasyUIResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {

	EasyUIResult getItemList(int page,int rows);
	TaotaoResult addItem(TbItem tbItem,String desc);
	//根据商品id查询商品信息
	TbItem selectItemById(long itemId);
	//根据商品id查询商品详情
	TbItemDesc selectItemDescById(long itemId);
}
