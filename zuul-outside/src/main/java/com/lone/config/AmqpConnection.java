package com.lone.config;

import com.lone.java.com.netflix.zuul.ZuulRunner;
import com.lone.java.com.netflix.zuul.context.RequestContext;
import com.lone.java.com.netflix.zuul.exception.ZuulException;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ProjectName: zuul-sample
 * @Package: com.lone.config
 * @ClassName: AmqpConnection
 * @Description:
 * @Author: meihao
 * @CreateDate: 2019/7/5 16:58
 */
public class AmqpConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmqpConnection.class);

    private static ConnectionFactory connectionFactory;

    public static void init(){
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
    };

    public static Connection getConnection() throws IOException, TimeoutException {
        if (connectionFactory == null){
            throw new RuntimeException("connection not init ...");
        }

        return  connectionFactory.newConnection();
    }

    public static void initReceive(String queueName) throws IOException, TimeoutException {
        //创建一个新的连接
        Connection connection = connectionFactory.newConnection();

        //创建一个通道
        Channel channel = connection.createChannel();

        //声明要关注的队列
        channel.queueDeclare(queueName, true, false, false, null);
        System.out.println("Customer Waiting Received messages");

        //DefaultConsumer类实现了Consumer接口，通过传入一个频道，
        // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
        Consumer consumer = new DefaultConsumer(channel) {
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                RequestContext.getCurrentContext().set("message",message);

                System.out.println("Customer Received '" + message + "'");

                try {
                    postRoute();
                } catch (ZuulException e) {
                    error(e);
                    return;
                }
            }
        };
        //自动应答
        channel.basicConsume(queueName, true, consumer);
    }

//    private ZuulRunner zuulRunner = new ZuulRunner();

    private static void postRoute() throws ZuulException {
        new ZuulRunner().postRoute();
    }

    private static void error(ZuulException e) {
        try {
            RequestContext.getCurrentContext().setThrowable(e);
            new ZuulRunner().error();
        } catch (Throwable t) {
            LOGGER.error(e.getMessage(), e);
        }finally{
        }
    }
}
