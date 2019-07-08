package com.lone.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

/**
 * @ProjectName: zuul-sample
 * @Package: com.lone.config
 * @ClassName: AmqpConfig
 * @Description:
 * @Author: meihao
 * @CreateDate: 2019/7/5 12:23
 */
public class AmqpConfig {

    private static final String FILE_NAME = "classpath:application.properties";

    private static final String QUEUE_NAME ="in";

//    public static void main(String[] args) throws IOException, TimeoutException {
////        ConnectionFactory connectionFactory = new ConnectionFactory();
////        connectionFactory.setHost("localhost");
////        connectionFactory.setHost("127.0.0.1");
////        connectionFactory.setPort(5672);
////        connectionFactory.setUsername("guest");
////        connectionFactory.setPassword("guest");
////
////        Connection connection = connectionFactory.newConnection();
//        Channel channel = connection.createChannel();
//
//        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
//        String msg = "hello rabbit";
//        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes("UTF-8"));
//        System.out.println("Producer Send +'" + msg + "'");
//        channel.close();
//        connection.close();
//    }


    public static void loadAmqpConfig() throws IOException {
        Properties pro = new Properties();
        FileInputStream in = new FileInputStream(FILE_NAME);
        pro.load(in);

        System.out.println(pro);
        in.close();
    }

}
