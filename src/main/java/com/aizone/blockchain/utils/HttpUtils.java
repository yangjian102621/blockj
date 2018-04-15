package com.aizone.blockchain.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http 请求工具类
 * @author yangjian
 */
public class HttpUtils {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	/**
	 * 发送 post 请求
	 * @param url 请求路径
	 * @param params 请求参数
	 * @return
	 */
	public static String post(String url, Map params) throws IOException {

		String result = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		// 创建参数队列
		List<NameValuePair> args = new ArrayList<>();
		if (null != params) {
			params.forEach((k,v) -> {
				args.add(new BasicNameValuePair(k.toString(), v.toString()));
			});
		}

		UrlEncodedFormEntity uefEntity;
		uefEntity = new UrlEncodedFormEntity(args, "UTF-8");
		httpPost.setEntity(uefEntity);
		CloseableHttpResponse response = httpClient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		if (null != entity) {
			result =  EntityUtils.toString(entity, "UTF-8");
		}

		response.close();
		httpClient.close();

		return result;
	}

	/**
	 * 发送 get 请求
	 * @param url 请求路径
	 * @param params 请求参数
	 * @return
	 */
	public static String get(String url, Map params) throws IOException {
		String result = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(httpBuildQuery(url, params));
		CloseableHttpResponse response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		if (null != entity) {
			result =  EntityUtils.toString(entity, "UTF-8");
		}

		response.close();
		httpClient.close();

		return result;
	}

	/**
	 * GET 方式请求 json API， 并返回 Map 对象
	 * @param url
	 * @param params
	 * @return
	 */
	public static Map getJson(String url, Map params) throws IOException {
		String html = get(url, params);
		if (null != html) {
			return OBJECT_MAPPER.readValue(html, Map.class);
		}
		return null;
	}

	/**
	 * POST 方式请求 json API， 并返回 Map 对象
	 * @param url
	 * @param params
	 * @return
	 */
	public static Map postJson(String url, Map params) throws IOException {
		String html = post(url, params);
		if (null != html) {
			return OBJECT_MAPPER.readValue(html, Map.class);
		}
		return null;
	}

	/**
	 * 组装 Url 和参数
	 * @param url
	 * @param params
     * @return
     */
	public static String httpBuildQuery(String url, Map params) {
		if (null == params) {
			return url;
		}
		String newUrl;
		if (url.indexOf("?") == -1) {
			newUrl = url+"?";
		} else {
			newUrl = url+"&";
		}

		ArrayList<String> list = new ArrayList<>();
		params.forEach((k, v) -> {
			list.add(k+"="+v);
		});
		return newUrl + StringUtils.join(list, "&");
	}

	/**
	 * 网络文件下载
	 * @param url
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static boolean download(String url, String filePath) throws IOException {

		//如果原有文件存在，则先删除
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		FileOutputStream os = new FileOutputStream(filePath);

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		//循环读取网络数据，写入本地文件
		while (true) {
			byte[] bytes = new byte[1024*1000];
			int len = is.read(bytes);
			if (len >= 0){
				os.write(bytes, 0, len);
				os.flush();
			} else {
				break;
			}
		}
		is.close();
		os.close();

		response.close();
		httpClient.close();

		return true;
	}

}
