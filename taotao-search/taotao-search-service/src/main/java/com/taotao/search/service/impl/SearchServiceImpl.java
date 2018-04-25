package com.taotao.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.ItemSearchDao;
import com.taotao.search.service.SearchService;

/**
 * 商品搜索服务实现类
 * <p>Title: SearchServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */

@Service
public class SearchServiceImpl implements SearchService{
	
	@Autowired
	private ItemSearchDao itemSearchDao;

	@Override
	public SearchResult search(String query, int page, int rows) throws Exception {
		//1、创建一个SolrQuery对象。
		SolrQuery solrQuery = new SolrQuery();
		//2、设置查询条件
		solrQuery.setQuery(query);
		//3、设置分页条件
		solrQuery.setStart((page-1)*rows);
		solrQuery.setRows(rows);
		//4、需要指定默认搜索域。
		solrQuery.set("df", "item_title");
		//5、设置高亮
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		//6、执行查询，调用SearchDao。得到SearchResult 
		SearchResult searchResult = itemSearchDao.search(solrQuery);
		//7、需要计算总页数。
		//获得总记录数
		long recordCount = searchResult.getRecordCount();
		//总记录数/每页显示的记录数=总页数
		long pageCount = recordCount / rows;
		if(recordCount%rows > 0){
			pageCount++;
		}
		searchResult.setPageCount(pageCount);
		//8、返回SearchResult 
		return searchResult;
	}

}
