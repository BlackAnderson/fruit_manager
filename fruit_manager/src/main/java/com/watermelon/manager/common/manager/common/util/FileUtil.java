package com.watermelon.manager.common.manager.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class FileUtil {
	public static String XML_PATH = "E:/work/java/my9yu_manager/resources/basic_XML/";
	public static String BUG_FILE_PATH = "";
	public static String ANNOUNCEMENT_FILE_PATH = "";

	public static void main(String[] args) throws Exception {
		String path = "E:/work/java/motaht/WebRoot/statistics";
		File[] dirs = (new File(path)).listFiles();
		Document xlsConfig = DocumentHelper.createDocument();
		Element configRoot = xlsConfig.addElement("config");
		File[] arg7 = dirs;
		int arg6 = dirs.length;

		for (int arg5 = 0; arg5 < arg6; ++arg5) {
			File dir = arg7[arg5];
			File[] d = dir.listFiles();
			System.out.println(dir.getName());
			if (dir.getName().equals("fight") || dir.getName().equals("finance") || dir.getName().equals("PVP")) {
				File[] arg12 = d;
				int arg11 = d.length;

				for (int arg10 = 0; arg10 < arg11; ++arg10) {
					File file = arg12[arg10];
					if (file.getName().contains(".jsp")) {
						System.out.println(file.getAbsolutePath());
						String className = file.getName();
						Element e = createElement(file, className);
						configRoot.add(e);
					}
				}
			}
		}

		XmlUtil.writeDocument(xlsConfig, new FileOutputStream("D:/xlsConfig.xml"));
	}

	private static Element createElement(File file, String className) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		StringBuffer sb = new StringBuffer();
		boolean isAdd = false;

		String temp;
		while ((temp = reader.readLine()) != null) {
			if (isAdd && temp.contains("</logic:notMatch")) {
				isAdd = false;
			}

			if (isAdd) {
				if (temp.contains("<table")) {
					temp = "<table>";
				} else if (temp.contains("</table")) {
					temp = "</table>";
				} else if (temp.contains("<tr")) {
					temp = "<tr>";
				} else if (temp.contains("</tr")) {
					temp = "</tr>";
				} else if (temp.contains("<logic:iterate")) {
					temp = "<logic>";
				} else if (temp.contains("</logic:iterate")) {
					temp = "</logic>";
				} else if (temp.contains("<bean:write")) {
					temp = temp.replace("<bean:write", "<bean");
				} else if (temp.contains("</bean:write")) {
					temp = temp.replace("</bean:write", "</bean");
				}

				sb.append(temp.trim());
			}

			if (!isAdd && temp.contains("<logic:notMatch")) {
				isAdd = true;
			}
		}

		reader.close();
		Document document = DocumentHelper.parseText(sb.toString());
		Element root = document.getRootElement();
		StringBuffer title = null;
		Iterator iter = root.element("tr").elementIterator();

		while (iter.hasNext()) {
			if (title == null) {
				title = new StringBuffer();
				title.append(((Element) iter.next()).getText());
			} else {
				title.append(",").append(((Element) iter.next()).getText());
			}
		}

		StringBuffer property = null;
		iter = root.element("logic").element("tr").elementIterator();

		while (iter.hasNext()) {
			if (property == null) {
				property = new StringBuffer();
				property.append(((Element) iter.next()).element("bean").attributeValue("property").trim());
			} else {
				property.append(",").append(((Element) iter.next()).element("bean").attributeValue("property").trim());
			}
		}

		Element data = DocumentHelper.createElement("data");
		data.addElement("className").setText(className);
		data.addElement("title").setText(title.toString());
		data.addElement("property").setText(property.toString());
		return data;
	}

	public static void newFolder(String folderPath) {
		try {
			String e = folderPath.toString();
			File myFilePath = new File(e);
			if (!myFilePath.exists()) {
				myFilePath.mkdir();
			}
		} catch (Exception arg2) {
			System.out.println("新建目录操作出错");
			arg2.printStackTrace();
		}

	}

	public static void newFolder(String path, String folderName) {
		try {
			File e = new File(path);
			if (e.exists() && e.isDirectory()) {
				String filePath = path + "/" + folderName;
				File newFilePath = new File(filePath);
				if (!newFilePath.exists()) {
					newFilePath.mkdir();
				}
			}
		} catch (Exception arg4) {
			System.out.println("新建目录操作出错");
			arg4.printStackTrace();
		}

	}

	public static void newFile(String filePathAndName, String fileContent) {
		try {
			String e = filePathAndName.toString();
			File myFilePath = new File(e);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}

			FileWriter resultFile = new FileWriter(myFilePath);
			PrintWriter myFile = new PrintWriter(resultFile);
			myFile.println(fileContent);
			myFile.close();
			resultFile.close();
		} catch (Exception arg6) {
			System.out.println("新建目录操作出错");
			arg6.printStackTrace();
		}

	}

	public static void delFile(String filePathAndName) {
		try {
			String e = filePathAndName.toString();
			File myDelFile = new File(e);
			myDelFile.delete();
		} catch (Exception arg2) {
			System.out.println("删除文件操作出错");
			arg2.printStackTrace();
		}

	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath);
			String e = folderPath.toString();
			File myFilePath = new File(e);
			myFilePath.delete();
		} catch (Exception arg2) {
			System.out.println("删除文件夹操作出错");
			arg2.printStackTrace();
		}

	}

	private static void delAllFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			if (file.isDirectory()) {
				String[] tempList = file.list();
				File temp = null;

				for (int i = 0; i < tempList.length; ++i) {
					if (path.endsWith(File.separator)) {
						temp = new File(path + tempList[i]);
					} else {
						temp = new File(path + File.separator + tempList[i]);
					}

					if (temp.isFile()) {
						temp.delete();
					}

					if (temp.isDirectory()) {
						delAllFile(path + "/" + tempList[i]);
						delFolder(path + "/" + tempList[i]);
					}
				}

			}
		}
	}

	public static void copyFile(String oldPath, String newPath) {
		try {
			int e = 0;
			boolean byteread = false;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				FileInputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];

				int byteread1;
				while ((byteread1 = inStream.read(buffer)) != -1) {
					e += byteread1;
					fs.write(buffer, 0, byteread1);
				}

				fs.close();
				inStream.close();
			}
		} catch (Exception arg7) {
			System.out.println("复制单个文件操作出错");
			arg7.printStackTrace();
		}

	}

	public static void copyFolder(String oldPath, String newPath) {
		try {
			(new File(newPath)).mkdirs();
			File e = new File(oldPath);
			String[] file = e.list();
			File temp = null;

			for (int i = 0; i < file.length; ++i) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + temp.getName().toString());
					byte[] b = new byte[5120];

					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}

					output.flush();
					output.close();
					input.close();
				}

				if (temp.isDirectory()) {
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception arg9) {
			System.out.println("复制整个文件夹内容操作出错");
			arg9.printStackTrace();
		}

	}

	public static void moveFile(String oldPath, String newPath) {
		copyFile(oldPath, newPath);
		delFile(oldPath);
	}

	public static void moveFolder(String oldPath, String newPath) {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);
	}
}