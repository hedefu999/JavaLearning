package com.changingedu.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.Socket;
import java.util.Collections;

public class HttpClientManager {
    private CloseableHttpClient httpClient = null;
    private PoolingHttpClientConnectionManager connectionManager;

    public HttpClientManager() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(3000)
                .setConnectTimeout(3000)
                .setSocketTimeout(5000)
                .build();
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy(){
            @Override //修改默认的保活策略
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                long keepAliveDuration = super.getKeepAliveDuration(response, context);
                if (keepAliveDuration == -1) keepAliveDuration = 5000;
                return keepAliveDuration;
            }
        });
        httpClientBuilder.setConnectionReuseStrategy(new DefaultConnectionReuseStrategy(){
            @Override //修改默认的链接复用策略
            public boolean keepAlive(HttpResponse response, HttpContext context) {
                boolean keepAlive = false;
                if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()){
                    keepAlive = super.keepAlive(response, context);
                }
                return super.keepAlive(response, context);
            }
        });

        if (false){ //如果需要走代理
            SSLContext ctx = SSLContexts.createSystemDefault();
            Registry<ConnectionSocketFactory> registry =
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", new PlainConnectionSocketFactory(){
                                @Override
                                public Socket createSocket(HttpContext context) throws IOException {
                                    InetSocketAddress socksAddr = (InetSocketAddress) context.getAttribute("socks.address");
                                    if (socksAddr != null){
                                        Proxy proxy = new Proxy(Type.SOCKS, socksAddr);
                                        return new Socket(proxy);
                                    }
                                    return super.createSocket(context);
                                }
                            })
                            .register("https", new MySSLConnectionSocketFactory(ctx))
                            .build();
            connectionManager = new PoolingHttpClientConnectionManager(registry);
            connectionManager.setMaxTotal(1023);
            connectionManager.setDefaultMaxPerRoute(200);
        }else {
            connectionManager = new PoolingHttpClientConnectionManager();
            connectionManager.setMaxTotal(1023);
            connectionManager.setDefaultMaxPerRoute(200);
        }
        httpClientBuilder.setDefaultHeaders(Collections.singletonList(new BasicHeader("identity","java_learning")));

    }
}
