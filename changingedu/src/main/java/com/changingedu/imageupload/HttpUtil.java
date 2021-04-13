package com.changingedu.imageupload;

//import com.qingqing.teachercommanderweb.constants.

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {
	public static final int connectionTimeout = 5000;

	private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

	public static final String postHttp(String url, String params, String encoding) throws UnsupportedEncodingException {
		log.debug("send post request to " + url);
		String responseMsg = "";
		//构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);
		//设置编码格式
		httpClient.getParams().setContentCharset(encoding);
		//构造PostMethod的实例
		PostMethod postMethod = new PostMethod(url);
		InputStream requestIO = new ByteArrayInputStream(params.getBytes("UTF-8"));
		InputStreamRequestEntity entity = new InputStreamRequestEntity(requestIO);
		postMethod.setRequestEntity(entity);
		try {
			//执行postMethod,调用http接口
			httpClient.executeMethod(postMethod);
			log.debug("response statusCode:" + postMethod.getStatusLine().getStatusCode());
			//读取内容
			responseMsg = postMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			//释放连接
			postMethod.releaseConnection();
		}
		log.debug("response message :" + responseMsg);
		return responseMsg;

	}

	public static final String postHttp(String url, InputStream inputStream) throws UnsupportedEncodingException {
		log.debug("send post request to " + url);
		String responseMsg = "";
		//构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);
		//构造PostMethod的实例
		PostMethod postMethod = new PostMethod(url);
		InputStreamRequestEntity entity = new InputStreamRequestEntity(inputStream);
		postMethod.setRequestEntity(entity);
		try {
			//执行postMethod,调用http接口
			httpClient.executeMethod(postMethod);
			log.debug("response statusCode:" + postMethod.getStatusLine().getStatusCode());
			//读取内容
			responseMsg = postMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			//释放连接
			postMethod.releaseConnection();
		}
		log.debug("response message :" + responseMsg);
		return responseMsg;

	}

	public static final String getHttp(String url, String params, String encoding) {
		log.debug("send get request to " + url);
		String responseMsg = "";
		//构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);
		//组装get请求url
		if (params != null) {
			url = url + "?" + params;
		}

		//构造getMethod的实例
		GetMethod getMethod = new GetMethod(url);
		//设置编码格式
		getMethod.getParams().setContentCharset(encoding);
		try {
			//执行getMethod,调用http接口
			httpClient.executeMethod(getMethod);
			log.debug("response statusCode:" + getMethod.getStatusLine().getStatusCode());
			if (200 == getMethod.getStatusCode()) {
				//处理返回的内容
				responseMsg = getMethod.getResponseBodyAsString();
			}
		} catch (HttpException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			//释放连接
			getMethod.releaseConnection();
		}
		log.debug("response message :" + responseMsg);
		return responseMsg;
	}

	/**
	* @Title: postImageHttp
	* @Description: TODO 图片上传
	* @param @param url
	* @param @param encoding
	* @param @param filename
	* @param @param bytes
	* @param @return
	* @param @throws FileNotFoundException    设定文件
	* @return String    返回类型
	* @throws
	 */
	public static final String postImageHttp(String url, String encoding, String filename, byte[] bytes) throws FileNotFoundException {
		String responseMsg="";
		//构造HttpClient的实例
		HttpClient httpClient=new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);
		//设置编码格式
		httpClient.getParams().setContentCharset(encoding);
		//构造PostMethod的实例
		PostMethod postMethod=new PostMethod(url);


		try {
			ByteArrayPartSource source=new ByteArrayPartSource(filename, bytes);
		            //FilePart：用来上传文件的类
		        FilePart fp = new FilePart("filedata", source);
		         Part[] parts = { fp };
		          //对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装
		         MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());
		         postMethod.setRequestEntity(mre);

			//执行postMethod,调用http接口
			httpClient.executeMethod(postMethod);
			log.info("send http request, url is "+url+", status code is"+postMethod.getStatusCode());
			if(200==postMethod.getStatusCode()){
				//读取内容
				responseMsg = postMethod.getResponseBodyAsString();
				// InputStream responseStream = postMethod.getResponseBodyAsStream();
				// byte[] bytes1 = new byte[responseStream.available()];
				// int read = responseStream.read(bytes1);
				// if (read == bytes1.length){
				// 	System.out.println("相等");
				// }
				// responseMsg  = new String(bytes1);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			//释放连接
			postMethod.releaseConnection();

		}
		return responseMsg;

	}

	public static final InputStream getImageHttp(String url, String params, String encoding){
		log.debug("send get request to "+url);
		InputStream in=null;
		//构造HttpClient的实例
		HttpClient httpClient=new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);
		//组装get请求url
		if(params!=null){
			url=url+"?"+params;
		}

		//构造getMethod的实例
        GetMethod getMethod = new GetMethod(url);
      //设置编码格式
        getMethod.getParams().setContentCharset(encoding);
        try {
        	//执行getMethod,调用http接口
			httpClient.executeMethod(getMethod);
			log.debug("response statusCode:"+getMethod.getStatusLine().getStatusCode());
			if(200==getMethod.getStatusCode()){
				in=getMethod.getResponseBodyAsStream();
			}
		} catch (HttpException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}finally{
			//释放连接
			getMethod.releaseConnection();
		}
        return in;
	}

	public static final byte[] postHttp(String url, Map<String, String[]> paramsMap, String encoding, int connectionTimeout) throws UnsupportedEncodingException {
		log.debug("send post request to " + url);
		byte[] responseMsg = new byte[]{};
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);
		httpClient.getParams().setContentCharset(encoding);
		PostMethod postMethod = new PostMethod(url);
		NameValuePair[] params = mapToNameValuePair(paramsMap);
		if(null != params) {
			postMethod.setQueryString(params);
		}
		try {
			httpClient.executeMethod(postMethod);
			log.debug("response statusCode:" + postMethod.getStatusLine().getStatusCode());
			responseMsg = postMethod.getResponseBody();
		} catch (HttpException var13) {
			log.error(var13.getMessage());
		} catch (IOException var14) {
			log.error(var14.getMessage());
		} finally {
			postMethod.releaseConnection();
		}
		return responseMsg;
	}

	public static NameValuePair[] mapToNameValuePair(Map<String, String[]> paramsMap) {
		if(null != paramsMap && !paramsMap.isEmpty()) {
			ArrayList params = new ArrayList();
			Iterator i$ = paramsMap.entrySet().iterator();

			while(true) {
				Entry entry;
				String[] values;
				do {
					if(!i$.hasNext()) {
						return (NameValuePair[])params.toArray(new NameValuePair[0]);
					}

					entry = (Entry)i$.next();
					values = (String[])entry.getValue();
				} while(null == values);

				String[] arr$ = values;
				int len$ = values.length;

				for(int i$1 = 0; i$1 < len$; ++i$1) {
					String value = arr$[i$1];
					params.add(new NameValuePair((String)entry.getKey(), value));
				}
			}
		} else {
			return null;
		}
	}

}
