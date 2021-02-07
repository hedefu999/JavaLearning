package com.changingedu.httpclient;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.net.Proxy.Type;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HttpClientManager {
    private CloseableHttpClient httpClient;
    private PoolingHttpClientConnectionManager connectionManager;
    private ScheduledExecutorService idleConnCloseExecutor = Executors.newSingleThreadScheduledExecutor();

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
        //创建不需要走代理的ConnectionManager
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(1023);
        connectionManager.setDefaultMaxPerRoute(200);
        httpClientBuilder.setDefaultHeaders(Collections.singletonList(new BasicHeader("identity","java_learning")));
        httpClient = httpClientBuilder.setConnectionManager(connectionManager).build();
    }

    //如果需要走代理 socks proxy
    private void buildConnectionManagerWithSocksProxy(String proxyHost, Integer proxyPort, HttpClientBuilder httpClientBuilder, String protocol){
        if (StringUtils.isEmpty(proxyHost) || proxyPort == null){
            return;
        }
        InetSocketAddress socketAddr = new InetSocketAddress(proxyHost, proxyPort);
        DefaultRoutePlanner routePlanner = new DefaultRoutePlanner(null){
            @Override
            public HttpRoute determineRoute(HttpHost host, HttpRequest request, HttpContext context) throws HttpException {
                String hostName = (host != null ? host.getHostName() : null);
                //下述几种情况不走socks代理
                if (hostName == null) return super.determineRoute(host, request, context);
                if (hostName.toLowerCase().endsWith("idc.cedu.cn")) return super.determineRoute(host, request, context);
                if (hostName.toLowerCase().endsWith("idc.changingedu.com")) return super.determineRoute(host, request, context);
                //走socks代理
                context.setAttribute("socks.address",socketAddr);
                return super.determineRoute(host, request, context);
            }
        };
        httpClientBuilder.setRoutePlanner(routePlanner);
        SSLContext context = SSLContexts.createSystemDefault();
        if (!StringUtils.isEmpty(protocol)){
            try {
                context = SSLContexts.custom()
                        .useProtocol(protocol)
                        .loadTrustMaterial(null, new TrustStrategy() {
                            @Override
                            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                                return true;
                            }
                        })
                        .build();
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                e.printStackTrace();
            }
        }
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
                        .register("https", new SSLConnectionSocketFactory(context){
                            @Override
                            public Socket createSocket(HttpContext context) throws IOException {
                                InetSocketAddress socksAddr = (InetSocketAddress) context.getAttribute("socket.address");
                                if (socksAddr != null){
                                    Proxy proxy = new Proxy(Type.SOCKS, socksAddr);
                                    return new Socket(proxy);
                                }
                                return super.createSocket(context);
                            }
                        })
                        .build();
        connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(1023);
        connectionManager.setDefaultMaxPerRoute(200);
    }

    //每30秒关闭一次http连接
    public void closeIdleStart(){
        idleConnCloseExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                connectionManager.closeExpiredConnections();
                connectionManager.closeIdleConnections(30, TimeUnit.SECONDS);
            }
        }, 30, 30, TimeUnit.SECONDS);
    }

    //先关闭维护线程，再关闭connection manager
    public void destroy(){
        idleConnCloseExecutor.shutdown();
        connectionManager.shutdown();
    }

    //测试下面提供的http请求方法
    public static void main(String[] args) {
        String url = "";
        HttpClientManager clientManager = new HttpClientManager();
        clientManager.getRequestReturnStream(url, null, null);
    }

    public ByteArrayOutputStream getRequestReturnStream(String url, Map<String,String> params, Map<String,String> headers){
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(url);
        } catch (URISyntaxException e) { e.printStackTrace(); }
        if (params != null){
            for (Map.Entry<String, String> entry : params.entrySet()){
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        URI uri = null;
        try {
            uri = uriBuilder.build();
        } catch (URISyntaxException e) { e.printStackTrace(); }
        HttpGet httpGet = new HttpGet(uri);
        if (headers != null){
            headers.entrySet().stream().forEach(entry -> httpGet.addHeader(entry.getKey(), entry.getValue()));
        }
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            HttpEntity entity = httpResponse.getEntity();
            if (statusCode == 200){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    entity.writeTo(baos);
                } catch (IOException e) { e.printStackTrace(); }
                return baos;
            } else {
                InputStream content = entity.getContent();
                byte[] bytes = new byte[content.available()];
                int read = content.read(bytes);
                String s = new String(bytes);
                System.out.println(s);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            httpGet.abort();
        }
    }


}
