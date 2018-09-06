package com.watermelon.manager.common.manager.common.util;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

public class SaveFile {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private static int BUFFER_SIZE = 8096;
	private Vector<String> vDownLoad = new Vector();
	private Vector<String> vFileList = new Vector();

	public void resetList() {
		this.vDownLoad.clear();
		this.vFileList.clear();
	}

	public void addItem(String url, String filename) {
		this.vDownLoad.add(url);
		this.vFileList.add(filename);
	}

	public void downLoadByList() {
		String url = null;
		String filename = null;

		for (int i = 0; i < this.vDownLoad.size(); ++i) {
			url = (String) this.vDownLoad.get(i);
			filename = (String) this.vFileList.get(i);

			try {
				this.saveToFile(url, filename);
			} catch (IOException arg4) {
				this.log.error("资源[{}]下载失败!!!", url);
			}
		}

		this.log.info("资源[{}]下载完成!!!", url);
	}

	public void saveToFile(String destUrl, String fileName) throws IOException {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		byte[] buf = new byte[BUFFER_SIZE];
		boolean size = false;
		url = new URL(destUrl);
		httpUrl = (HttpURLConnection) url.openConnection();
		httpUrl.connect();
		bis = new BufferedInputStream(httpUrl.getInputStream());
		fos = new FileOutputStream(fileName);
		this.log.info("正在获取文件[{}]", destUrl);

		int size1;
		while ((size1 = bis.read(buf)) != -1) {
			fos.write(buf, 0, size1);
		}

		fos.close();
		bis.close();
		httpUrl.disconnect();
		this.log.info("保存为文件[{}]", fileName);
	}

	public void saveToFile2(String destUrl, String fileName) throws IOException {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		byte[] buf = new byte[BUFFER_SIZE];
		boolean size = false;
		url = new URL(destUrl);
		httpUrl = (HttpURLConnection) url.openConnection();
		String authString = "50301:88888888";
		String auth = "Basic " + (new BASE64Encoder()).encode(authString.getBytes());
		httpUrl.setRequestProperty("Proxy-Authorization", auth);
		httpUrl.connect();
		bis = new BufferedInputStream(httpUrl.getInputStream());
		fos = new FileOutputStream(fileName);
		this.log.info("正在获取文件[{}]", destUrl);

		int size1;
		while ((size1 = bis.read(buf)) != -1) {
			fos.write(buf, 0, size1);
		}

		fos.close();
		bis.close();
		httpUrl.disconnect();
		this.log.info("保存为文件[{}]", fileName);
	}

	public void setProxyServer(String proxy, String proxyPort) {
		System.getProperties().put("proxySet", "true");
		System.getProperties().put("proxyHost", proxy);
		System.getProperties().put("proxyPort", proxyPort);
	}

	public void setAuthenticator(String uid, String pwd) {
		Authenticator.setDefault(new MyAuthenticator(uid, pwd));
	}

	public static void main(String[] argv) {
		SaveFile oInstance = new SaveFile();

		try {
			oInstance.setAuthenticator("downuser", "downuserpwd");
			oInstance.saveToFile("http://s1.mogong.37wan.com/mglog/loginLog.log", "./loginLog.log");
		} catch (Exception arg2) {
			System.out.println(arg2.getMessage());
		}

	}
}