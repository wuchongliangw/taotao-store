package com.taotao.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;

/*
 * 商品搜索功能dao
 */
@Repository
public class ItemSearchDao {

	@Autowired
	private SolrServer solrServer;
	
	public SearchResult search(SolrQuery query) throws Exception{
		//根据query对象查询索引库
		QueryResponse response = solrServer.query(query);
		//取商品列表
		SolrDocumentList solrDocumentList = response.getResults();
		//商品列表
		List<SearchItem> itemList = new ArrayList<>();
		for (SolrDocument solrDocument : solrDocumentList) {
			SearchItem searchItem = new SearchItem();
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setPrice((long) solrDocument.get("item_price"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			//取高亮显示
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String itemTitle = "";
			//有高亮显示的内容
			if(list != null && list.size() > 0){
				itemTitle = list.get(0);
			}else{
				itemTitle = (String) solrDocument.get("item_title");
			}
			searchItem.setTitle(itemTitle);
			//添加到商品列表
			itemList.add(searchItem);
		}
		SearchResult searchResult = new SearchResult();
		//商品列表
		searchResult.setItemList(itemList);
		//总记录数
		searchResult.setRecordCount(solrDocumentList.getNumFound());
		return searchResult;
	}
}
