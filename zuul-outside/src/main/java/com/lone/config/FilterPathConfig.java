package com.lone.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: zuul-sample
 * @Package: com.lone.config
 * @ClassName: FilterPathConfig
 * @Description:
 * @Author: meihao
 * @CreateDate: 2019/7/4 23:31
 */

@Component
@ConfigurationProperties(prefix = "zuul.filter")
public class FilterPathConfig {
    String pre;
    String route;
    String post;
    String error;

    public String getPre() {
        return pre;
    }

    public void setPre(String pre) {
        this.pre = pre;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "FilterPathConfig{" +
                "pre='" + pre + '\'' +
                ", route='" + route + '\'' +
                ", post='" + post + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
