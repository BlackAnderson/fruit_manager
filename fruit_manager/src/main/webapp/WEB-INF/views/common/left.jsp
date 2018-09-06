<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<meta http-equiv="Cache-Control" content="public,max-age=7200">
<meta http-equiv="Expires" content="Sunday 26 October 2022 01:00 GMT" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/devexpress-web-14.1/js/jquery-1.11.1.js"></script>
<script>

function initpage() {
	setOperatorId(document.getElementById('operator').value);
	savSelectIndex();
	tagEventInit();	
}
</script>
<style>
	#operator {background-color: rgba(255,255,255,0.3);border: none;color: rgba(0,0,0,0.7);margin-left:20px; font-size: 14px;width: 110px;}
    .buane:before{content:"";height:45px;width:6px;background-color: #FFD700;display:block;float:right;}
	.towtable > li{background: #007696;}
	
</style>
<div class="settings-pane">
	<a data-toggle="settings-pane" data-animate="true">&times;</a>
	<div class="settings-pane-inner">
		<div class="row">
			<div class="col-md-6">
				<div class="user-info">
					<div class="user-details">
						<h3><a>${sessionScope.user.name }</a></h3>
						<p class="user-title">${sessionScope.user.trueName }</p>
						<div class="user-links">
							<a href="updatePassword.do" onclick="return showFrame(this.href)" class="btn btn-primary">修改密码</a>
							<a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">退出</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="page-container" >
	<div class="sidebar-menu toggle-others fixed">
		<div class="sidebar-menu-inner">
			<div style="height: 70px;width: 100%;">
				<img class="logina" src="${pageContext.request.contextPath}/images/logo-MT.png">
			</div>
			<header class="logo-env">
				<div class="logo">
					<select class="form-control input-sm" id="operator" onchange="changServerSiteByOperatorId(this.value);">
					<c:forEach items="${operatorList }" var="operator">
						<option value="${operator.operatorId }">${operator.name }</option>
					</c:forEach>
					</select>
					<input id="tmpOperatorId" type="hidden"/>
				</div>
				<div class="mobile-menu-toggle visible-xs">
					<a href="#" data-toggle="mobile-menu"><i class="glyphicon glyphicon-align-justify"></i></a>
				</div>
				<div class="settings-icon">
					<a href="#" data-toggle="settings-pane" data-animate="true"><i class="linecons-cog"></i></a>
				</div>
			</header>
			<ul id="main-menu" class="main-menu">
				<sec:authorize access="hasRole('OPERATION_MANAGER')">
				<li class="active opened active">
					<a><i class="meteocons-cloud"></i><span class="title"><h4><spring:message code="GAME.MENU.TITLE.OPMANAGER" text="运营管理" /></h4></span></a>
					<ul>
						<sec:authorize access="hasRole('CHARGE')">
						<li>
							<a><span class="title"><spring:message code="GAME.MENU.TITLE.CHARGECONSUME" text="充值与消费" /></span></a>
							<ul>
								<sec:authorize access="hasRole('CH_CHLOG')">
									<li class="tag"><a href="../game/finance/listChargeRecord" onclick="return showFrame(this.href);"><spring:message text="玩家充值记录"/></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('CH_CHUSER')">
									<li class="tag"><a href="../game/finance/listChargeUser" target="mainFrame"><spring:message code="GAME.PAGE.TITLE.MANA_CH_CHUSER" text="充值用户列表" /></a></li>
								</sec:authorize>
								<%-- <sec:authorize access="hasRole('CH_TOP1000')">
									<li class="tag"><a href="../game/finance/operatorTopInfo" target="mainFrame"><spring:message text="前1000玩家信息" /></a></li>
								</sec:authorize> --%>
								<sec:authorize access="hasRole('CH_DAILYRAN')">
									<li class="tag"><a href="../game/finance/listChargeRank" onclick="return showFrame(this.href);"><spring:message text="每日充值排行榜"/></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('CH_YBLOG')">
									<li class="tag"><a href="../game/finance/listGoldLog" onclick="return showFrame(this.href);"><spring:message text="元宝日志"/></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('CH_RECGRADE')">
									<li class="tag"><a href="../game/finance/chargeLevelCensus" onclick="return showFrame(this.href);"><spring:message text="充值档次统计"/></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('CH_DOWNLOADREC')">
									<li class="tag"><a href="../game/finance/downChargeLog" onclick="return showFrame(this.href);"><spring:message text="下载充值记录"/></a></li>
								</sec:authorize>
								<%-- <sec:authorize access="hasRole('CH_CONSUMELOG')">
									<li class="tag"><a href="../game/finance/listChargeRecord" onclick="return showFrame(this.href);"><spring:message text="玩家消费记录"/></a></li>
								</sec:authorize> --%>
								<sec:authorize access="hasRole('CH_CHSUMMARY')">
									<li class="tag"><a href="../game/finance/listChargeSummary" onclick="return showFrame(this.href);"><spring:message text="充值汇总"/></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('CH_CONSOU')">
									<li class="tag"><a href="../game/consumption/listConsumeSource" onclick="return showFrame(this.href);"><spring:message text="消费类型排行"/></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('CH_DAILYREC')">
									<li class="tag"><a href="../game/finance/listChargeDaily" onclick="return showFrame(this.href);"><spring:message text="每天充值统计"/></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('CH_YBIE')">
									<li class="tag"><a href="../game/finance/listMonthOperatorGoldInfo" onclick="return showFrame(this.href);"><spring:message text="月汇总元宝收支"/></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('CH_YNEWPLAYERS')">
									<li class="tag"><a href="../game/finance/listMonthNewChargeUser" onclick="return showFrame(this.href);"><spring:message text="月新增付费玩家数"/></a></li>
								</sec:authorize>
								<%-- <sec:authorize access="hasRole('CH_FREQUENCY')">
									<li class="tag"><a href="../game/finance/listFrequency" onclick="return showFrame(this.href);"><spring:message text="跨服单排增加次数"/></a></li>
								</sec:authorize> --%>
								<sec:authorize access="hasRole('CH_FPAYGRADE')">
									<li class="tag"><a href="../game/finance/firstPayLevel" target="mainFrame"><spring:message text="首次付费等级" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('CH_FPAYLINE')">
									<li class="tag"><a href="../game/finance/firstPayLimit" target="mainFrame"><spring:message text="首次付费额度" /></a></li>
								</sec:authorize>
							</ul>				
						</li>
						</sec:authorize>
						<sec:authorize access="hasRole('REGISTER_ONLINE')">
						<li>
							<a><span class="title"><spring:message code="GAME.MENU.TITLE.ONLINEREGISTER" text="在线与注册" /></span></a>
							<ul>
								<sec:authorize access="hasRole('RO_ONLINEDATA')">
									<li class="tag"><a href="../game/listregister/listOnlineByDay" onclick="return showFrame(this.href);"><spring:message text="每日在线数据" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('RO_DAILYMAXOL')">
									<li class="tag"><a href="../game/listregister/listOnlineTop" onclick="return showFrame(this.href);"><spring:message text="每日在线峰值" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('RO_RETAINED')">
									<li class="tag"><a href="../game/listregister/listPlayerRetention" onclick="return showFrame(this.href);"><spring:message text="玩家留存" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('OL_DROPUSER')">
								<li class="tag"><a href="../game/listregister/listRegistUserLoginByServer" target="mainFrame"><spring:message  text="用户留存(服)" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('OL_RDROPUSER')">
								<li class="tag"><a href="../game/listregister/listRegistUserLoginByTime" target="mainFrame"><spring:message  text="用户留存(日期)" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('OL_YDROPUSER')">
								<li class="tag"><a href="../game/listregister/listUserRetentionRate" target="mainFrame"><spring:message  text="用户留存率(日期)" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('OL_PAYDROPUSER')">
								<li class="tag"><a href="../game/listregister/listDropPayuserByServer" target="mainFrame"><spring:message  text="付费用户留存(服)" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('OL_RPAYDROPUSER')">
								<li class="tag"><a href="../game/listregister/listDropPayuserByTime" target="mainFrame"><spring:message  text="付费用户留存(日期)" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('OL_CPAYDROPUSER')">
								<li class="tag"><a href="../game/listregister/listContinuedPayuser" target="mainFrame"><spring:message  text="付费用户持续度" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('OL_USERLOGIN')">
								<li class="tag"><a href="../game/listregister/listLoginUser" target="mainFrame"><spring:message  text="用户登陆统计" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('OL_REGHOUR')">
								<li class="tag"><a href="../game/listRegistered/listRegistUserHour" target="mainFrame"><spring:message  text="分时段注册" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('OL_REGDAY')">
								<li class="tag"><a href="../game/listRegistered/listRegistUserDaily" target="mainFrame"><spring:message  text="按天注册" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('OL_USERDETAIL')">
								<li class="tag"><a href="../game/listRegistered/listSearchPlayer" target="mainFrame"><spring:message  text="注册用户明细" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('OL_NOCREATE')">
								<li class="tag"><a href="../game/listRegistered/listNoCreateUser" target="mainFrame"><spring:message text="未创角明细" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('OL_DAYREGISTER')">
								<li class="tag"><a href="../game/listRegistered/listSearchPlayerByTime" target="mainFrame"><spring:message  text="按天查询注册明细" /></a></li>
								</sec:authorize>
								
							</ul>
						</li>
						</sec:authorize>
						<sec:authorize access="hasRole('GIFT')">
						<li>
							<a><span class="title"><spring:message code="GAME.MENU.TITLE.GIFT" text="礼包" /></span></a>
							<ul>
								<sec:authorize access="hasRole('GIFT_CHECKGIFT')">
									<li class="tag"><a href="../game/gift/addMaintenanceGift" onclick="return showFrame(this.href);"><spring:message text="添加维护礼包" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('GIFT_ADDNOMGIFT')">
									<li class="tag"><a href="../game/gift/addNormalGift" onclick="return showFrame(this.href);"><spring:message text="添加普通礼包" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('GIFT_ADDUSERG')">
									<li class="tag"><a href="../game/gift/addMt3PlayerGift" onclick="return showFrame(this.href);"><spring:message text="申请奖励与补偿" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('GIFT_PGIFTBAG')">
									<li class="tag"><a href="../game/gift/listGift" onclick="return showFrame(this.href);"><spring:message text="礼包列表" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('GIFT_GIFTBAG')">
									<li class="tag"><a href="../game/gift/listSendApplyGift" onclick="return showFrame(this.href);"><spring:message text="可发放的礼包" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('GIFT_CHECKGIFT')">
									<li class="tag"><a href="../game/gift/listCheckedApplyGift" onclick="return showFrame(this.href);"><spring:message text="需要审核的礼包" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('GIFT_ADDSEGIFT')">
									<li class="tag"><a href="../game/gift/addSerialGift" onclick="return showFrame(this.href);"><spring:message text="添加序列号礼包" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('GIFT_MANSEGIFT')">
									<li class="tag"><a href="../game/gift/listSerialGift" onclick="return showFrame(this.href);"><spring:message text="序列号礼包管理" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('GIFT_USERRMB')">
									<li class="tag"><a href="../game/gift/addUserRmb" target="mainFrame"><spring:message code="GAME.PAGE.TITLE.MANA_GIFT_ADD_RMB" text="添加内币" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('GIFT_APPLYMONEYLOG')">
									<li class="tag"><a href="../game/gift/listApplyMoneyLog" target="mainFrame"><spring:message code="GAME.PAGE.TITLE.MANA_GIFT_ADDJINGQIANLOG" text="金钱添加记录" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('GIFT_DIAMOND')">
									<li class="tag"><a href="../game/gift/addDiamond" onclick="return showFrame(this.href);"><spring:message text="添加钻石" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('GIFT_USERMONEY')">
									<li class="tag"><a href="../game/gift/addUserMoney" onclick="return showFrame(this.href);"><spring:message text="添加金币" /></a></li>
								</sec:authorize>
							</ul>
						</li>
						</sec:authorize>
						<sec:authorize access="hasRole('STATISTICS')">
							<li>
								<a><span class="title"><spring:message code="GAME.MENU.TITLE.STATISTIC" text="数据统计" /></span></a>
								<ul>
									<sec:authorize access="hasRole('DATA_QFSJHZ')">
										<li class="tag"><a href="../game/mt3statistics/listDataSummary" onclick="return showFrame(this.href);"><spring:message text="全服数据汇总" /></a></li>
									</sec:authorize>
									<sec:authorize access="hasRole('DATA_USERGROW')">
										<li class="tag"><a href="../game/mt3statistics/listPlayerLevel" onclick="return showFrame(this.href);"><spring:message text="玩家等级分布" /></a></li>
									</sec:authorize>
									<sec:authorize access="hasRole('DATA_VIPGROW')">
										<li class="tag"><a href="../game/mt3statistics/listVIPLevel" onclick="return showFrame(this.href);"><spring:message text="VIP等级分布" /></a></li>
									</sec:authorize>
									<sec:authorize access="hasRole('DATA_GFFX')">
										<li class="tag"><a href="../game/mt3statistics/listRollServerAnalysis" onclick="return showFrame(this.href);"><spring:message text="滚服分析" /></a></li>
									</sec:authorize>
									<sec:authorize access="hasRole('DATA_LTVFX')">
										<li class="tag"><a href="../game/mt3statistics/listLTVAnalysis" onclick="return showFrame(this.href);"><spring:message text="LTV分析" /></a></li>
									</sec:authorize>
										<%-- <li class="tag"><a href="../game/mt3statistics/listRechargeService" onclick="return showFrame(this.href);"><spring:message text="充值服管理" /></a></li> --%>
									<sec:authorize access="hasRole('DATA_AREA_NEW_USER')">
										<li class="tag"><a href="../game/mt3statistics/listAreaNewUser" onclick="return showFrame(this.href);"><spring:message text="区服新增用户数据" /></a></li>
									</sec:authorize>
									
									<sec:authorize access="hasRole('DATA_ONE_DAY')">
										<li class="tag"><a href="../game/mt3statistics/listOneDayData" onclick="return showFrame(this.href);"><spring:message text="单日数据" /></a></li>
									</sec:authorize>
									
									<sec:authorize access="hasRole('DATA_DAILY')">
										<li class="tag"><a href="../game/mt3statistics/listDaily" onclick="return showFrame(this.href);"><spring:message text="运营日报" /></a></li>
									</sec:authorize>
								</ul>
							</li>
						</sec:authorize>
						<sec:authorize access="hasRole('SYSTEM')">
						<li>
							<a><span class="title"><spring:message text="服务器管理" /></span></a>
							<ul>
							<%-- <sec:authorize access="hasRole('SERVER_LIST')">
								<li class="tag"><a href="../game/gamectrl/listServerBaseInfo" onclick="return showFrame(this.href);"><spring:message text="服务器列表" /></a></li>
							</sec:authorize> --%>
							<sec:authorize access="hasRole('SERVER_OPEN')">
								<li class="tag"><a href="../game/gamectrl/listOpenServer" onclick="return showFrame(this.href);"><spring:message text="开服管理" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('SERVER_USERGROW')">
								<li class="tag"><a href="../game/gamectrl/applyMergeServer" onclick="return showFrame(this.href);"><spring:message text="申请合服" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('SERVER_LISTAPPROVAL')">
								<li class="tag"><a href="../game/gamectrl/listApplyMergeServer" onclick="return showFrame(this.href);"><spring:message text="合服审批列表" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('SERVER_APPROVAL')">
								<li class="tag"><a href="../game/gamectrl/checkApplyMergeServer" onclick="return showFrame(this.href);"><spring:message text="审批合服" /></a></li>
							</sec:authorize>
							</ul>
						</li>
						</sec:authorize>
						<sec:authorize access="hasRole('CHART')">
						<li>
							<a><span class="title"><spring:message code="GAME.MENU.TITLE.CHARTS" text="图表" /></span></a>
							<ul>
								<sec:authorize access="hasRole('CHART_RECHARGE')">
									<li class="tag"><a href="../game/charts/chartChargeStatistic" onclick="return showFrame(this.href);"><span><spring:message text="充值金额" /></span></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('CHART_FCHARGE')">
									<li class="tag"><a href="../game/charts/chargeStatistic" onclick="return showFrame(this.href);"><span><spring:message text="首充" /></span></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('CHART_PROPOUTPUT')">
									<li class="tag"><a href="../game/charts/chargeStatistic" onclick="return showFrame(this.href);"><span><spring:message text="道具产出" /></span></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('CHART_PROPCONSUMPT')">
									<li class="tag"><a href="../game/charts/chargeStatistic" onclick="return showFrame(this.href);"><span><spring:message text="道具消耗" /></span></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('CHART_STORESALE')">
									<li class="tag"><a href="../game/charts/chargeStatistic" onclick="return showFrame(this.href);"><span><spring:message text="商店销售" /></span></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('CHART_GAMEPLAY')">
									<li class="tag"><a href="../game/charts/chargeStatistic" onclick="return showFrame(this.href);"><span><spring:message text="玩法通关" /></span></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('CHART_STRENGTHEN')">
									<li class="tag"><a href="../game/charts/chargeStatistic" onclick="return showFrame(this.href);"><span><spring:message text="强化几率" /></span></a></li>
								</sec:authorize>
							</ul>
						</li>
						</sec:authorize>
						<sec:authorize access="hasRole('NOTICE')">
						<li>
							<a><span class="title"><spring:message code="GAME.MENU.TITLE.MSGMANAGER" text="公告管理" /></span></a>
							<ul>
							<sec:authorize access="hasRole('MSG_SENDIM')">
								<li class="tag"><a href="../game/message/sendChatMsg" onclick="return showFrame(this.href);"><spring:message text="即时公告" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('MSG_CHATMSG')">
								<li class="tag"><a href="../game/message/listChatMailLog" onclick="return showFrame(this.href);"><spring:message text="循环公告" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('MSG_SYSMAIL')">
								<li class="tag"><a href="../game/message/addSystemMail" onclick="return showFrame(this.href);"><spring:message text="系统邮件" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('MSG_PERMAIL')">
								<li class="tag"><a href="../game/message/addUserMail" onclick="return showFrame(this.href);"><spring:message text="个人邮件" /></a></li>
							</sec:authorize>
							</ul>
						</li>
						</sec:authorize>
						<sec:authorize access="hasRole('CYCLE_DATA')">
							<li>
								<a><span class="title"><spring:message code="" text="周期数据" /></span></a>
								<ul>
								<sec:authorize access="hasRole('CYCLE_DATA_WEEK_CORRELATION')">
									<li class="tag"><a href="../game/periodic/weekCorrelation" onclick="return showFrame(this.href);"><spring:message text="周数据统计" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('CYCLE_DATA_MONTH_CORRELATION')">
									<li class="tag"><a href="../game/periodic/monthCorrelation" onclick="return showFrame(this.href);"><spring:message text="月数据统计" /></a></li>
								</sec:authorize>
								</ul>
							</li>
						</sec:authorize>
						<sec:authorize access="hasRole('USER_COMPONENT')">
							<li>
								<a><span class="title"><spring:message  text="用户构成" /></span></a>
								<ul>
								<sec:authorize access="hasRole('USER_COMPONENT_FIRST_CHARGE')">
									<li class="tag"><a href="../game/component/listFirstCharge" onclick="return showFrame(this.href);"><spring:message text="首充数据" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('USER_COMPONENT_NEW_USER')">
									<li class="tag"><a href="../game/component/listNewUserData" onclick="return showFrame(this.href);"><spring:message text="新用户数据" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('USER_COMPONENT_RERAIN_USER')">
									<li class="tag"><a href="../game/component/listRetainUser" onclick="return showFrame(this.href);"><spring:message text="留存用户数据" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('USER_COMPONENT_LOSS_WEEK_DISTRIBUTION')">
									<li class="tag"><a href="../game/component/listWeekLossDistribution" onclick="return showFrame(this.href);"><spring:message text="周流失玩家等级分布" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('USER_COMPONENT_LOSS_BOUT_DISTRIBUTION')">
									<li class="tag"><a href="../game/component/listLossDistribution" onclick="return showFrame(this.href);"><spring:message text="次流玩家等级分布" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('USER_COMPONENT_DAUDISTRIBUTION')">
									<li class="tag"><a href="../game/component/listDauDistribution" onclick="return showFrame(this.href);"><spring:message text="活跃玩家等级分布" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('USER_COMPONENT_NEWDISTRIBUTION')">
									<li class="tag"><a href="../game/component/listNewDistribution" onclick="return showFrame(this.href);"><spring:message text="新玩家等级分布" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('USER_COMPONENT_PAYDISTRIBUTION')">
									<li class="tag"><a href="../game/component/listPayDistribution" onclick="return showFrame(this.href);"><spring:message text="付费玩家等级分布" /></a></li>
								</sec:authorize>
								</ul>
							</li>
						</sec:authorize>
						<sec:authorize access="hasRole('ECOLOGY_DATA')">
							<li>
								<a><span class="title"><spring:message  text="生态数据" /></span></a>
								<ul>
								<sec:authorize access="hasRole('ECOLOGY_DATA_SERVER')">
									<li class="tag"><a href="../game/ecology/listEcology" onclick="return showFrame(this.href);"><spring:message text="区服资源生态" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('ECOLOGY_DATA_STATISTICS')">
									<li class="tag"><a href="../game/ecology/listEcologyStatistics" onclick="return showFrame(this.href);"><spring:message text="区服生态数据统计" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('ECOLOGY_DATA_DAU_RESOURCES')">
									<li class="tag"><a href="../game/ecology/listDauResources" onclick="return showFrame(this.href);"><spring:message text="活跃玩家资源统计" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('ECOLOGY_DATA_ITEM_SALES')">
									<li class="tag"><a href="../game/ecology/listItemSales" onclick="return showFrame(this.href);"><spring:message text="道具销售数据" /></a></li>
								</sec:authorize>
								<sec:authorize access="hasRole('ECOLOGY_DATA_FUNCTION_POINT')">
									<li class="tag"><a href="../game/ecology/listFunctionPoint" onclick="return showFrame(this.href);"><spring:message text="功能点参与统计数据" /></a></li>
								</sec:authorize>
								</ul>
							</li>
						</sec:authorize>
					</ul>
				</li>
				</sec:authorize>
				<sec:authorize access="hasRole('CLIENT_MANAGER')">
				<li>
					<a><i class="meteocons-cloud-sun"></i><span class="title"><h4><spring:message code="GAME.MENU.TITLE.CUSSERMANAGER" text="客服管理" /></h4></span></a>
					<ul>
					<sec:authorize access="hasRole('SERV_BLOCK')">
						<li>
							<a><span class="title"><spring:message text="监控工具" /></span></a>
							<ul>
							<sec:authorize access="hasRole('MSG_VIEWMSG')">
								<li class="tag"><a href="../game/language/viewChatMsg" onclick="return showFrame(this.href);"><spring:message text="聊天监控" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('BLOCK_BUSER')">
								<li class="tag"><a href="../game/block/blockUser" onclick="return showFrame(this.href);"><spring:message text="封禁帐号" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('BLOCK_BIP')">
								<li class="tag"><a href="../game/block/blockIp" onclick="return showFrame(this.href);"><spring:message text="封禁IP" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('BLOCK_BANUSER')">
								<li class="tag"><a href="../game/block/blockChat" onclick="return showFrame(this.href);"><spring:message text="禁言" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('BLOCK_BANWORD')">
								<li class="tag"><a href="../game/block/blockChatWord" onclick="return showFrame(this.href);"><spring:message text="禁言关键字" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('BLOCK_BANAUTO')">
								<li class="tag"><a href="../game/block/listAutoBlockChat" onclick="return showFrame(this.href);"><spring:message text="自动禁言列表" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('LOG_SUPLANDING')">
								<li class="tag"><a href="../game/block/loginServer" onclick="return showFrame(this.href);"><spring:message text="超级登录" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('BLOCK_KICKUSER')">
								<li class="tag"><a href="../game/block/kickOutUsers" onclick="return showFrame(this.href);"><spring:message text="踢用户下线" /></a></li>
							</sec:authorize>
							</ul>
						</li>
						</sec:authorize>
						<sec:authorize access="hasRole('SERV_LOG')">
						<li>
							<a><span class="title"><spring:message text="玩家管理工具" /></span></a>
							<ul>
							<sec:authorize access="hasRole('LOG_LOGIN')">
								<li class="tag"><a href="../game/querylog/listUserLoginLog" onclick="return showFrame(this.href);"><spring:message text="登录记录" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('LOG_ITEMLOG')">
								<li class="tag"><a href="../game/querylog/listUserItemLog" onclick="return showFrame(this.href);"><spring:message text="道具使用记录" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('LOG_CONSUMELOG')">
								<li class="tag"><a href="../game/querylog/listPlayerChargeRecord" onclick="return showFrame(this.href);"><spring:message text="玩家消费记录" /></a></li>
							</sec:authorize>
							</ul>
						</li>
						</sec:authorize>
						<sec:authorize access="hasRole('SERV_INFO')">
						<li>
							<a><span class="title"><spring:message text="数据查询" /></span></a>
							<ul>
							<sec:authorize access="hasRole('SERV_USERINFO')">
								<li class="tag"><a href="../game/search/playerInfo" onclick="return showFrame(this.href);"><spring:message text="玩家基本信息" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('SERV_RANKING')">
								<li class="tag"><a href="../game/search/listServerRank" onclick="return showFrame(this.href);"><spring:message text="排行榜" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('SERV_GUILD')">
								<li class="tag"><a href="../game/search/listFamilyInfo" onclick="return showFrame(this.href);"><spring:message text="公会列表" /></a></li>
							</sec:authorize>
							</ul>
						</li>
						</sec:authorize>
					</ul>
				</li>
				</sec:authorize>
				<sec:authorize access="hasRole('OPERATIONS_MANAGER')">
				<li>
					<a><i class="meteocons-cloud-sun"></i><span class="title"><h4><spring:message code="GAME.MENU.TITLE.OPERATIONMANAGER" text="运维管理" /></h4></span></a>
					<ul>
							<sec:authorize access="hasRole('SYSTEM_OPERATOR_LIST')">
								<li class="tag"><a href="../system/operator/listOperator" onclick="return showFrame(this.href);"><span><spring:message text="运营商列表" /></span></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('SYSTEM_SERVER_LIST')">
								<li class="tag"><a href="../system/server/listServer" onclick="return showFrame(this.href);"><span><spring:message text="服务器列表" /></span></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('SYSTEM_CHARGE_LIST')">			
								<li class="tag"><a href="../system/charge/listCharge" onclick="return showFrame(this.href);"><span><spring:message text="充值服列表" /></span></a></li>
							</sec:authorize>
					</ul>
				</li>
				</sec:authorize>
				<sec:authorize access="hasRole('SHENGJI_MANAGER')">
				<li>
					<a><i class="meteocons-cloud-sun"></i><span class="title"><h4><spring:message code="GAME.MENU.TITLE.SHENGJIMANAGER" text="审计管理" /></h4></span></a>
					<ul>
						<sec:authorize access="hasRole('MONTH_MANAGER')">
							<li>
								<a><span class="title"><spring:message  text="月度管理" /></span></a>
								<ul>
									<sec:authorize access="hasRole('SHENGJI_MANAGER_MONTHLY')">
										<li class="tag"><a href="../game/audit/monthUserUseGoldInfo" onclick="return showFrame(this.href);"><span><spring:message text="玩家月消费" /></span></a></li>
									</sec:authorize>
									<sec:authorize access="hasRole('SHENGJI_MANAGER_OPERATOR_MONTH')">
										<li class="tag"><a href="../game/audit/summaryData" onclick="return showFrame(this.href);"><span><spring:message text="运营商月汇总" /></span></a></li>
									</sec:authorize>
								</ul>
							</li>
						</sec:authorize>
					</ul>
					
				</li>
				</sec:authorize>
				
				<!-- <li><h4><a href="../game/mt3statistics/testPage" onclick="return showFrame(this.href)">测试页面</a></h4> -->
				<sec:authorize access="hasRole('AUTHORITY')">
				<li>
					<a><i class="meteocons-cloud-sun"></i><span class="title"><h4><spring:message code="GAME.MENU.TITLE.ADMINMANAGER" text="权限管理" /></h4></span></a>
					<ul>
						<sec:authorize access="hasRole('SECURITY')">
							<li>
								<a><span class="title"><spring:message code="GAME.MENU.TITLE.SECMANAGER" text="安全管理" /></span></a>
								<ul>
									<sec:authorize access="hasRole('SECURITY_USER_LIST')">
										<li class="tag"><a href="../security/user/listUser" onclick="return showFrame(this.href);"><span><spring:message text="用户管理" /></span></a></li>
									</sec:authorize>
									<sec:authorize access="hasRole('SECURITY_AUTH_LIST')">
										<li class="tag"><a href="../security/auth/pageAuth" onclick="return showFrame(this.href);"><span><spring:message text="权限管理" /></span></a></li>
									</sec:authorize>
									<sec:authorize access="hasRole('SECURITY_GROUP_LIST')">
										<li class="tag"><a href="../security/group/listGroup" onclick="return showFrame(this.href);"><span><spring:message text="权限组管理" /></span></a></li>
									</sec:authorize>
								</ul>
							</li>
						</sec:authorize>
					</ul>
				</li>
				</sec:authorize>
			</ul>
		</div>
	</div>
	
	<div class="main-content" style="height:100%;">
		<div class="topis">
			<div class="clock">
				<div id="Date"></div>
				<ul>
					<li id="hours"> </li>
					<li id="point">:</li>
					<li id="min"> </li>
					<li id="point">:</li>
					<li id="sec"> </li>
				</ul>
			</div>
		</div>
		<script>
			function showFrame (href) {
				$("iframe")[0].src = href;
				return false;
			}
			$(document).ready(function() {
				// 创建两个变量，一个数组中的月和日的名称
				var monthNames = [ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" ]; 
				var dayNames= ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"]
				// 创建一个对象newDate（）
				var newDate = new Date();
				// 提取当前的日期从日期对象
				newDate.setDate(newDate.getDate());
				//输出的日子，日期，月和年
				$('#Date').html(newDate.getFullYear() + " " + monthNames[newDate.getMonth()] + ' ' + newDate.getDate() + ' ' + dayNames[newDate.getDay()]);
				setInterval( function() {
					// 创建一个对象，并提取newDate（）在访问者的当前时间的秒
					var seconds = new Date().getSeconds();
					//添加前导零秒值
					$("#sec").html(( seconds < 10 ? "0" : "" ) + seconds);
				},1000);
				setInterval( function() {
					// 创建一个对象，并提取newDate（）在访问者的当前时间的分钟
					var minutes = new Date().getMinutes();
					// 添加前导零的分钟值
					$("#min").html(( minutes < 10 ? "0" : "" ) + minutes);
			    },1000);
				setInterval( function() {
					// 创建一个对象，并提取newDate（）在访问者的当前时间的小时
					var hours = new Date().getHours();
					// 添加前导零的小时值
					$("#hours").html(( hours < 10 ? "0" : "" ) + hours);
			    }, 1000);
			}); 
		</script>
		<iframe src="about:blank" name="mainFrame" id="mainFrame" scrolling="auto" frameborder="0" height="100%" width="100%"></iframe>
	</div>
<script>
	initpage();
</script>	
</div>
