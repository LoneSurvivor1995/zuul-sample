package com.lone.utils;

import com.lone.config.AmqpConnection;
import com.lone.java.com.netflix.zuul.context.RequestContext;
import com.lone.java.com.netflix.zuul.context.RequestContextEntity;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ProjectName: zuul-sample
 * @Package: com.lone.utils
 * @ClassName: MqUtil
 * @Description:
 * @Author: meihao
 * @CreateDate: 2019/7/5 17:09
 */
public class MqUtil {

    public static void mqSend(String queueName,RequestContext context) throws IOException, TimeoutException {

//        String contextStr = JSON.toJSONString(context);
//
//        System.out.println(contextStr);
//
//        RequestContext requestContext = (RequestContext) JSONObject.parse(contextStr);
//
//        System.out.println(requestContext);

        Channel channel = AmqpConnection.getConnection().createChannel();

        channel.queueDeclare(queueName,true,false,false,null);

        RequestContextEntity requestContextEntity = new RequestContextEntity();
        requestContextEntity.setRequestMethod(context.getRequest().getMethod());
        requestContextEntity.setRequestUri(context.getRequest().getRequestURI());
//        requestContextEntity.setRequestHeader(context.getRequest().getHeader("s"));
        channel.basicPublish("",queueName,null,SerializationUtils.serialize(requestContextEntity));
//        channel.basicPublish("",queueName,null,("hello " + new Date()).getBytes("UTF-8"));
        channel.close();
        AmqpConnection.getConnection().close();
    }


    public static void main(String[] args) throws IOException, TimeoutException {
//    public static void send() throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();

        //设置RabbitMQ相关信息
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");


        //创建一个新的连接
        Connection connection = connectionFactory.newConnection();

        //创建一个通道
        Channel channel = connection.createChannel();

        String QUEUE_NAME = "in_test";

        // 声明一个队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        //发送消息到队列中
        String message = "Hello RabbitMQ";
//        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));

        channel.basicPublish("",QUEUE_NAME, null, SerializationUtils.serialize(new RequestContext()));
        System.out.println("Producer Send +'" + message + "'");

        //关闭通道和连接
        channel.close();
//        connection.close();
    }
}
