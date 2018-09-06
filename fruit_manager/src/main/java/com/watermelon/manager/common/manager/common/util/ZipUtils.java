package com.watermelon.manager.common.manager.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
	public static final String ZIP_FILENAME = "C:\\XJPDA.zip";
	public static final String ZIP_DIR = "C:\\XJPDA\\";
	public static final String UN_ZIP_DIR = "C:\\";
	public static final int BUFFER = 1024;

	public static void main(String[] args) {
		try {
			unZipFile("D:/upload/dist/", "D:/upload/dist.zip");
		} catch (Exception arg1) {
			arg1.printStackTrace();
		}

	}

	public static void zipFile(String baseDir, String fileName) throws Exception {
		List fileList = getSubFiles(new File(baseDir));
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileName));
		ZipEntry ze = null;
		byte[] buf = new byte[1024];
		boolean readLen = false;

		for (int i = 0; i < fileList.size(); ++i) {
			File f = (File) fileList.get(i);
			ze = new ZipEntry(getAbsFileName(baseDir, f));
			ze.setSize(f.length());
			ze.setTime(f.lastModified());
			zos.putNextEntry(ze);
			BufferedInputStream is = new BufferedInputStream(new FileInputStream(f));

			int arg9;
			while ((arg9 = is.read(buf, 0, 1024)) != -1) {
				zos.write(buf, 0, arg9);
			}

			is.close();
		}

		zos.close();
	}

	public static String getAbsFileName(String baseDir, File realFileName) {
		File real = realFileName;
		File base = new File(baseDir);
		String ret = realFileName.getName();

		while (true) {
			real = real.getParentFile();
			if (real == null || real.equals(base)) {
				return ret;
			}

			ret = real.getName() + "/" + ret;
		}
	}

	public static List<File> getSubFiles(File baseDir) {
		ArrayList ret = new ArrayList();
		File[] tmp = baseDir.listFiles();

		for (int i = 0; i < tmp.length; ++i) {
			if (tmp[i].isFile()) {
				ret.add(tmp[i]);
			}

			if (tmp[i].isDirectory()) {
				ret.addAll(getSubFiles(tmp[i]));
			}
		}

		return ret;
	}

	public static void unZipFile(String baseDir, String fileName) throws Exception {
		ZipFile zfile = new ZipFile(fileName);
		Enumeration zList = zfile.entries();
		ZipEntry ze = null;
		byte[] buf = new byte[1024];

		while (true) {
			while (zList.hasMoreElements()) {
				ze = (ZipEntry) zList.nextElement();
				if (ze.isDirectory()) {
					File os1 = new File(baseDir + ze.getName());
					os1.mkdir();
				} else {
					BufferedOutputStream os = new BufferedOutputStream(
							new FileOutputStream(getRealFileName(baseDir, ze.getName())));
					BufferedInputStream is = new BufferedInputStream(zfile.getInputStream(ze));
					boolean readLen = false;

					int readLen1;
					while ((readLen1 = is.read(buf, 0, 1024)) != -1) {
						os.write(buf, 0, readLen1);
					}

					is.close();
					os.close();
				}
			}

			zfile.close();
			return;
		}
	}

	public static File getRealFileName(String baseDir, String absFileName) {
		String[] dirs = absFileName.split("/");
		File ret = new File(baseDir);
		if (dirs.length <= 1) {
			return ret;
		} else {
			for (int i = 0; i < dirs.length - 1; ++i) {
				ret = new File(ret, dirs[i]);
			}

			if (!ret.exists()) {
				ret.mkdirs();
			}

			ret = new File(ret, dirs[dirs.length - 1]);
			return ret;
		}
	}
}