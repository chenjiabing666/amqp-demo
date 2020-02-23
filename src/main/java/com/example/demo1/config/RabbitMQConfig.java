package com.example.demo1.config;

import com.example.demo1.callback.MyConfirmCallback;
import com.example.demo1.callback.MyReturnCallBack;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

import java.util.Random;
import java.util.UUID;

/**
 * @Description TODO
 * @Author CJB
 * @Date 2020/2/20 21:16
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("127.0.0.1");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        //设置消息发送ack，默认none
//        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        //设置开启发布消息的Return监听
//        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory ) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }



    @Bean
    public RabbitTemplate rabbitTemplate(MyConfirmCallback myConfirmCallback, MyReturnCallBack myReturnCallBack) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMandatory(true);
//        template.setReturnCallback(myReturnCallBack);
//        template.setConfirmCallback(myConfirmCallback);
        template.setChannelTransacted(true);
        return template;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        RabbitTransactionManager manager = new RabbitTransactionManager();
        manager.setConnectionFactory(connectionFactory());
        return manager;
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        //添加监听的队列
        container.addQueueNames("queue1");
        //设置自动ack消息，默认是自动
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //设置消费者的并发数量
        container.setConcurrentConsumers(1);
        //设置单个消费请求能够处理的消息条数，默认250
        container.setPrefetchCount(250);
        //开启事务
        container.setChannelTransacted(true);
        //设置事务管理器
        container.setTransactionManager(transactionManager());
        //设置最大的并发数量
        container.setMaxConcurrentConsumers(10);
        //设置消费者的tag的生成策略，队列的名字+"_"+UUID
        container.setConsumerTagStrategy(queue -> queue+"_"+ UUID.randomUUID().toString());
        container.setMessageListener(customMessageListener1());
        return container;
    }

    /**
     * 自定义Message监听器
     * @return
     */
    @Bean
    public MessageListener customMessageListener(){
        return msg-> {
            System.err.println("消费者："+new String(msg.getBody()));
            System.out.println(1/0);
        };
    }

    @Bean
    public ChannelAwareMessageListener customMessageListener1(){
        return (msg,chanel)->{
            System.err.println("message:"+new String(msg.getBody()));
            System.err.println("properties:"+msg.getMessageProperties().getDeliveryTag());
            chanel.basicAck(msg.getMessageProperties().getDeliveryTag(),false);
            System.out.println(1/0);
        };
    }
}