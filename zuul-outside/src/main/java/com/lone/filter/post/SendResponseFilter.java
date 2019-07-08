package com.lone.filter.post;

import com.lone.common.Constants;
import com.lone.java.com.netflix.zuul.ZuulFilter;
import com.lone.java.com.netflix.zuul.constants.ZuulHeaders;
import com.lone.java.com.netflix.zuul.context.RequestContext;
import com.lone.java.com.netflix.zuul.exception.ZuulException;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;


public class SendResponseFilter extends ZuulFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendResponseFilter.class);

    private static final DynamicIntProperty BUFFER_SIZE = DynamicPropertyFactory.getInstance().getIntProperty(Constants.ZUUL_INITIAL_STREAM_BUFFER_SIZE, 1024);

    private static final DynamicBooleanProperty CONTENT_LENGTH = DynamicPropertyFactory.getInstance().getBooleanProperty(Constants.ZUUL_SET_CONTENT_LENGTH, false);

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 100;
    }

    @Override
    public boolean shouldFilter() {
//        RequestContext context = RequestContext.getCurrentContext();
//        return context.getResponseBody() != null || context.getResponseDataStream() != null;
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        LOGGER.info("this is SendResponseFilter ...");
//        RequestContext context = RequestContext.getCurrentContext();
//        Transaction tran = Cat.getProducer().newTransaction("SendResponseFilter", context.getRequest().getRequestURL().toString());

//        HttpServletResponse servletResponse = context.getResponse();
//        OutputStream outStream = null;
//        try {
//            outStream = servletResponse.getOutputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String responseBody = context.getResponseBody();
//        InputStream responseDataStream = context.getResponseDataStream();
//
//        InputStream inStream = null;
//        try{
//            addResponseHeaders();
//            if(responseBody != null){
//                inStream = new ByteArrayInputStream(responseBody.getBytes());
////                writeResponse(inStream, outStream);
//            }else{
//                inStream = responseDataStream;
//            }
//
//            if (!context.getResponseGZipped()) {
////                writeResponse(inStream, outStream);
//            }
//            // if origin response is gzipped, and client has not requested gzip, decompress stream
//            // before sending to client
//            // else, stream gzip directly to client
//            boolean isGZipRequested = false;
//            final String requestEncoding = context.getRequest().getHeader(ZuulHeaders.ACCEPT_ENCODING);
//            if (requestEncoding != null && requestEncoding.contains("gzip")) {
//                isGZipRequested = true;
//            }
//            if (isGZipRequested) {
//                servletResponse.setHeader(ZuulHeaders.CONTENT_ENCODING, "gzip");
//            } else {
//                try {
//                    inStream = new GZIPInputStream(responseDataStream);
//                } catch (IOException e) {
//                    LOGGER.info("gzip expected but not received assuming unencoded response" + RequestContext.getCurrentContext().getRequest().getRequestURL().toString(),e);
//                }
//            }
//            writeResponse(inStream, outStream);

//            String message = RequestContext.getCurrentContext().get("message").toString();
//
//            outStream.write(message.getBytes(), 0, 1024);
//            outStream.flush();
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }finally{
//            try {
//                if (inStream != null) {
//                    inStream.close();
//                }
//                if (outStream != null) {
//                    outStream.flush();
//                    outStream.close();
//                }
//            } catch (IOException e) {
//            }
//        }
        return true;
    }

    private void addResponseHeaders() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletResponse servletResponse = context.getResponse();
        List<Pair<String, String>> zuulResponseHeaders = context.getZuulResponseHeaders();

//        zuulResponseHeaders?.each {
//            Pair<String, String> it ->
//            servletResponse.addHeader(it.first(), it.second())
//        }

        for (Pair<String,String> pair : zuulResponseHeaders){
            servletResponse.addHeader(pair.first(),pair.second());
        }

        RequestContext ctx = RequestContext.getCurrentContext();
        Integer contentLength = ctx.getOriginContentLength().intValue();

        // only inserts Content-Length if origin provides it and origin response is not gzipped
        if (CONTENT_LENGTH.get()){
            if (contentLength != null && !ctx.getResponseGZipped())
                servletResponse.setContentLength(contentLength);
        }
    }

    private void writeResponse(InputStream zin, OutputStream out) {
        long start = System.currentTimeMillis();

        long readCost = 0; // store the cost for reading data from server
        long writeCost = 0; // store the cost for sending data to client

        long begin = 0;
        long end = 0;
        try {
            byte[] bytes = new byte[BUFFER_SIZE.get()];
            int bytesRead = -1;

            begin = System.currentTimeMillis();
            while ((bytesRead = zin.read(bytes)) != -1) {
                end = System.currentTimeMillis();
                readCost += (end-begin);

                begin = end;
                try {
                    out.write(bytes, 0, bytesRead);
                    out.flush();
                } catch (IOException e) {
                } finally {
                    end = System.currentTimeMillis();
                    writeCost += (end-begin);
                }

                // doubles buffer size if previous read filled it
                if (bytesRead == bytes.length) {
                    bytes = new byte[bytes.length * 2];
                }

                begin = end;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            RequestContext.getCurrentContext().set("sendResponseCost", System.currentTimeMillis() - start);
            RequestContext.getCurrentContext().set("sendResponseCost:read", readCost);
            RequestContext.getCurrentContext().set("sendResponseCost:write", writeCost);
        }
    }
}