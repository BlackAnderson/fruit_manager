package com.watermelon.manager.common.manager.common.constant;

public enum FunctionType {
	// vip====================================
	VIP_EXP("vip经验", 1),
	VIP_BOUNS("vip奖励", 1),
	// 战斗====================================
	FIGHT_BOUNS("战斗奖励", 1),
	FIGHT_CHAPTER("战斗章节奖励", 1),

	COPY_METERIAL("材料副本", 1),
	COPY_METERIALSAODANG("购买副本次数", 0),
	Item_UseItem("使用道具", 1),
	Item_ObtainItem("获得节日宝箱", 1),
	
	Mail_Fj("邮件附件", 1),
	Suit_Split("套装分解", 1),
	// Equip===================================
	Equip_Enhance("装备强化", 1),
	Equip_Load("装备穿戴", 1),
	Equip_Stone("宝石提升", 1),
	Equip_Soul("装备注灵", 1),
	Equip_LongSoul("龙魂", 1),
	Equip_Defence("护盾", 1),
	// Bag====================================
	Bag_MeltingEquip("熔炼", 1),
	Bag_OpenBag("购买背包格子", 0),
	Bag_DecomposeEquip("橙装分解", 1),
	Bag_DecomposeLegendEquip("传奇装备分解", 1),
	Bag_FightFigureEquip("战纹分解", 1),
	Bag_Miji("秘籍置换", 1),
	// 召唤====================================
	BOSS_SINGLE("个人boss", 1),
	BOSS_CALLBOSS("召唤boss", 1),
	BOSS_QUANMINGBOSS("全民boss", 1),
	BOSS_QUANMINGBOSSKILL("全民boss击杀", 1),
	// 装备====================================
	ENHANCE_MODULE("装备模块", 1),

	OFFLINE_REWARD("离线奖励", 1),
	// 王者====================================
	King_EndBattle("王者战斗奖励", 1),
	King_Week("王者周奖励", 1),
	King_Count("购买王者次数", 0),
	King_Create("创建王者数据", 1),
	King_RecoveryCount("王者次数自动恢复", 1),
	// 充值====================================
	RECHAGE_RMB("订单充值", 1),
	RECHAGE_INNERRMB("内币充值", 1),
	// 排行榜====================================
	RANK_WARSHIP("排行崇拜", 1),
	// 经脉====================================
	JIMAI_MODULE("经脉模块", 1),
	// 翅膀====================================
	WING_MODULE("翅膀模块", 1),
	WING_EQUIP("翅膀装备模块", 1),
	WING_EQUIPCHANGE("翅膀装备转化", 1),
	WING_STARUP("翅膀升星", 1),
	WING_LEVELUP("翅膀升阶", 1),
	WING_USEUPITEM("翅膀直升丹", 1),
	// 转身====================================
	ZhuanSheng_Exp("转生经验", 1),
	ZhuanSheng_MODULE("转生模块", 1),
	ZhuanSheng_RELIVE("转生boss复活", 0),
	ProtectBiQi_RELIVE("守卫比奇复活", 0),
	Summon_RELIVE("公会boss复活", 0),
	QuanMing_RELIVE("全名boss复活", 0),
	//战纹=====================================
	FightFigure_EQUIP("战纹模块", 1),
	FightFigure_PICK("战纹替换下来的装备入包",1),
	//轮回=====================================
	LunHui_MODULE("轮回模块", 1),
	LunHui_Exp("轮回经验", 1),
	//轮回=====================================
	Sky_EQUIP("焚天模块", 1),
	Sky_Transition("焚天转换", 1),
	//春节Boss=====================================
	SpringFestivalReward("春节Boss阶段性奖励宝箱领取", 1),
	
	
	
	F9("F9", 1),
	TEST("测试用例", 1),
	SWEEP("扫荡", 1),
	CREATE("创建角色", 1),
	GUIDE("新手大礼包", 1),
	USEBOX("使用物品", 1),
	LOTT("抽奖", 1),
	EXTRACTEQUIP("抽装备", 1),
	PICKINBAG("抽装仓库提取", 1),
	MIJICHANGE("秘籍置换", 1),
	MAILANNEXES("提取邮件附件", 1),
	RESETSKILL("重置技能", 1),
	UPGRADESKILL("升级技能", 1),
	REGETCOST("处理补偿资源", 1),
	GUAJI("挂机", 1),
	RECHARGE("充值", 1),
	ACTIVITY("活动", 1),
	ACTIVITY_GONGFU("活动神功", 1),
	ACTIVITY_LIMITSHOP("开服活动-全民限购", 0),
	SENDGIFT("赠送道具", 1),
	LEVELUP("玩家升级", 1),
	EXTACTEQUIP("寻宝", 0),
	EXTACTACTIVITY("活动寻宝", 0),
	MONEYTREE("摇钱树", 1),
	TURNTABLE("幸运转盘", 1),
	// 商城========================
	SHOP_ARENA("竞技场购买道具", 1),
	SHOP_FAMILY("军团商店", 1),
	SHOPMALL_BUY("新版商城-兑换商店", 1),
	SHOP_MYSTERIOUS("商城-神秘商店", 0),
	SHOP_INTERGRAL("商城-积分商店", 0),
	SHOP_SHOPPING("商城-商店", 0),
	SHOP_COMBATGAINS("商城-战绩商店", 0),

	// 武将============================
	HERO_STARUP("武将升星", 1),
	HERO_QUALITYUP("武将进阶", 1),
	HERO_SKILLUP("武将技能升级", 1),
	HERO_EQUIPUP("升级橙装", 1),
	HERO_EQUIPUPCHUANQI("升级传奇", 1),
	HERO_EQUIPCOMPOSE("合成橙装", 1),
	HERO_EQUIPCOMPOSECHUANQI("合成传奇", 1),
	// 副本=====================================
	COPY_NONE("普通副本", 1),
	COPY_EQUIP("装备副本", 1),
	COPY_BUYCURRENY("购买精力", 1),

	// 竞技场====================================
	ARENA_ONWINREWARD("每日胜利次数奖励", 1),
	ARENA_RANK("排名奖励", 1),
	ARENA_BUYCOUNT("购买挑战次数", 1),
	ARENA_CHALLENGE("竞技场挑战", 1),
	ARENA_CLEARCD("竞技场清除CD", 1),
	ARENA_BET("竞技场下注", 1),

	// 任务==============================
	TASK_ACTIVE("领取活跃奖励", 1),
	TASK_Daily("日常任务奖励", 1),
	TASK_ACHIEVE("领取成就奖励", 1),
	TASK_REWARD("领取任务奖励", 1),
	TASK_RESET("重置日常任务", 1),
	TASK_FLUSH("刷新日常任务", 1),
	CTIVE_BOX("活跃宝箱", 1),
	ACTIVE_BOX("活跃宝箱", 1),
	LONGBALL("龙珠",1),

	// 军团====================================
	FAMILY_CREATE("军团创建", 1),
	FAMILYBATTLE("百团混战", 1),
	FAMILYACTIVE("军团活跃领取", 1),
	FAMILYCOPY("军团副本领取", 1),
	FAMILYALLOT("军团仓库分配", 1),
	FAMILY_DONATE("军团捐献", 1),
	FAMILY_CLEARCD("军团清除CD", 1),

	// 装备====================================
	BUY_EQUIPMAX("购买帮被仓库上限", 1),
	// 遭遇战====================================
	MEET_SEARCH("增加遭遇目标", 0),
	MEET_REVENGE_COUNT("消耗遭遇复仇次数", 1),
	MEET_BUYREVENGE_COUNT("购买复仇次数", 0),
	MEET_BONUS("遭遇战奖励", 1),
	MEET_DAILY("遭遇战每日重置", 1),
	MEET_DROP("遭遇战掉落", 1),
	MEET_HONOR("遭遇战PK值", 1),

	EXCHANGE_CARD("兑换码", 1),
	ACHIEVE("成就奖励", 1),
	DeductionJungong("0点系统扣除军功", 1),
	Study_FamilySkill("学习工会技能", 1),
	Family_ShopBuy("工会商店购买道具", 1),
	BOOK_STUDY("学习秘籍", 1),
	BOOK_BACKOUT("拆除秘籍", 1),
	BOOK_SPLIT("分解秘籍", 1), 
	MODULE_GIFT("功能礼包",1), 
	Impeachment("公会弹劾",1),
	
	// 主城====================================
	MAIN_CITY_CUER("治疗扣费", 1),
	MAIN_CITY_FIGHT("主城战斗", 1),
	// 时装
	FATION_ACTIVITY("时装激活",1),
	// 护镖
	DART_CLEAN_CD("护镖清除CD",1),
	DART_REWARD("护镖奖励",1),
	DART_ONE_KEY_GOLD("护镖一键刷橙",1),
	DART_REFRESH_QUALITY("护镖刷品质",1),
	DART_DOUBLE_REWARD("双倍奖励",1),
	DART_ROB("护镖抢夺",1),
	DART_REVENGE("护镖复仇",1),
	Daily_Sign("每日签到",1),
	// 守卫比奇
	PROTECT_BIQI_REBORN("守卫比奇复活",1),
	PROTECT_BIQI_SOLDIER("守卫比奇攻击小怪",1),
	PROTECT_BIQI_BOSS("守卫比奇BOSS奖励",1),
	FULING("装备附灵",1),
	FULING_Recovery("装备附灵回收",1),
	//公会仓库
	GrabRedPacket("抢红包",1),
	// 沙城
	SANDY_FAMILY_DAILY_REWARD("沙城家族每日奖励",1),
	SANDY_REBORN_COST("沙城复活扣费",1),
	SANDY_SCORE_REWARD("沙城积分达标奖励",1),
	FAMILY_MAGIC_REWARD("公会密境奖励",1),
	
	
	// 夜战比奇
	Night_REBORN_COST("夜战比奇复活扣费",1),
	NIGHT_PICK_BOX("夜战比奇拾取宝箱奖励",1),
	NIGHT_SCORE_REWARD("夜战比奇积分达标奖励",1),
	
	//锁妖塔
	SYAOTOWERSWEEP("锁妖塔扫荡",1),
	SYAOTOWERBATTLE("锁妖塔战斗",1),
	SYAOTOWERADDBUFF("锁妖塔使用鼓舞药水",1),
	
	//跨服单排
	MERGERSINGLE_CREAT("创建单排数据", 1),
	MERGERSINGLE_ADDTIMES("跨服单排增加次数",1),
	MERGERSINGLE_STAGEBONUS("跨服单排段位奖励",1),
	MERGERSINGLE_DAILYBONUS("跨服单排每日奖励",1),
	MERGERSINGLE_EndBattle("跨服单排结束", 1),
	MERGERSINGLE_Title("跨服单排称号奖励", 1),
	;
	
	String des;
	int deleted;// 0表示后台中使用此来源 1表示不使用

	private FunctionType(String des, int del) {
		this.des = des;
		this.deleted = del;
	}

	public boolean isUse() {
		if (deleted == 1) {
			return false;
		}
		return true;
	}

	public String getDes() {
		return des;
	}
	public int getDeleted() {
		return deleted;
	}

}
