package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;

/**
 * 商品搜索服务Controller
 * <p>Title: SearchController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@Value("${SEARCH_ROWS}")
	private Integer SEARCH_ROWS;
	
	@RequestMapping("/search")
	public String search(@RequestParam("q") String queryString,
			@RequestParam(defaultValue="1") Integer page,Model model) throws Exception{
		//处理搜索参数乱码问题
		queryString = new String(queryString.getBytes("iso8859-1"),"utf-8");
		SearchResult searchResult = searchService.search(queryString, page, SEARCH_ROWS);
		//返回页面需要的元素
		model.addAttribute("query", queryString);
		model.addAttribute("totalPages", searchResult.getPageCount());
		model.addAttribute("itemList", searchResult.getItemList());
		model.addAttribute("page", page);
		//测试发生异常
		//int i=10/0;
		return "search";
	}
}
