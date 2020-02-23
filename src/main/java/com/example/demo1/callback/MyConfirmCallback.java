package com.example.demo1.callback;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description 消息确认回调，在消息发出之后回调
 * @Author CJB
 * @Date 2020/2/21 15:36
 */
@Component
public class MyConfirmCallback implements RabbitTemplate.ConfirmCallback {

    /**
     *
     * @param correlationData  发送消息时携带的参数，在业务上能够唯一识别，比如主键id等
     * @param ack  消息是否发送成功的标志，true成功，false失败
     * @param cause 消息发送失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.err.println(correlationData.getId()+"---->"+ack+"--->"+cause);
        //消息投递失败执行逻辑，比如消息入库，设置失败标记等操作
        if (!ack){
            System.err.println("消息投递失败");
        }
    }
}