package com.taotao.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.ItemMapper;
import com.taotao.search.service.SearchItemService;
/**
 * 索引库维护，添加商品信息到索引库
 * <p>Title: SearchItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class SearchItemServiceImpl implements SearchItemService{

	@Autowired
	private SolrServer solrServer;
	
	@Autowired
	private ItemMapper itemMapper;
	
	@Override
	public TaotaoResult importAllItems() throws Exception {
		List<SearchItem> itemList = itemMapper.getItemList();
		for (SearchItem searchItem : itemList) {
			//为每个商品创建一个SolrInputDocument对象
			SolrInputDocument document = new SolrInputDocument();
			//为文档添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			document.addField("item_desc", searchItem.getItem_desc());
			//向索引库中添加文档
			solrServer.add(document);
		}
		//提交修改
		solrServer.commit();
		return TaotaoResult.ok();
	}

}
