package com.changingedu.httpclient;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.Socket;

public class MySSLConnectionSocketFactory extends SSLConnectionSocketFactory {

    public MySSLConnectionSocketFactory(SSLContext sslContext) {
        super(sslContext, ALLOW_ALL_HOSTNAME_VERIFIER);
    }

    @Override
    public Socket createSocket(HttpContext context) throws IOException {
        InetSocketAddress socksAddr = (InetSocketAddress) context.getAttribute("socket.address");
        if (socksAddr != null){
            Proxy proxy = new Proxy(Type.SOCKS, socksAddr);
            return new Socket(proxy);
        }
        return super.createSocket(context);
    }
}
