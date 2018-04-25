package com.taotao.search.service;

import com.taotao.common.pojo.TaotaoResult;

/**
 * 索引库维护，添加商品信息到索引库
 * <p>Title: SearchItemService</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
public interface SearchItemService {

	TaotaoResult importAllItems() throws Exception;
}
