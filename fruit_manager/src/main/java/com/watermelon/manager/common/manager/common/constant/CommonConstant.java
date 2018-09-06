package com.watermelon.manager.common.manager.common.constant;


/**
 * 公共返回常量
 * <p>
 * *****************************
 * <p>预留 10000个作为公共常量值
 * <p>
 * *****************************
 * 
 */
public interface CommonConstant {
	
	/** 操作成功 */
	int SUCCESS = 0;

	/** 操作失败 */
	int FAILURE = -1;
	
	/** 没操作权限 */
	int NO_RIGHT = -2;
	
	/**连接服务器失败**/
	int CONNET_ERROR = -3;
	
	/**响应超时*/
	int RESPONSE_OUT_TIME = -4;
	
	/***服务器没有找到**/
	int SERVER_NOT_FIND = -5;
	
	/**返回空值**/
	int RESPONSE_NONE = -6;
}
