package com.lone.java.com.netflix.zuul.context;


import java.io.Serializable;
import java.util.Map;

/**
 * @ProjectName: zuul-sample
 * @Package: com.lone.java.com.netflix.zuul.context
 * @ClassName: RequestContextEntity
 * @Description:
 * @Author: meihao
 * @CreateDate: 2019/7/7 23:25
 */
public class RequestContextEntity implements Serializable{

    private static final long serialVersionUID = -2803475275156413633L;

    String requestMethod;

    String requestUri;

    Map<String,Object> requestHeader;

    Map<String,Object> requestBody;

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public Map<String, Object> getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(Map<String, Object> requestHeader) {
        this.requestHeader = requestHeader;
    }

    public Map<String, Object> getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Map<String, Object> requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public String toString() {
        return "RequestContextEntity{" +
                "requestMethod='" + requestMethod + '\'' +
                ", requestUri='" + requestUri + '\'' +
                ", requestHeader=" + requestHeader +
                ", requestBody=" + requestBody +
                '}';
    }
}
