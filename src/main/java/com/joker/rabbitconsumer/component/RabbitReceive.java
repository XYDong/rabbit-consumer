package com.joker.rabbitconsumer.component;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * 消息消费
 */
@Component
public class RabbitReceive {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue-1", durable = "true"),
            exchange = @Exchange(name = "exchange-1", durable = "true",
                    type = "topic", ignoreDeclarationExceptions = "true"),
            key = "springboot.*"
            )
    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {
        // 1. 收到消息以后进行业务端消费处理
        System.err.println("------------------------------------");
        System.err.println("消费消息：" + message.getPayload());
        // 2. 处理成功后，获取deliveryTag 并进行手工的ack处理，因为配置文件中配置的是手工签收
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag,false);
    }

}
