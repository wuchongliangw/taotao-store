package com.taotao.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;


/**
 * 添加商品后，生成商品详情静态页面
 * <p>Title: HtmlGenListener</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
public class HtmlGenListener implements MessageListener{

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Value("${HTML_OUT_PATH}")
	private String HTML_OUT_PATH;
	
	
	@Override
	public void onMessage(Message message) {
		try {
			//接收商品id
			TextMessage textMessage = (TextMessage) message;
			String strItemId = textMessage.getText();
			Long itemId = new Long(strItemId);
			//查询商品信息
			TbItem tbItem = itemService.selectItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc tbItemDesc = itemService.selectItemDescById(itemId);
			//创建数据集
			Map data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", tbItemDesc);
			//创建商品详情页面的模板
			
			//指定文件输出目录
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.htm");
			FileWriter out = new FileWriter(new File(HTML_OUT_PATH + strItemId + ".html"));
			template.process(data, out);
			//关闭流
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
