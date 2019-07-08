package com.lone.mq;

import com.lone.java.com.netflix.zuul.context.RequestContextEntity;
import com.netflix.zuul.http.ZuulServlet;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * @ProjectName: zuul-sample
 * @Package: com.lone.mq
 * @ClassName: Receiver
 * @Description:
 * @Author: meihao
 * @CreateDate: 2019/7/5 18:25
 */

@Component
public class Receiver {

    @Autowired
    AmqpTemplate amqpTemplate;

    @RabbitListener(queues = "in_test")
    public void process(byte[] body) throws ServletException, IOException {
        RequestContextEntity contextEntity = (RequestContextEntity)SerializationUtils.deserialize(body);

        System.out.println(contextEntity);

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet("http://localhost:9000/" + contextEntity.getRequestUri());

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
//                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
//                String responseBody = EntityUtils.toString(responseEntity);

                // 发送对象类型的消息
//                Event event = new Event(); //实现Serializable接口
//                event.setId(1101);
//                event.setName("printscreen event");
//                event.setCreateTimestamp(System.currentTimeMillis());
//                event.setUpdateTimestamp(System.currentTimeMillis());

//                System.out.println(queue.getName());
                amqpTemplate.convertAndSend("out_test", EntityUtils.toString(responseEntity)); // 队列名称，消息
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        }finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



//        System.out.println(new String(msg.getBody()));
//        Object object = SerializationUtils.deserialize(body);
//        System.out.println(object);
//        RequestContext context = (RequestContext)object ;
//        new ZuulServlet().service(getRequest(),getResponse());
    }

}
