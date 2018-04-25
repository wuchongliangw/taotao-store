package com.taotao.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.Ad1Node;
import com.taotao.pojo.TbContent;

/**
 * 展示首页的处理
 */
@Controller
public class IndexController {

	@Autowired
	private ContentService contentService;
	
	@Value("${AD1_CID}")
	private Long AD1_CID;
	@Value("${AD1_HEIGHT}")
	private Integer AD1_HEIGHT;
	@Value("${AD1_WIDTH}")
	private Integer AD1_WIDTH;
	@Value("${AD1_HEIGHT_B}")
	private Integer AD1_HEIGHT_B;
	@Value("${AD1_WIDTH_B}")
	private Integer AD1_WIDTH_B;

	
	@RequestMapping("/index")
	public String showIndex(Model model){
		//System.out.println(AD1_CID);
		
		List<TbContent> list = contentService.getContentList(AD1_CID);
		//转换成Ad1NodeList
		List<Ad1Node> ad1Nodes = new ArrayList<>();
		for (TbContent tbContent : list) {
			Ad1Node ad1Node = new Ad1Node();
			ad1Node.setAlt(tbContent.getTitle());
			ad1Node.setHeight(AD1_HEIGHT);
			ad1Node.setWidth(AD1_WIDTH);
			ad1Node.setHeightB(AD1_HEIGHT_B);
			ad1Node.setWidthB(AD1_WIDTH_B);
			ad1Node.setHref(tbContent.getUrl());
			ad1Node.setSrc(tbContent.getPic());
			ad1Node.setSrcB(tbContent.getPic2());
			ad1Nodes.add(ad1Node);
		}
		model.addAttribute("ad1", JsonUtils.objectToJson(ad1Nodes));
		return "index";
	}
}
