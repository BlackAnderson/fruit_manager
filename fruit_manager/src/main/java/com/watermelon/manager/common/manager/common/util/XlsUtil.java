package com.watermelon.manager.common.manager.common.util;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jxl.Workbook;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class XlsUtil {
	private static Log log = LogFactory.getLog(XlsUtil.class);

	public static boolean writeXls(String[] title, List<Object[]> datas, OutputStream os) {
		try {
			WritableWorkbook e = Workbook.createWorkbook(os);
			WritableSheet sheet = e.createSheet("sheet1", 0);

			Label label;
			for (int data = 0; data < title.length; ++data) {
				label = new Label(data, 0, title[data]);
				sheet.addCell(label);
			}

			for (int row = 1; row <= datas.size(); ++row) {
				Object[] arg11 = (Object[]) datas.get(row - 1);

				for (int col = 0; col < title.length; ++col) {
					if (arg11[col] instanceof String) {
						label = new Label(col, row, (String) arg11[col]);
						sheet.addCell(label);
					} else {
						Number numberLabel;
						if (arg11[col] instanceof Integer) {
							numberLabel = new Number(col, row, (double) ((Integer) arg11[col]).intValue());
							sheet.addCell(numberLabel);
						} else if (arg11[col] instanceof Long) {
							numberLabel = new Number(col, row, (double) ((Long) arg11[col]).longValue());
							sheet.addCell(numberLabel);
						} else if (arg11[col] instanceof Float) {
							numberLabel = new Number(col, row, (double) ((Float) arg11[col]).floatValue());
							sheet.addCell(numberLabel);
						} else if (arg11[col] instanceof Double) {
							numberLabel = new Number(col, row, ((Double) arg11[col]).doubleValue());
							sheet.addCell(numberLabel);
						} else if (arg11[col] instanceof Date) {
							DateTime dateTimeLabel = new DateTime(col, row, (Date) arg11[col]);
							sheet.addCell(dateTimeLabel);
						}
					}
				}
			}

			e.write();
			e.close();
			os.close();
			return true;
		} catch (Exception arg10) {
			log.error("XlsUtil.writeXls(title, datas, os)", arg10);
			return false;
		}
	}

	public static boolean writeXls(List<String[]> datas, OutputStream os) {
		try {
			WritableWorkbook e = Workbook.createWorkbook(os);
			WritableSheet sheet = e.createSheet("sheet1", 0);

			for (int row = 0; row < datas.size(); ++row) {
				String[] data = (String[]) datas.get(row);

				for (int col = 0; col < data.length; ++col) {
					Label label = new Label(col, row, data[col]);
					sheet.addCell(label);
				}
			}

			e.write();
			e.close();
			return true;
		} catch (Exception arg7) {
			log.error("XlsUtil.writeXls(title, datas, os)", arg7);
			return false;
		}
	}

	public static boolean writeXls(String datas, OutputStream os) {
		String[] temp = datas.split(";");
		if (temp.length <= 0) {
			return false;
		} else {
			ArrayList list = new ArrayList(temp.length);
			String[] arg6 = temp;
			int arg5 = temp.length;

			for (int arg4 = 0; arg4 < arg5; ++arg4) {
				String str = arg6[arg4];
				list.add(str.split(":"));
			}

			return writeXls((List) list, os);
		}
	}
}