package com.watermelon.manager.common.manager.common.util;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlUtil {
	private static Log log = LogFactory.getLog(XmlUtil.class);

	public static Document readDocument(String file) {
		SAXReader reader = new SAXReader();
		Document document = null;

		try {
			document = reader.read(new File(file));
		} catch (DocumentException arg3) {
			log.error("XmlUtil.readDocument", arg3);
		}

		return document;
	}

	public static void writeDocument(Document document, OutputStream os) {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");

		try {
			XMLWriter e = new XMLWriter(new OutputStreamWriter(os, "UTF-8"), format);
			e.write(document);
			e.close();
		} catch (Exception arg3) {
			log.error("XmlUtil.writeDocument", arg3);
		}

	}

	public static <T> Document createDocument(List<T> list) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		String[] attributes = (String[]) null;
		if (list != null && !list.isEmpty()) {
			Iterator arg4 = list.iterator();

			while (arg4.hasNext()) {
				Object instance = (Object) arg4.next();
				Element data = root.addElement("data");
				int i;
				if (attributes == null) {
					Field[] attribute = instance.getClass().getDeclaredFields();
					attributes = new String[attribute.length];

					for (i = 0; i < attribute.length; ++i) {
						attributes[i] = attribute[i].getName();
					}
				}

				String[] arg9 = attributes;
				int arg8 = attributes.length;

				for (i = 0; i < arg8; ++i) {
					String arg14 = arg9[i];

					try {
						Object e = BeanUtil.getProperty(instance, arg14);
						Element e1 = data.addElement(arg14);
						if (e != null) {
							String text;
							if (e instanceof Date) {
								text = DateUtil.date2String((Date) e, "yyyy-MM-dd HH:mm:ss");
							} else {
								text = "" + e;
							}

							e1.setText(text);
						}
					} catch (Exception arg13) {
						log.error("XmlUtil.createDocument", arg13);
						return null;
					}
				}
			}
		}

		return document;
	}
}