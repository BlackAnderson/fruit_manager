<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">

function checkedAllServer(checkbox) {  // 分段服务器全选
	var inputs = $("input[name='serverIds']")
	if($(checkbox).is(":checked")) { 
		for(var i = 0; i<inputs.length; i++) {
			$(inputs[i]).prop("checked", true);
		}
	} else {
		for(var i = 0; i<inputs.length; i++) {
			$(inputs[i]).removeAttr("checked", false);
		}
	}
}


function checkedServerIndex(checkbox){//区服多选
	var rangeIndexStr = $(checkbox).val();
	var rangeIndex = rangeIndexStr.split("-");
	var count = rangeIndex[1] - rangeIndex[0];
	rangeIndex[0] = rangeIndex[0]-1;
	var inputs = $("input[name='serverIds']");
	if ($(checkbox).is(":checked") == true) {
		for(var i = 0; i<count+1; i++) {
			$(inputs[parseInt(rangeIndex[0]) + i]).prop("checked", true);
		}
	} else { // 取消全选
		for(var i = 0; i<count+1; i++) {
			$(inputs[parseInt(rangeIndex[0]) + i]).removeAttr("checked", false);
		}
	}
}
</script>
<div class="form-group">
	<label class="col-sm-2 control-label">选择服务器</label>
	<div class="col-sm-10" id="a">
		<c:forEach items="${_serverList}" var="servers">
				<c:if test="${ servers.status == 'ENABLE'}">
					<label><input name="serverIds" type="checkbox" id="serverIds_${servers.serverId }" value="${servers.serverId }" title="${servers.startDate }" />${servers.name }</label>
				</c:if>
		</c:forEach><br />
		<input name="allServers" type="checkbox" id = "allServers" onclick="checkedAllServer(this)" /> 全选
		<c:set value="100" var = "rangeNum" />
		<c:set value= "${serverList.size() }" var = "activeCount" />
		<c:set value= "${activeCount + 1 }" var = "count" />
		<c:set value="${count%rangeNum}" var ="otherCount"  />
		<c:set value= "${count - otherCount }" var = "innerCount" />
		<c:set value = "${innerCount / rangeNum}" var = "rangeCount" />
		<c:set value="0" var = "min" />
		<c:if test="${rangeCount >0 }" >
			<c:forEach begin="1" end="${rangeCount }" step="1"  var="range">
					<input name="serversIndex" type="checkbox" id="serversIndex" onclick="checkedServerIndex(this)" value="${min+1 }-${range*rangeNum }" on />${min+1 } - ${range*rangeNum }
					<c:set var="min" value="${range*rangeNum }" />
			</c:forEach>
		</c:if>
		<c:if test="${otherCount > 0 }" >
			<input name="serversIndex" type="checkbox" id="serversIndex" onclick = "checkedServerIndex(this)" value="${min + 1 } - ${min+otherCount }"/> ${min + 1 } - ${min+otherCount-1 }
		</c:if>
		
	</div>
</div>
