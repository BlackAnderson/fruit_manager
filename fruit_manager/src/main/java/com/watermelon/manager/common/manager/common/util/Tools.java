package com.watermelon.manager.common.manager.common.util;

import org.apache.commons.logging.*;
import java.io.*;
import javax.servlet.http.*;
import java.math.*;
import java.text.*;
import java.util.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Tools {
	private static final Log log = LogFactory.getLog(Tools.class);
	public static final String DELIMITER_INNER_ITEM = "_";
	public static final String DELIMITER_BETWEEN_ITEMS = "|";
	public static final String DELIMITER_BETWEEN_ITEMS2 = "#";
	public static final String ARGS_DELIMITER = " ";
	public static final String ARGS_ITEMS_DELIMITER = "\\|";
	private static Random rd = new Random();

	public static byte[] object2ByteArray(Object obj) {
		if (obj == null) {
			return null;
		} else {
			try {
				ByteArrayOutputStream ex = new ByteArrayOutputStream();
				(new ObjectOutputStream(ex)).writeObject(obj);
				return ex.toByteArray();
			} catch (IOException arg1) {
				log.error("failed to serialize obj", arg1);
				return null;
			}
		}
	}

	public static Object byteArray2Object(byte[] buffer) {
		if (buffer != null && buffer.length != 0) {
			try {
				ByteArrayInputStream ex = new ByteArrayInputStream(buffer);
				ObjectInputStream ois = new ObjectInputStream(ex);
				return ois.readObject();
			} catch (Exception arg2) {
				log.error("failed to deserialize obj", arg2);
				return null;
			}
		} else {
			return null;
		}
	}

	public static Integer getServerId(Integer operatorId, Integer serverId) {
		String sid = "";
		if (operatorId == null) {
			return null;
		} else if (serverId != null && serverId.intValue() >= 1) {
			if (serverId.intValue() < 10) {
				sid = operatorId + "00" + serverId;
			} else if (serverId.intValue() < 100) {
				sid = operatorId + "0" + serverId;
			} else {
				sid = "" + operatorId + serverId;
			}

			return Integer.valueOf(Integer.parseInt(sid));
		} else {
			return null;
		}
	}

	public static String getZeroFillId(Integer id, Integer min_len) {
		String ids = "";
		if (id != null && id.intValue() > 0 && min_len != null && min_len.intValue() > 0) {
			int id_len = id.toString().length();
			if (id_len < min_len.intValue()) {
				for (int i = 0; i < min_len.intValue() - id_len; ++i) {
					ids = ids + "0";
				}
			}
		}

		ids = ids + id;
		return ids;
	}

	public static int getRandomInteger(int maxValue) {
		return maxValue <= 0 ? 0 : rd.nextInt(maxValue);
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		if (ip.indexOf(",") > 0) {
			String[] iplist = ip.split(",");
			String[] arg5 = iplist;
			int arg4 = iplist.length;

			for (int arg3 = 0; arg3 < arg4; ++arg3) {
				String ips = arg5[arg3];
				ips = ips.trim();
				if (!ips.equals("127.0.0.1") && checkIP(ips)) {
					ip = ips;
					break;
				}
			}
		}

		return ip;
	}

	public static boolean checkIP(String checkStr) {
		try {
			String e = checkStr.substring(0, checkStr.indexOf(46));
			if (Integer.parseInt(e) > 255) {
				return false;
			} else {
				checkStr = checkStr.substring(checkStr.indexOf(46) + 1);
				e = checkStr.substring(0, checkStr.indexOf(46));
				if (Integer.parseInt(e) > 255) {
					return false;
				} else {
					checkStr = checkStr.substring(checkStr.indexOf(46) + 1);
					e = checkStr.substring(0, checkStr.indexOf(46));
					if (Integer.parseInt(e) > 255) {
						return false;
					} else {
						e = checkStr.substring(checkStr.indexOf(46) + 1);
						return Integer.parseInt(e) <= 255;
					}
				}
			}
		} catch (Exception arg1) {
			return false;
		}
	}

	public static List<String[]> delimiterString2Array(String delimiterString) {
		if (delimiterString != null && delimiterString.trim().length() != 0) {
			String[] ss = delimiterString.trim().split("\\|");
			if (ss != null && ss.length > 0) {
				ArrayList list = new ArrayList();

				for (int i = 0; i < ss.length; ++i) {
					list.add(ss[i].split("_"));
				}

				return list;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static String array2DelimiterString(String[] subArray) {
		if (subArray != null && subArray.length != 0) {
			StringBuffer subContent = new StringBuffer();

			for (int tmp = 0; tmp < subArray.length; ++tmp) {
				subContent.append(subArray[tmp]).append("_");
			}

			String arg2 = subContent.toString().substring(0, subContent.lastIndexOf("_"));
			return arg2 + "|";
		} else {
			return "";
		}
	}

	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		} else {
			BigDecimal b = new BigDecimal(Double.toString(v));
			BigDecimal one = new BigDecimal("1");
			return b.divide(one, scale, 4).doubleValue();
		}
	}

	public static double roundDown(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		} else {
			BigDecimal b = new BigDecimal(Double.toString(v));
			BigDecimal one = new BigDecimal("1");
			return b.divide(one, scale, 1).doubleValue();
		}
	}

	public static double roundUp(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		} else {
			BigDecimal b = new BigDecimal(Double.toString(v));
			BigDecimal one = new BigDecimal("1");
			return b.divide(one, scale, 0).doubleValue();
		}
	}

	public static double divideAndRoundUp(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		} else {
			BigDecimal bd1 = new BigDecimal(v1);
			BigDecimal bd2 = new BigDecimal(v2);
			return bd1.divide(bd2, scale, 0).doubleValue();
		}
	}

	public static double divideAndRoundDown(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		} else {
			BigDecimal bd1 = new BigDecimal(v1);
			BigDecimal bd2 = new BigDecimal(v2);
			return bd1.divide(bd2, scale, 1).doubleValue();
		}
	}

	public static long getQuot(String time1, String time2) {
		long quot = 0L;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

		try {
			Date e = ft.parse(time1);
			Date date2 = ft.parse(time2);
			quot = e.getTime() - date2.getTime();
			quot = quot / 1000L / 60L / 60L / 24L;
		} catch (ParseException arg6) {
			arg6.printStackTrace();
		}

		return quot;
	}

	public static void main(String[] args) throws Exception {
	}
}