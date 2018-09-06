
/** ==============  表格插件相关   ============== **/
/**
 * 分页表格初始化
 * @param tableId 显示数据的表格ID
 * @param url 表格的数据源
 * @param method 请求数据源使用的方法, 可选参数: 1.GET, 2.POST
 * @param toolbar 工具栏, 提高可读性
 */
var pageTableInit = function(tableId, url, method, toolbar) {
	$("#" + tableId).bootstrapTable({
		url: url,
		method: method,
		toolbar: toolbar,
		pageSize: 20,
		pagination: true,
		sidePagination: "server",
		queryParamsType: "",
		cache: false,
	});
}

/**
 * 普通表格初始化
 * @param tableId 显示数据的表格ID
 * @param toolbar 提高可读性
 * 
 */
var normalTableInit = function(tableId, toolbar) {
	$("#" + tableId).bootstrapTable({
		toolbar: toolbar,
		queryParamsType: "",
		valign: "middle",
		cache: false,
	});
}

/**
 * 分页条件查询表格初始化
 * @param tableId 显示数据的tableId
 * @param url 异步获取数据的数据源
 * @param method 使用GET或POST请求数据源,可选参数: 1.GET, 2.POST
 * @param toolbarId 工具栏ID, 作用不明,目前当作提高可读性参数
 * @param queryParamsFun 一个function, 返回向后台请求的参数,默认向后台传递 pageSize, pageNumber, searchText, sortname, sortOrder, 该方法有一个默认参数params对象, 封装了分页控件默认的参数.
 * sortname以及sortOrder目前无用, searchText目前用于存储一个Json字符串,转Json字符串的对象来源于条件查询的form表单,js中使用JSON.stringify(Object)来将对象转换为json字符串
 */
var searchPageTableInit = function(tableId, url, method, toolbarId, queryParamsFun) {
	$("#" + tableId).bootstrapTable({
		url: url,
		method: method,
		toolbar: toolbarId,
		pageSize: 20,
		pagination: true,
		sidePagination: "server",
		queryParamsType: "",
		queryParams: queryParamsFun,
		cache: false,
	});
}


/** ==================  时间插件相关  ================== **/
/**
 * 日期范围初始, YYYY-MM-DD ~ YYYY-MM-DD, 以' ~ ' 为分隔符, 例: 2018-02-01 ~ 2018-03-01
 * @param dateRangeId 初始时间插件所需要的元素ID
 */
var dateRangeInit = function (dateRangeId) {
	  $('#'+dateRangeId).daterangepicker({
	    	 "autoApply":true,
	    	 "opens":"center",
	    	 "singleDatePicker":false,
	         "linkedCalendars": false,
	         "autoUpdateInput": true,
	         "showDropdowns": true,
	         "locale": {
	             format: 'YYYY-MM-DD',
	             separator: ' ~ ',
	             applyLabel: "确定",
	             cancelLabel: "取消",
	             resetLabel: "重置",
	             daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
				 monthNames: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
				 firstDay:1
	         },
	     });
}

/**
 * 日期时间范围初始, YYYY-MM-DD HH:mm ~ YYYY-MM-DD HH:mm, 以' ~ '为分隔符, 例: 2018-02-01 01:59 ~ 2018-03-01 02:08
 * @param dateTimeRangeId 初始时间插件所需要的元素ID
 */
var dateTimeRangeInit = function (dateTimeRangeId) {
	$("#"+dateTimeRangeId).daterangepicker({
		"autoApply":true,
		"opens":"center",
		"singleDatePicker":false,
		"timePicker": true,
		"timePicker24Hour": true,
		"timePickerSeconds": true,
		"showDropdowns": true,
		"locale" : {
			"format": "YYYY-MM-DD HH:mm:ss", 
			"separator": ' ~ ',
			"applyLabel": '确定',
			"cancelLabel": '取消',
			"fromLabel": '开始',
			"toLabel": '结束',
			"daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
			"monthNames": ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
		},
	})
}
/**
 * 单日日期选择, 格式: YYYY-MM-DD 例: 2018-02-01
 * @param dateId 初始时间插件所需要的元素ID
 */
var dateInit = function (dateId) {
	$('#'+dateId).daterangepicker({
    	 "autoApply":true,
    	 "singleDatePicker":true,
         "linkedCalendars": true,
         "autoUpdateInput": true,
         "showDropdowns": true,
     	 "opens":"center",
         "locale": {
             format: 'YYYY-MM-DD',
             applyLabel: "确定",
             cancelLabel: "取消",
             resetLabel: "重置",
             daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
			 monthNames: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月' ],
			 firstDay:1
         },
     });
};

/**
 * 单日时间选择, 格式: YYYY-MM-DD HH:mm:ss, 例: 2018-02-01 15:33:12
 * @param dateId 初始时间插件所需要的元素ID
 */
var dateTimeInit = function(dateTimeId) {
	  $('#'+dateTimeId).daterangepicker({
	    	 "autoApply":true,
	    	 "opens":"center",
	    	 "singleDatePicker":true,
	         "timePicker": true,
	         "timePicker24Hour": true,
	         "timePickerSeconds": true,
	         "linkedCalendars": false,
	         "autoUpdateInput": true,
	         "showDropdowns": true,
	         "locale": {
	             format: 'YYYY-MM-DD HH:mm:ss',
	             applyLabel: "确定",
	             cancelLabel: "取消",
	             resetLabel: "重置",
	             daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
				 monthNames: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月' ],
				 firstDay:1
	         },
	     });
}


// ========================   写死url, 向后台获取数据		=======================
function checkUser(url, targetType, targetValue, serverId, isSingle, sucfun){
	var single = 0;
	if (isSingle) { single = 1; }
	if(targetValue != ""){
		var args = "serverId=" + serverId + "&single="+ single +"&targetType=" + targetType + "&targetValue=" + targetValue;
		$.ajax({
			url: url,
			data: args,
			type: 'POST',
			dataType: 'json',
			timeout: 10000,
			error: function(){ alert('连接服务器失败！！');},
			success: sucfun
		});
	}
}