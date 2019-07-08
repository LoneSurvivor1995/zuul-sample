package com.lone.filter.route;

import com.lone.java.com.netflix.zuul.ZuulFilter;
import com.lone.java.com.netflix.zuul.context.RequestContext;
import com.lone.java.com.netflix.zuul.exception.ZuulException;
import com.lone.utils.MqUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * @ProjectName: zuul-sample
 * @Package: com.lone.filter.error
 * @ClassName: WriteMqFilter
 * @Description:
 * @Author: meihao
 * @CreateDate: 2019/7/4 23:49
 */

public class WriteMqFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WriteMqFilter.class);

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        LOGGER.info("this is writeMqFilter...");
        // TODO queue动态配置
//        amqpTemplate.convertAndSend("in","hello " + new Date());

        RequestContext context = RequestContext.getCurrentContext();
//        String message = "hello " + new Date();

        try {
            MqUtil.mqSend("in_test",context);
//            MqUtil.send();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

//        AmqpTemplateFactory.getAmqpTemplate().convertAndSend("inExchange", "routingKey_in", message);

        return true;
    }
}
