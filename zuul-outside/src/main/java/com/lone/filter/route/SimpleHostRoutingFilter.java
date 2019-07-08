package com.lone.filter.route;

import com.lone.java.com.netflix.zuul.ZuulFilter;
import com.lone.java.com.netflix.zuul.exception.ZuulException;

/**
 * @ProjectName: zuul-sample
 * @Package: com.lone.filter.route
 * @ClassName: SimpleHostRoutingFilter
 * @Description:
 * @Author: meihao
 * @CreateDate: 2019/7/7 17:40
 */
public class SimpleHostRoutingFilter extends ZuulFilter{
    @Override
    public String filterType() {
        return null;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        return null;
    }
}
