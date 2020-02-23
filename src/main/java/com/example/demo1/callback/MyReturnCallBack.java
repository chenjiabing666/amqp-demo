package com.example.demo1.callback;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description ReturnListener的监听，处理发送消息时路由不可达的消息
 * @Author CJB
 * @Date 2020/2/21 17:04
 */
@Component
public class MyReturnCallBack implements RabbitTemplate.ReturnCallback {
    /**
     * 在消息路由不可达会回调此方法，用于处理这些消息，比如记录日志，消息补偿等等操作
     * @param message  投递的消息
     * @param replyCode  响应的状态吗
     * @param replyText  响应的文本
     * @param exchange  交换机
     * @param routingKey  路由键
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.err.println("message："+new String(message.getBody()));
        System.err.println("replyCode："+replyCode);
        System.err.println("replyText："+replyText);
        System.err.println("exchange："+exchange);
        System.err.println("routingKey："+routingKey);
    }
}