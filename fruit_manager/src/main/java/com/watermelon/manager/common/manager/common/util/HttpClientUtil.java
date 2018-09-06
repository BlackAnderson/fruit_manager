package com.watermelon.manager.common.manager.common.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpClientUtil {
	private static Log log = LogFactory.getLog(HttpClientUtil.class);

	public static <T> T getClientResult(String chargeURL, String action, Map<String, Object> parameters) {
		StringBuffer urlName = new StringBuffer(chargeURL);
		urlName.append("/").append(action);
		if (parameters != null && parameters.size() > 0) {
			boolean url = true;

			Entry e;
			for (Iterator httpURLConnection = parameters.entrySet().iterator(); httpURLConnection.hasNext(); urlName
					.append((String) e.getKey()).append("=").append(e.getValue())) {
				e = (Entry) httpURLConnection.next();
				if (url) {
					urlName.append("?");
					url = false;
				} else {
					urlName.append("&&");
				}
			}
		}

		try {
			URL url1 = new URL(urlName.toString());
			URLConnection e1 = url1.openConnection();
			HttpURLConnection httpURLConnection1 = (HttpURLConnection) e1;
			httpURLConnection1.setRequestMethod("GET");
			httpURLConnection1.connect();
			ObjectInputStream ois = new ObjectInputStream(e1.getInputStream());
			Object o = ois.readObject();
			if (o == null) {
				return null;
			} else {
				ois.close();
				return (T) o;
			}
		} catch (Exception arg8) {
			log.error("连接接口出错---->" + urlName.toString(), arg8);
			return null;
		}
	}

	public static <T> T getClientResultPost(String chargeURL, String action, Map<String, Object> parameters) {
		StringBuffer urlName = new StringBuffer(chargeURL);
		urlName.append("/").append(action);
		StringBuffer paramStr = null;
		Entry url;
		if (parameters != null && parameters.size() > 0) {
			for (Iterator e = parameters.entrySet().iterator(); e.hasNext(); paramStr.append((String) url.getKey())
					.append("=").append(url.getValue())) {
				url = (Entry) e.next();
				if (paramStr == null) {
					paramStr = new StringBuffer();
				} else {
					paramStr.append("&");
				}
			}
		}

		try {
			URL url1 = new URL(urlName.toString());
			URLConnection e1 = url1.openConnection();
			HttpURLConnection httpURLConnection = (HttpURLConnection) e1;
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			OutputStream os = httpURLConnection.getOutputStream();
			os.write(paramStr.toString().getBytes("utf-8"));
			os.close();
			ObjectInputStream ois = new ObjectInputStream(e1.getInputStream());
			Object o = ois.readObject();
			if (o == null) {
				return null;
			} else {
				ois.close();
				return (T) o;
			}
		} catch (Exception arg10) {
			log.error("连接接口出错---->" + urlName.toString(), arg10);
			return null;
		}
	}

	public static String getHttpResultPost(String url, String action, Map<String, Object> parameters) {
		if (url != null && !url.equals("")) {
			StringBuffer urlName = new StringBuffer(url);
			urlName.append("/").append(action);
			StringBuffer paramStr = null;
			Entry e;
			if (parameters != null && parameters.size() > 0) {
				for (Iterator post = parameters.entrySet().iterator(); post.hasNext(); paramStr
						.append((String) e.getKey()).append("=").append(e.getValue())) {
					e = (Entry) post.next();
					if (paramStr == null) {
						paramStr = new StringBuffer();
					} else {
						paramStr.append("&");
					}
				}
			}

			try {
				HttpClient e1 = new HttpClient();
				PostMethod post1 = new PostMethod(urlName.toString());
				Iterator arg7 = parameters.keySet().iterator();

				while (arg7.hasNext()) {
					String key = (String) arg7.next();
					post1.addParameter(key, parameters.get(key).toString());
				}

				e1.executeMethod(post1);
				return post1.getResponseBodyAsString();
			} catch (Exception arg8) {
				log.error("连接接口出错---->" + urlName.toString());
				return null;
			}
		} else {
			return null;
		}
	}

	public static String getHttpResultGet(String url, String action, Map<String, Object> parameters) {
		if (url != null && !url.equals("")) {
			StringBuffer urlName = new StringBuffer(url);
			urlName.append("/").append(action);
			if (parameters != null && parameters.size() > 0) {
				boolean e = true;

				Entry get;
				for (Iterator arg5 = parameters.entrySet().iterator(); arg5.hasNext(); urlName
						.append((String) get.getKey()).append("=").append(get.getValue())) {
					get = (Entry) arg5.next();
					if (e) {
						urlName.append("?");
						e = false;
					} else {
						urlName.append("&&");
					}
				}
			}

			try {
				HttpClient e1 = new HttpClient();
				GetMethod get1 = new GetMethod(urlName.toString());
				e1.executeMethod(get1);
				return get1.getResponseBodyAsString();
			} catch (Exception arg6) {
				log.error("连接接口出错---->" + urlName.toString());
				return null;
			}
		} else {
			return null;
		}
	}

	public static void writeResult(HttpServletResponse response, Object result) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream());
		oos.writeObject(result);
		oos.close();
	}
}