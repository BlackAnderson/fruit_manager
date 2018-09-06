package com.watermelon.manager.common.manager.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONObject;

public class Json {
	public static String converJson(Object obj) {
		return object2json(obj);
	}

	private static String object2json(Object obj) {
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			json.append("\"\"");
		} else if (!(obj instanceof String) && !(obj instanceof Integer) && !(obj instanceof Float)
				&& !(obj instanceof Boolean) && !(obj instanceof Short) && !(obj instanceof Double)
				&& !(obj instanceof Long) && !(obj instanceof BigDecimal) && !(obj instanceof BigInteger)
				&& !(obj instanceof Byte)) {
			if (obj instanceof Object[]) {
				json.append(array2json((Object[]) obj));
			} else if (obj instanceof List) {
				json.append(list2json((List) obj));
			} else if (obj instanceof Map) {
				json.append(map2json((Map) obj));
			} else if (obj instanceof Set) {
				json.append(set2json((Set) obj));
			} else {
				json.append(JSONObject.fromObject(obj).toString());
			}
		} else {
			json.append("\"").append(string2json(obj.toString())).append("\"");
		}

		return json.toString();
	}

	private static String list2json(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			Iterator arg2 = list.iterator();

			while (arg2.hasNext()) {
				Object obj = arg2.next();
				json.append(object2json(obj));
				json.append(",");
			}

			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}

		return json.toString();
	}

	private static String array2json(Object[] array) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (array != null && array.length > 0) {
			Object[] arg4 = array;
			int arg3 = array.length;

			for (int arg2 = 0; arg2 < arg3; ++arg2) {
				Object obj = arg4[arg2];
				json.append(object2json(obj));
				json.append(",");
			}

			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}

		return json.toString();
	}

	private static String map2json(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			Iterator arg2 = map.keySet().iterator();

			while (arg2.hasNext()) {
				Object key = arg2.next();
				json.append(object2json(key));
				json.append(":");
				json.append(object2json(map.get(key)));
				json.append(",");
			}

			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}

		return json.toString();
	}

	private static String set2json(Set<?> set) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (set != null && set.size() > 0) {
			Iterator arg2 = set.iterator();

			while (arg2.hasNext()) {
				Object obj = arg2.next();
				json.append(object2json(obj));
				json.append(",");
			}

			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}

		return json.toString();
	}

	private static String string2json(String s) {
		if (s == null) {
			return "";
		} else {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < s.length(); ++i) {
				char ch = s.charAt(i);
				switch (ch) {
					case '\b' :
						sb.append("\\b");
						continue;
					case '\t' :
						sb.append("\\t");
						continue;
					case '\n' :
						sb.append("\\n");
						continue;
					case '\f' :
						sb.append("\\f");
						continue;
					case '\r' :
						sb.append("\\r");
						continue;
					case '\"' :
						sb.append("\\\"");
						continue;
					case '/' :
						sb.append("\\/");
						continue;
					case '\\' :
						sb.append("\\\\");
						continue;
				}

				if (ch >= 0 && ch <= 31) {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");

					for (int k = 0; k < 4 - ss.length(); ++k) {
						sb.append('0');
					}

					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}

			return sb.toString();
		}
	}
}