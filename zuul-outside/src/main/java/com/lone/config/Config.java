package com.lone.config;

import com.lone.core.InitializeServletListener;
import com.lone.core.SyncZuulServlet;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: zuul-sample
 * @Package: com.lone.config
 * @ClassName: Config
 * @Description:
 * @Author: meihao
 * @CreateDate: 2019/7/4 20:08
 */
@Configuration
public class Config {

    @Bean
    public ServletListenerRegistrationBean<ServletContextListener> setStartupServletContextListener(){
        ServletListenerRegistrationBean<ServletContextListener> result = new ServletListenerRegistrationBean<>();
        result.setListener(new InitializeServletListener());
        result.setOrder(20);
        return result;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        SyncZuulServlet servlet = new SyncZuulServlet();
        ServletRegistrationBean bean = new ServletRegistrationBean(servlet);
        bean.setOrder(40);
        bean.setName("SyncZuulServlet");
        List<String> urlPattern = new ArrayList<>();
        urlPattern.add("/hi/*");
        bean.setUrlMappings(urlPattern);
        return bean;
    }

//    @Bean
//    public CachingConnectionFactory connectionFactory(){
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setAddresses("127.0.0.1:5672");
//        connectionFactory.setUsername("guest");
//        connectionFactory.setPassword("guest");
//        connectionFactory.setVirtualHost("/");
//        return connectionFactory;
//    }
//
//    @Bean
//    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
//        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
//        rabbitAdmin.setAutoStartup(true);
//        return rabbitAdmin;
//    }

//    @Bean
//    public Queue inQueue(){
//        return new Queue("in_test");
//    }
//
//    @Bean
//    public Queue outQueue(){
//        return new Queue("out");
//    }

//    @Bean
//    public DirectExchange inExchange() {
//        return new DirectExchange("inExchange", true, false);
//    }
//
//    @Bean
//    //把立即消费的队列和立即消费的exchange绑定在一起
//    public Binding immediateBinding() {
//        return BindingBuilder.bind(inQueue()).to(inExchange()).with("routingKey_in");
//    }
}
