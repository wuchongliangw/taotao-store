package com.taotao.test;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class mq {
	@Test
	public void testQueueProduce() throws Exception{
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//从容器中获得JMSTemplate对象
		JmsTemplate template = applicationContext.getBean(JmsTemplate.class);
		//从容器中获得一个Destination对象
		Queue queue = (Queue) applicationContext.getBean("queueDestination");
		//使用template对象发送消息，需要知道Destination
		template.send(queue,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage("spring mq text11");
				return textMessage;
			}
		});
	}
}
