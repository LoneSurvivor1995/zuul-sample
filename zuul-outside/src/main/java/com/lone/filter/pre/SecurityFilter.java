package com.lone.filter.pre;

import com.lone.config.FilterPathConfig;
import com.lone.java.com.netflix.zuul.ZuulFilter;
import com.lone.java.com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ProjectName: zuul-sample
 * @Package: com.lone.filter.pre
 * @ClassName: SecurityFilter
 * @Description:
 * @Author: meihao
 * @CreateDate: 2019/7/4 22:26
 */
public class SecurityFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    public String filterType() {
        return "pre";
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
        LOGGER.info("this is SecurityFilter ...");
        return true;
    }
}
