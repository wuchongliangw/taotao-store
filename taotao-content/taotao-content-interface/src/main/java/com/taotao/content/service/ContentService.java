package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {

	//添加分类列表
	TaotaoResult addContent(TbContent content);
	//根据分类id查询列表
	List<TbContent> getContentList(long cid);
}
