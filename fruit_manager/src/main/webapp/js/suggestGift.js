var Class = {
  create: function() {
    return function() {
      this.initialize.apply(this, arguments);
    }
  }
}
var suggestboxObj = null;

var Suggest = Class.create();
Suggest.prototype = {
	initialize:function(objName,options){
		this.selectList = new Array();
		this.options=options||{};
		this.keywordListElement=false;
		this.currentSelectIndex=-1;
		this.currentSearchWord="";
		this.currentSearchResult=null;
		this.defalutHight = 300;
		this.keywordSelectElement=false;
		
		this.keywordInputElement = document.getElementById(objName);
		xy = getXY(this.keywordInputElement);
		this.keywordInputElementX = xy[0];
		this.keywordInputElementY = xy[1];
		
		this.handle();
	}, handle:function () {
		if(suggestboxObj==null){
			suggestboxObj = window.document.createElement("div");
			
			window.document.body.appendChild(suggestboxObj);
		}
		suggestboxObj.setAttribute('id','swordlist');
		suggestboxObj.style.position="absolute";
		suggestboxObj.style.display="none";
		suggestboxObj.style.fontSize="14px";
		suggestboxObj.style.width= this.keywordInputElement.offsetWidth+"px";
		suggestboxObj.style.maxHeight = this.defalutHight + "px";
		suggestboxObj.style.overflowX="hidden";
		suggestboxObj.style.overflowY="auto";
		suggestboxObj.style.marginLeft="1px";
		suggestboxObj.style.border=" 1px solid #adadad";
		suggestboxObj.style.zIndex = "999";
		
		this.keywordSelectElement = document.getElementById("swordlist");
		
		this.keywordSelectElement.innerHTML = "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td></td></tr></table>";
		
		this.keywordListElement = this.keywordSelectElement.firstChild.rows[0].firstChild;
		this.currentSelectIndex = 0;
		this.keywordInputElement.onblur = function () {
			suggest.doSelect();
		};
		
		try {
			//Mozilla系列，需要用到addEventListener
			//this.keywordInputElement.addEventListener("keydown", this.checkKeyDown, false);
			this.keywordInputElement.addEventListener("keypress", this.keypressProc, false);
			this.keywordInputElement.addEventListener("keyup", this.keyupProc, false);
		}
		catch (e) {
			try {
				//IE系列，需要用到attachEvent
				this.keywordInputElement.attachEvent("onkeydown", this.checkKeyDown);
				this.keywordInputElement.attachEvent("onkeypress", this.keypressProc);
				this.keywordInputElement.attachEvent("onkeyup", this.keyupProc);
			}
			catch (e) {
			}
		}
	}, preventEvent:function (event) {
		event.cancelBubble = true;
		event.returnValue = false;
		if (event.preventDefault) {
			event.preventDefault();
		}
		if (event.stopPropagation) {
			event.stopPropagation();
		}
	}, checkKeyDown:function (event) {
		this.currentSelectIndex = 0;
		var keyCode = event.keyCode;
		if (keyCode == 38 || keyCode == 40) {
			suggest.clearFocus();
			if (keyCode == 38) {
				suggest.upSelectIndex();
			} else {
				suggest.downSelectIndex();
			}
			suggest.setFocus();
		}
	}, keyupProc:function (event) {
		this.currentSelectIndex = 0;
		var keyCode = event.keyCode;
		suggest.changeKeywordSelect()
		if (keyCode == 13) {
			suggest.doSelect();
		}
	}, keypressProc:function (event) {
		this.currentSelectIndex = 0;
		var keyCode = event.keyCode;
		if (keyCode == 13) {
			suggest.preventEvent(event);
		} else {
			if (keyCode == 38 || keyCode == 40) {
				suggest.preventEvent(event);
				suggest.clearFocus();
				if (keyCode == 38) {
					suggest.upSelectIndex();
				} else {
					suggest.downSelectIndex();
				}
				suggest.setFocus();
			} else {
				if (keyCode == 108 || keyCode == 110 || keyCode == 111 || keyCode == 115) {
					setTimeout("suggest.changeKeywordSelect()", 20);
				}
			}
		}
	}, clearFocus:function (index) {
		var index = this.currentSelectIndex;
		try {
			var x = this.findTdElement(index);
			x.style.backgroundColor = "white";
		}
		catch (e) {
		}
	}, findTdElement:function (index) {
		try {
			var x = this.keywordListElement.firstChild.rows;
			for (var i = 0; i < x.length; ++i) {
				if (x[i].firstChild.idx == index) {
					return x[i].firstChild;
				}
			}
		}
		catch (e) {
		}
		return false;
	}, upSelectIndex:function () {
		var index = this.currentSelectIndex;
		if (this.keywordListElement.firstChild == null) {
			return;
		}
		var x = this.keywordListElement.firstChild.rows;
		var i;
		for (i = 0; i < x.length; ++i) {
			if (x[i].firstChild.idx == index) {
				break;
			}
		}
		if (i == 0) {
			this.currentSelectIndex = (x.length - 1);
		} else {
			this.currentSelectIndex = x[i - 1].firstChild.idx;
		}
	}, downSelectIndex:function () {
		var index = this.currentSelectIndex;
		if (this.keywordListElement.firstChild == null) {
			return;
		}
		var x = this.keywordListElement.firstChild.rows;
		var i = 0;
		for (; i < x.length; ++i) {
			if (x[i].firstChild.idx == index) {
				break;
			}
		}
		if (i >= x.length - 1) {
			this.currentSelectIndex = x[0].firstChild.idx;
		} else {
			this.currentSelectIndex = x[i + 1].firstChild.idx;
		}
	}, setFocus:function () {
		var index = this.currentSelectIndex;
		try {
			var x = this.findTdElement(index);
			x.style.backgroundColor = "#D5F1FF";
			
			var inputXY = getXY(this.keywordInputElement);
			var selestObjXY = getXY(x);
			var tdHeight = x.offsetHeight;
			//设置滚动条,让当前选择的项一直在可见区域
			if((selestObjXY[1] - inputXY[1]) > (this.defalutHight-10)){
				this.keywordSelectElement.scrollTop = index*tdHeight-(this.defalutHight - tdHeight);
			}
			else if((selestObjXY[1] - inputXY[1]) < 10){
				this.keywordSelectElement.scrollTop = index*tdHeight;
			}
		}
		catch (e) {
		}
	},searchKeywordSuggest:function (keyword) {
		if(keyword!=""){
			if(this.currentSearchWord!=keyword){
				this.currentSearchWord = keyword;
				if(this.options.oninput){
					this.options.oninput(keyword);
				}
			}
			else{
				if(this.currentSearchResult!=null)this.showSuggestKeyword(this.currentSearchResult);
			}
		}
	}, showSuggestKeyword:function (listj) {
		if (listj.result == 0) {
			this.selectList.clean(); 
			this.keywordSelectElement.style.display = "none";
		} else {
			
			var keywords = [];
			this.selectList = listj;
			for (var i = 0; i < listj.length; ++i) {
				keywords.push(listj[i].name+(listj[i].type==2?("["+gradeWord(listj[i].grade)+"]"):""));
			}
			
			if (keywords.length > 0) {
				xy = getXY(this.keywordInputElement);
				this.keywordInputElementX = xy[0];
				this.keywordInputElementY = xy[1];
				
				//this.currentSelectIndex = 0 ;
				this.keywordSelectElement.style.left = this.keywordInputElementX + "px";
				var isSafari;
				if((isSafari=navigator.userAgent.indexOf("Safari"))>0){
					this.keywordSelectElement.style.top = (this.keywordInputElementY + this.keywordInputElement.offsetHeight + 20) + "px";
					
				}else
					this.keywordSelectElement.style.top = (this.keywordInputElementY + this.keywordInputElement.offsetHeight) + "px";
				
				
				this.keywordSelectElement.style.zIndex = "2000";
				this.keywordSelectElement.style.paddingRight = "0";
				this.keywordSelectElement.style.paddingLeft = "0";
				this.keywordSelectElement.style.paddingTop = "0";
				this.keywordSelectElement.style.paddingBottom = "0";
				this.keywordSelectElement.style.backgroundColor = "white";
				this.keywordSelectElement.style.display = "block";
				var myTable = document.createElement("TABLE");
				myTable.width = "100%";
				myTable.cellSpacing = 0;
				myTable.cellPadding = 3;
				var tbody = document.createElement("TBODY");
				myTable.appendChild(tbody);
				for (var i = 0; i < keywords.length; i++) {
					var tr = document.createElement("TR");
					var td = document.createElement("TD");
					td.nowrap = "true";
					td.align = "left";
					td.innerHTML = keywords[i];
					td.idx = i;
					td.onmouseover = function () {
						suggest.clearFocus();
						suggest.currentSelectIndex = this.idx;
						suggest.setFocus();
						this.style.cursor = "hand";
					};
					td.onmouseout = function () {
					};
					td.onclick = function () {
						suggest.doSelect();
					};
					tr.appendChild(td);
					tbody.appendChild(tr);
				}
				this.keywordListElement.innerHTML = "";
				this.keywordListElement.appendChild(myTable);
				this.setFocus();
			} else {
				this.keywordSelectElement.style.display = "none";
				this.currentSelectIndex = -1;
			}
		}
	}, changeKeywordSelect:function () {
		var userInput = this.keywordInputElement.value;
		if(userInput.trim() != ""){
			this.searchKeywordSuggest(userInput);
		}
	}, doSelect:function () {
		this.keywordSelectElement.style.display = "none";
		if (this.keywordInputElement.value.trim() == "") {
			return;
		}
		var currentItem = this.selectList[this.currentSelectIndex];
		if(currentItem){
			nowselitem = currentItem;
			$("#itemNumVal").val(1);
			
			this.keywordInputElement.value = currentItem.name;
		}
	}
}