package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
/*
 * 展示内容分类列表
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		// 1、取查询参数id，parentId
		// 2、根据parentId查询tb_content_category，查询子节点列表。
		TbContentCategoryExample example = new TbContentCategoryExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		// 3、得到List<TbContentCategory>
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		// 4、把列表转换成List<EasyUITreeNode>
		List<EasyUITreeNode> result = new ArrayList<>();
		
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			//添加到列表
			result.add(node);
		}
		return result;
	}

	@Override
	public TaotaoResult addContentCategory(long parentId, String name) {
		// 1、接收两个参数：parentId、name
		// 2、向tb_content_category表中插入数据。
		// a)创建一个TbContentCategory对象
		TbContentCategory tbContentCategory = new TbContentCategory();
		// b)补全TbContentCategory对象的属性
		tbContentCategory.setIsParent(false);
		tbContentCategory.setName(name);
		tbContentCategory.setParentId(parentId);
		//排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列
		tbContentCategory.setSortOrder(1);
		//状态：可选值：1(正常)，2(删除)
		tbContentCategory.setStatus(1);
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		// C)向tb_content_category表中插入数据
		tbContentCategoryMapper.insert(tbContentCategory);
		//判断父节点的isparent是否为true,不是true需要改为true
		TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if(!category.getIsParent()){
			category.setIsParent(true);
			//更新父节点
			tbContentCategoryMapper.updateByPrimaryKey(category);
		}
		//4.需要主键返回
		//5.返回taotaoResult,其中包装TbContentCategory对象
		return TaotaoResult.ok(tbContentCategory);
	}

}
