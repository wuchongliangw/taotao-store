package com.taotao.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

@Controller
public class CartController {

	@Autowired
	private ItemService itemService;
	
	@Value("${COOKIE_CART_KEY}")
	private String COOKIE_CART_KEY;
	
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	
	@RequestMapping("/cart/add/{itemId}")
	public String addItemCart(@PathVariable Long itemId,Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//1、从cookie中查询商品列表。
		List<TbItem> list = getCartList(request);
		//2、判断商品在商品列表中是否存在。
		boolean hasItem = false;
		for (TbItem tbItem : list) {
			if (tbItem.getId() == itemId.longValue()) {
				//如果存在，商品数量相加
				//3、如果存在，商品数量相加。
				tbItem.setNum(tbItem.getNum()+num);
				hasItem = true;
				break;
			}
		}
		if (!hasItem) {
			//4、不存在，根据商品id查询商品信息。
			TbItem tbItem = itemService.selectItemById(itemId);
			
			//取第一张照片
			String image = tbItem.getImage();
			if (StringUtils.isNotBlank(image)) {
	             String[] images = image.split(",");
	             tbItem.setImage(images[0]); 
			}
			//设置购买数量
			tbItem.setNum(num);
			//5、把商品添加到购车列表。
			list.add(tbItem);
		}
		//6、把购车商品列表写入cookie。
		CookieUtils.setCookie(request, response, COOKIE_CART_KEY, 
				JsonUtils.objectToJson(list),COOKIE_CART_EXPIRE,true);
		return "cartSuccess";
	}
	
	//从cookie中取购物车列表
	public List<TbItem> getCartList(HttpServletRequest request){
		String json = CookieUtils.getCookieValue(request, COOKIE_CART_KEY,true);
		//判断json是否为null
		if (StringUtils.isNotBlank(json)) {
			//把json转换成商品列表返回
			List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
			return list;
		}
		return new ArrayList<>();
	}
	
	//展示购物车商品列表
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request){
		List<TbItem> cartList = getCartList(request);
		request.setAttribute("cartList", cartList);
		return "cart";
	}
	
	//修改商品数量
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public TaotaoResult updateNum(@PathVariable Long itemId,Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//从cookie中取购物车列表
		List<TbItem> list = getCartList(request);
		for (TbItem tbItem : list) {
			if (tbItem.getId() == itemId.longValue()) {
				//修改商品的数量
				tbItem.setNum(num);
			}
		}
		//把商品列表写回cookie
		CookieUtils.setCookie(request, response, COOKIE_CART_KEY, 
				JsonUtils.objectToJson(list),COOKIE_CART_EXPIRE,true);
		return TaotaoResult.ok();
	}
	
	//删除购物车商品
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCart(@PathVariable Long itemId,HttpServletRequest request,
			HttpServletResponse response){
		List<TbItem> cartList = getCartList(request);
		for (TbItem tbItem : cartList) {
			if (tbItem.getId() == itemId.longValue()) {
				//删除商品
				cartList.remove(tbItem);
				break;
			}
		}
		CookieUtils.setCookie(request, response, COOKIE_CART_KEY, 
				JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
		return "redirect:/cart/cart.html";
		
	}
}
