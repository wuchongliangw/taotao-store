package com.taotao.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.ItemMapper;

public class ItemAddListener implements MessageListener{

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			//接收消息
			TextMessage textMessage = (TextMessage) message;
			String strItemId = textMessage.getText();
			Long itemid = new Long(strItemId);
			//根据id向数据库查询商品信息
			SearchItem searchItem = itemMapper.getItemById(itemid);
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
			//提交修改
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
