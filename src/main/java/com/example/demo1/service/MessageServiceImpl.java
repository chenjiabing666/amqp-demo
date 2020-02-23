package com.example.demo1.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @Description TODO
 * @Author CJB
 * @Date 2020/2/23 21:18
 */
@Service
public class MessageServiceImpl {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional
    public void sendMsg(String msg){
        //接收消息
        Message message1 = rabbitTemplate.receive("a_queue");
        System.err.println(new String(message1.getBody()));

        String queueName="direc_q_1";
        String exchangeName="direct_1";
        String  routingKey="direct";
        Message message = MessageBuilder.withBody(msg.getBytes()).andProperties(new MessageProperties()).build();
        rabbitTemplate.send(exchangeName,routingKey,message,new CorrelationData(UUID.randomUUID().toString()));
        System.out.println(1/0);
    }
}