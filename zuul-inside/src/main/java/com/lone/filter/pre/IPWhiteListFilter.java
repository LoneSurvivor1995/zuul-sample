package com.lone.filter.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: zuul-sample
 * @Package: com.lone.filter.pre
 * @ClassName: IPWhiteListFilter
 * @Description:
 * @Author: meihao
 * @CreateDate: 2019/7/5 18:17
 */

@Component
public class IPWhiteListFilter extends ZuulFilter{
    private static final Logger LOGGER = LoggerFactory.getLogger(IPWhiteListFilter.class);

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
        LOGGER.info("this is IPWhiteListFilter...");
        return null;
    }
}
