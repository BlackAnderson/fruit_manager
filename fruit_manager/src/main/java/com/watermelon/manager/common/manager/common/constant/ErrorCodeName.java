package com.watermelon.manager.common.manager.common.constant;

public abstract class ErrorCodeName {

	public static String getErrorCodeName(int code){
		switch(code){
		case 0:
			return "操作成功 ";

		case -1:
			return "操作失败";
		
		case -2:
			return "没操作权限";
		case -3:
			return "连接服务器失败";
			
		case -4:
			return "响应超时";
		
		case -5:
			return "服务器没有找到";
		
		case -6:
			return "返回空值";
			
		case -10002:
			return "礼包编号已存在";
			
		case -10003:
			return "错误的礼包设置时间";
		}
		return "操作失败";
	}
}
