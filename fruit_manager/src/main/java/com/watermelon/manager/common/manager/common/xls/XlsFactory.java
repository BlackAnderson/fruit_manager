package com.watermelon.manager.common.manager.common.xls;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.dom4j.Document;
import org.dom4j.Element;

import com.watermelon.manager.common.manager.common.util.BeanUtil;
import com.watermelon.manager.common.manager.common.util.FileUtil;
import com.watermelon.manager.common.manager.common.util.XlsUtil;
import com.watermelon.manager.common.manager.common.util.XmlUtil;


public class XlsFactory {
	private static ConcurrentHashMap<String, String[]> titleMap = new ConcurrentHashMap();
	private static ConcurrentHashMap<String, Map<String, String>> propertiesMap = new ConcurrentHashMap();

	static {
		init();
	}

	public static boolean writeXls(List<?> list, OutputStream os) {
		Object instance = list.get(0);
		String className = instance.getClass().getSimpleName();
		String[] title = (String[]) titleMap.get(className);
		Map properties = (Map) propertiesMap.get(className);
		ArrayList datas = new ArrayList(list.size());
		int titleLength = title.length;
		Iterator arg8 = list.iterator();

		while (arg8.hasNext()) {
			Object o = arg8.next();
			Object[] data = new Object[titleLength];

			for (int index = 0; index < titleLength; ++index) {
				data[index] = BeanUtil.getProperty(o, (String) properties.get(title[index]));
			}

			datas.add(data);
		}

		XlsUtil.writeXls(title, datas, os);
		return false;
	}

	private static void init() {
		Document document = XmlUtil.readDocument(FileUtil.XML_PATH + "xlsConfig.xml");
		Iterator iter = document.getRootElement().elementIterator();

		while (iter.hasNext()) {
			Element e = (Element) iter.next();
			String className = e.element("className").getText().trim();
			String[] title = e.element("title").getText().trim().split(",");
			String[] property = e.element("property").getText().trim().split(",");
			titleMap.put(className, title);
			HashMap propertyMap = new HashMap();

			for (int i = 0; i < title.length; ++i) {
				propertyMap.put(title[i], property[i]);
			}

			propertiesMap.put(className, propertyMap);
		}

	}
}