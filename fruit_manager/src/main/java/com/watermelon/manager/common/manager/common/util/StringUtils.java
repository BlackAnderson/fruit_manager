package com.watermelon.manager.common.manager.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringUtils extends org.apache.commons.lang.StringUtils {
	private static Log log = LogFactory.getLog(StringUtils.class);
	private static final String PASSWORD_CRYPT_KEY = "__jDlog_";
	private static final String DES = "DES";
	private static final DecimalFormat percentFormat = new DecimalFormat("#0.00%");
	private static final String[] SERIALS = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "q", "w",
			"e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v",
			"b", "n", "m", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K",
			"L", "Z", "X", "C", "V", "B", "N", "M"};
	private static int SERIALS_LENGTH;

	static {
		SERIALS_LENGTH = SERIALS.length;
	}

	public static byte[] md5(byte[] src) throws Exception {
		MessageDigest alg = MessageDigest.getInstance("MD5");
		return alg.digest(src);
	}

	public static String md5(String src) throws Exception {
		return byte2hex(md5(src.getBytes()));
	}

	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(1, securekey, sr);
		return cipher.doFinal(src);
	}

	public static byte[] hex2byte(byte[] b) {
		if (b.length % 2 != 0) {
			throw new IllegalArgumentException("长度不是偶数");
		} else {
			byte[] b2 = new byte[b.length / 2];

			for (int n = 0; n < b.length; n += 2) {
				String item = new String(b, n, 2);
				b2[n / 2] = (byte) Integer.parseInt(item, 16);
			}

			return b2;
		}
	}

	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(2, securekey, sr);
		return cipher.doFinal(src);
	}

	public static final String decryptPassword(String data) {
		if (data != null) {
			try {
				return new String(decrypt(hex2byte(data.getBytes()), "__jDlog_".getBytes()));
			} catch (Exception arg1) {
				arg1.printStackTrace();
			}
		}

		return null;
	}

	public static final String encryptPassword(String password) {
		if (password != null) {
			try {
				return byte2hex(encrypt(password.getBytes(), "__jDlog_".getBytes()));
			} catch (Exception arg1) {
				arg1.printStackTrace();
			}
		}

		return null;
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";

		for (int n = 0; b != null && n < b.length; ++n) {
			stmp = Integer.toHexString(b[n] & 255);
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}

		return hs.toUpperCase();
	}

	public static String byte2webhex(byte[] b) {
		return byte2hex(b, "%");
	}

	public static String byte2hex(byte[] b, String elide) {
		String hs = "";
		String stmp = "";
		elide = elide == null ? "" : elide;

		for (int n = 0; b != null && n < b.length; ++n) {
			stmp = Integer.toHexString(b[n] & 255);
			if (stmp.length() == 1) {
				hs = hs + elide + "0" + stmp;
			} else {
				hs = hs + elide + stmp;
			}
		}

		return hs.toUpperCase();
	}

	public static List<String> delimiterString2Array(String delimiterString) {
		if (delimiterString != null && delimiterString.trim().length() != 0) {
			String[] ss = delimiterString.trim().split("|");
			if (ss != null && ss.length > 0) {
				ArrayList list = new ArrayList();

				for (int i = 0; i < ss.length; ++i) {
					list.add(ss[i]);
				}

				return list;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static String strReplaceJSTag(String oldStr) {
		if (oldStr != null && oldStr.trim().length() != 0) {
			StringBuffer buffer = new StringBuffer(oldStr.length() * 2);

			for (int i = 0; i < oldStr.length(); ++i) {
				char c = oldStr.charAt(i);
				if (c == 92) {
					buffer.append("\\\\");
				} else if (c == 34) {
					buffer.append("&quot;");
				} else if (c == 39) {
					buffer.append("&#039;");
				} else if (c == 60) {
					buffer.append("&lt;");
				} else if (c == 62) {
					buffer.append("&gt;");
				} else if (c == 13) {
					buffer.append("<br>");
				} else if (c == 10) {
					buffer.append("<br>");
				} else {
					buffer.append(c);
				}
			}

			return buffer.toString();
		} else {
			return null;
		}
	}

	public static String strReplaceAll(String oldStr) {
		if (oldStr != null && oldStr.trim().length() != 0) {
			String content1 = oldStr.replaceAll(" ", "&amp;");
			String content2 = content1.replaceAll("[\\n]", "<brn>");
			String content3 = content2.replaceAll("[\\r]", "<brr>");
			String content4 = content3.replaceAll("\\|", "<&1>");
			String content5 = content4.replaceAll("\\_", "<&2>");
			String newStr = content5.replaceAll("[\r\n]", "<br>");
			return newStr;
		} else {
			return null;
		}
	}

	public static String getMD5(byte[] source) {
		String s = null;
		char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

		try {
			MessageDigest e = MessageDigest.getInstance("MD5");
			e.update(source);
			byte[] tmp = e.digest();
			char[] str = new char[32];
			int k = 0;

			for (int i = 0; i < 16; ++i) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 15];
				str[k++] = hexDigits[byte0 & 15];
			}

			s = new String(str);
		} catch (Exception arg8) {
			arg8.printStackTrace();
		}

		return s;
	}

	public static String getMD5(String source) {
		try {
			byte[] e = source.getBytes("UTF-8");
			return getMD5(e);
		} catch (UnsupportedEncodingException arg1) {
			log.error("StringUtils.getMD5(String source)", arg1);
			return "";
		}
	}

	public static String printReqParam(Object obj, int index) {
		if (obj == null) {
			return "";
		} else {
			if (obj instanceof String[]) {
				String[] params = (String[]) obj;
				if (params != null && params.length > 0 && index < params.length) {
					return params[index];
				}
			}

			return "";
		}
	}

	public static String printReqParam(Object obj) {
		return printReqParam(obj, 0);
	}

	public static String doubleToPercent(Double value) {
		return percentFormat.format(value);
	}

	public static List<Integer> stringToList(String source) {
		if (source != null && !source.equals("")) {
			String[] temp = source.trim().split(";");
			ArrayList list = new ArrayList();
			String[] arg5 = temp;
			int arg4 = temp.length;

			for (int arg3 = 0; arg3 < arg4; ++arg3) {
				String str = arg5[arg3];
				list.add(Integer.valueOf(Integer.parseInt(str.trim())));
			}

			return list;
		} else {
			return null;
		}
	}

	public static List<String> stringToStringList(String source) {
		if (source != null && !source.equals("")) {
			String[] temp = source.split(";");
			ArrayList result = new ArrayList();
			String[] arg5 = temp;
			int arg4 = temp.length;

			for (int arg3 = 0; arg3 < arg4; ++arg3) {
				String str = arg5[arg3];
				result.add(str);
			}

			return result;
		} else {
			return null;
		}
	}

	public static String cutThefrontNumWord(String oldWord, int num) {
		int oldWordLength = oldWord.length();
		if (oldWordLength < num + 1) {
			return oldWord;
		} else {
			String newWord = oldWord.substring(0, num);
			newWord = newWord + "...";
			return newWord;
		}
	}

	public static List<String> createSerialNO(int n, int length) {
		if (n <= 0) {
			return null;
		} else {
			ArrayList result = new ArrayList(n);
			Random random = new Random(System.currentTimeMillis());

			for (int i = 0; i < n; ++i) {
				StringBuffer sb = new StringBuffer();

				for (int j = 0; j < length; ++j) {
					int index = random.nextInt(SERIALS_LENGTH);
					sb.append(SERIALS[index]);
				}

				result.add(sb.toString());
			}

			return result;
		}
	}

	public static String createSerialNO(int length) {
		Random random = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();

		for (int j = 0; j < length; ++j) {
			int index = random.nextInt(SERIALS_LENGTH);
			sb.append(SERIALS[index]);
		}

		return sb.toString();
	}

	public static String replaceLineFeed(String string) {
		StringBuffer result = new StringBuffer();
		if (string != null && string != "") {
			String[] tmp = string.split("\r\n");
			String[] arg5 = tmp;
			int arg4 = tmp.length;

			for (int arg3 = 0; arg3 < arg4; ++arg3) {
				String string2 = arg5[arg3];
				if (string != null && string2.length() > 0 && !string.equals("")) {
					result.append(string2);
					result.append(";");
				}
			}

			return result.toString().substring(0, result.toString().length() - 1);
		} else {
			return null;
		}
	}

	public static String replaceSemicolon(String string) {
		StringBuffer result = new StringBuffer();
		if (string != null && string != "") {
			String[] tmp = string.split(";");
			String[] arg5 = tmp;
			int arg4 = tmp.length;

			for (int arg3 = 0; arg3 < arg4; ++arg3) {
				String string2 = arg5[arg3];
				if (string != null && string2.length() > 0 && !string.equals("")) {
					result.append(string2);
					result.append("\r\n");
				}
			}

			return result.toString().substring(0, result.toString().length() - 2);
		} else {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(strReplaceJSTag("123\\\\\"\"\'\r\n\r0000000000"));
	}
}