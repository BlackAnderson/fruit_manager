String.prototype.getLength=function(){return this.replace(/[^\x00-\xff]/gi,'xxx').length;};
String.prototype.trim = function () {return this.replace(/(^\s*)|(\s*$)/g, "");};
String.prototype.replaceJSTag=function(){return this.replace(/\'/g,'\\\'').replace(/\"/g,'\\\"');}

Date.prototype.before = function(d){return this.getTime() < d.getTime();}
Date.prototype.after = function(d){return this.getTime() > d.getTime();}
Date.prototype.equal = function(d){return this.getTime() == d.getTime();}
Date.prototype.nextDate = function (d){
	return new Date(this.getTime() + 24 * 3600 * 1000);
}
Date.prototype.prevDate = function (d){
	return new Date(this.getTime() - 24 * 3600 * 1000);
}

isSafari = (document.childNodes && !document.all && !navigator.taintEnabled);  
var getXY = function(xyel) {
	if (document.documentElement.getBoundingClientRect) { // IE  
	    var box = xyel.getBoundingClientRect();  
	
	    var rootNode = xyel.ownerDocument;  
	    return [box.left + getDocumentScrollLeft(rootNode), box.top +  
		    getDocumentScrollTop(rootNode)];  
	} else {
	    var pos = [xyel.offsetLeft, xyel.offsetTop];  
	    var parentNode = xyel.offsetParent;  
	
	    // safari: subtract body offsets if el is abs (or any offsetParent), unless body is offsetParent  
	    var accountForBody = (isSafari &&  
	    		xyel.style['position'] == 'absolute' &&  
	    		xyel.offsetParent == xyel.ownerDocument.body);  
	
	    if (parentNode != xyel) {  
		while (parentNode) {  
		    pos[0] += parentNode.offsetLeft;  
		    pos[1] += parentNode.offsetTop;  
		    if (!accountForBody && isSafari &&   
			    parentNode.style['position'] == 'absolute' ) {   
			accountForBody = true;  
		    }  
		    parentNode = parentNode.offsetParent;  
		}  
	    }  
	
	    if (accountForBody) { //safari doubles in this case  
		pos[0] -= xyel.ownerDocument.body.offsetLeft;  
		pos[1] -= xyel.ownerDocument.body.offsetTop;  
	    }   
	    parentNode = xyel.parentNode;  
	
	    // account for any scrolled ancestors  
	    while ( parentNode.tagName && !/^body|html$/i.test(parentNode.tagName) )   
	    {  
	       // work around opera inline/table scrollLeft/Top bug  
	       if (parentNode.style['display'].search(/^inline|table-row.*$/i)) {   
		    pos[0] -= parentNode.scrollLeft;  
		    pos[1] -= parentNode.scrollTop;  
		}  
		  
		parentNode = parentNode.parentNode;   
	    }  
	
	    return pos;  
	}
},
/** 
* Returns the left scroll value of the document  
* @method getDocumentScrollLeft 
* @param {HTMLDocument} document (optional) The document to get the scroll value of 
* @return {Int}  The amount that the document is scrolled to the left 
*/  
getDocumentScrollLeft = function(doc) {  
	doc = doc || document;  
	return Math.max(doc.documentElement.scrollLeft, doc.body.scrollLeft);  
},

/** 
* Returns the top scroll value of the document  
* @method getDocumentScrollTop 
* @param {HTMLDocument} document (optional) The document to get the scroll value of 
* @return {Int}  The amount that the document is scrolled to the top 
*/  
getDocumentScrollTop = function(doc) {  
	doc = doc || document;  
	return Math.max(doc.documentElement.scrollTop, doc.body.scrollTop);  
};

function check_input_length(inid,l){
	var cv = document.getElementById(inid).value.trim();
	var cl=cv.getLength();
	ml=l*3;
	if(cl<=ml){
		document.getElementById(inid+"_num").innerHTML = Math.ceil(cl/3);
		return true;
	}
	var nv='';
	for(var i=0;i<cv.length;i++){
		var cvc=cv.substring(i,i+1);
		var cvcl=cvc.getLength();
		var nvl=nv.getLength();
		if(nvl>=ml||(cvcl>1&&(nvl+cvcl)>ml)){
			break;
		}
		nv+=cvc;
	}
	document.getElementById(inid).value = nv;
	document.getElementById(inid+"_num").innerHTML= Math.ceil(nv.getLength()/3);
	return true;
}

function chengTextarea(textvalue){
	var regRN = /[\r\n]/g;
	var regR = /[\r]/g;
	var regN = /[\n]/g;
	var descrip = textvalue.replace(regRN,';');
	descrip = descrip.replace(regN,';');
	descrip = descrip.replace(regR,'');
	
	var tmp = descrip.split(';');
	var result = '';
	//去除其中带空格的部分，例：有可能得到这样的情况，非人们所愿意的格式“;;;”
	var tmpList = new Array();
	for(var i=0;i< tmp.length;i++){
		var item = tmp[i].trim();
		if(item != ""){
			var match = false;
			for(var j=0;j<tmpList.length;j++){
				if(tmpList[j] == item){
					match = true;
					break;
				}
			}
			if(!match){
				tmpList.push(item);
				result=result+tmp[i].trim()+';';
			}
			else{
				alert("存在重复数据：" + tmp[i].trim());
			}
		}
	}
	
	return result.substring(0,result.length-1);
}

/*
用途：检查输入字符串是否符合正整数格式
输入：
s：字符串
返回：
如果通过验证返回true,否则返回false
*/
function isNumber( s ){
	var regu = "^(-?)[0-9]+$";
	var re = new RegExp(regu);
	if (s.search(re) != -1) {
		return true;
	} else {
		return false;
	}
}

function isUrl(s) {
	var regexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
	return regexp.test(s);
}

//日期检查函数
function isDate(v){
	var r = v.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
	if(r==null){return false;}

	var d = new Date(r[1], r[3]-1,r[4]);
	return (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4]);
}

//时间检查函数
function isDateTime(v){
	var r = v.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})(\s)(\d{1,2})(:|\/)(\d{1,2})\7(\d{1,2})$/);
	if(r==null)return false;

	var d = new Date(r[1], r[3]-1,r[4],r[6],r[8],r[9]);
	return (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4] && d.getHours() == r[6] && d.getMinutes() == r[8] && d.getSeconds() == r[9]);
}

//字符串转时间
function string2date(str){
	return new Date(str.replace(/-/g,"\/"));
}
//日期转字符串
function date2string(d){
	var str = d.getFullYear() + "-";
	str += ((d.getMonth() + 1)<10?("0"+(d.getMonth() + 1)):(d.getMonth() + 1)) + "-";
	str += (d.getDate()<10?("0"+d.getDate()):d.getDate());
	return str;
}
//时间转字符串
function time2string(d){
	var str = d.getFullYear() + "-";
	str += ((d.getMonth() + 1)<10?("0"+(d.getMonth() + 1)):(d.getMonth() + 1)) + "-";
	str += (d.getDate()<10?("0"+d.getDate()):d.getDate()) + " ";
	str += (d.getHours()<10?("0"+d.getHours()):d.getHours()) + ":";
	str += (d.getMinutes()<10?("0"+d.getMinutes()):d.getMinutes()) + ":";
	str += (d.getSeconds()<10?("0"+d.getSeconds()):d.getSeconds());
	return str;
}
/**
 * 获得当天的0点时间
 */
function getDate(){
	var d = new Date();
	return new Date(d.getFullYear(), d.getMonth(), d.getDate());
}

/**
 * JS MAP
 * @param {Object} key
 * @param {Object} value
 * @memberOf {TypeName} 
 */
function struct(key, value) {
	this.key = key;
	this.value = value;
}
function MapClass(){
	this.map = new Array();
	this.set=function(key, value) {
		for (var i = 0; i < this.map.length; i++){
			if ( this.map[i].key === key ){
				this.map[i].value = value;
				return;
			}
		}
		this.map[this.map.length] = new struct(key, value);
	},
	this.get=function(key){
		for (var i = 0; i < this.map.length; i++){
			if ( this.map[i].key === key ){
				return this.map[i].value;
			}
		}
		return null;
	},
	this.removeKey=function(key){
		var v;
		for (var i = 0; i < this.map.length; i++){
			v = this.map.pop();
			if ( v.key === key )
			continue;

			this.map.unshift(v);
		}
	},
	this.getCount=function() {
		return this.map.length;
	},
	this.key2List=function() {
		var ret = new Array();
		for (var i = 0; i < this.map.length; i++){
			ret.push(this.map[i].key);
		}
		return ret;
	},
	this.isEmpty=function() {
		return this.map.length <= 0;
	}
}