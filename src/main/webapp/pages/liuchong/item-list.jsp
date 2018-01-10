<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="easyui-datagrid" id="itemTransList" title="商品列表" 
       data-options="singleSelect:false,collapsible:true,pagination:true,striped:true,rownumbers:true,url:'${pageContext.request.contextPath}/access/liuchong/tranactionList',method:'get',pageSize:20,toolbar:toolbar">
    <thead>
        <tr>
        	<th data-options="field:'id',hidden:'true'">ID</th>
            <th data-options="field:'created',width:120,align:'center',formatter:TAOTAO.formatDateTime">申请日期</th>
            <th data-options="field:'type',width:120">商品种类</th>
            <th data-options="field:'user',width:120">申请人</th>
            <th data-options="field:'toUser',width:120">领用人</th>
            <th data-options="field:'num',width:120">申请数量</th>
           <!--  <th data-options="field:'count',width:120">当时库存</th> -->
            <th data-options="field:'reason',width:240">描述</th>
            <th data-options="field:'action',width:150,align:'center',formatter:formatOper">操作</th>  
        </tr>
    </thead>
</table>
<div id="tb" style="padding:5px">  
        <div>  
           <!--  查询申请人：<input type="text" name="user" style="width:150px" class="easyui-textbox"/>  &nbsp;&nbsp; -->
            查询日期：  <input id="timebox" type="text" class="easyui-datebox">&nbsp;&nbsp;&nbsp;
            查询种类：  <input id="QueryTypes"  class="easyui-combobox"  type="text" data-options="valueField:'id',textField:'text',width:171" />&nbsp;&nbsp;&nbsp;
            操作类型：  <select id="orderType" class="easyui-combobox" name="orderType" style="width:100px;">
			    <option value="">全选</option>
			    <option value="入库">入库</option>
			    <option value="出库">出库</option>
			</select>&nbsp;&nbsp;&nbsp;
            <a href="#" class="easyui-linkbutton"  onclick="itemObj.search()">查询</a>  
        </div>  
    </div>
<div id="ReceiveFeedBackDialog" class="easyui-dialog" closed="true" buttons="#dlg-buttons" align="center" title="即将撤回的记录" style="width:auto;height:auto;">
	<form id="receiveForm" class="receiveForm" method="post">
	    <table cellpadding="5">
	    	<tr>
	            <td>时间:</td>
	            <td><span id ="callBackRowDate" style="color:red"></span></td>
	        </tr>
	        <tr>
	            <td>商品:</td>
	            <td><span id ="callBackRowType" style="color:red"></span></td>
	        </tr>
	        <tr>
	            <td>数量:</td>
	            <td><span id ="callBackRowNum" style="color:red"></span></td>
	        </tr>
	        <tr>
	            <td>描述:</td>
	            <td><span id ="callBackRowDesc" style="color:red"></span></td>
	        </tr>
	        <tr>
	            <td><input class="easyui-numberbox" type="text" id="receivePw" data-options="min:0,max:999999,required:true" style="width: 80px;"></input></td>
	            <td><input class="easyui-linkbutton" id="callBackCode" type="button" onclick="sendCode()" value="免费发送验证码  "/></td>
	        </tr>
	    </table>
	</form>
</div> 
    <div id="dlg-buttons">
        <a href="#" class="easyui-linkbutton" onclick="formSubmit();" id="receiveYes">确认</a> <a href="#"
            class="easyui-linkbutton" onclick="closeDialog();">取消</a>
    </div>      
<script>
	function sendCode() { 
	    curCount = 60;  
	    //设置button效果，开始计时  
	    /* $("#callBackCode").attr("disabled", "true");  
	    $("#callBackCode").val(curCount + "秒后重新获取");  */ 
	     //向后台发送处理数据  
	     $.ajax({
				type : "POST",
				url : "${pageContext.request.contextPath}/access/liuchong/sendMessage",
				dataType : "json",
				data : {
					"action" : "回滚"
				},
				success : function(data) {
						$.messager.alert('提示',
								data.message);
					if(data.code == 0){
						InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
					}
				}
		});
	}  
	//timer处理函数  
	function SetRemainTime() {  
	    if (curCount == 0) {                  
	        window.clearInterval(InterValObj);//停止计时器  
	        $("#callBackCode").removeAttr("disabled");//启用按钮  
	        $("#callBackCode").val("重新发送验证码");  
	    }  
	    else {  
	        curCount--;  
	        $("#callBackCode").val(curCount + "秒后重新获取");  
	    }  
	} 
    var choose ;
	var url = "${pageContext.request.contextPath}/access/liuchong/getTypes";
	$.getJSON(url, function(data) {
		typesData = [];
		var message = data.message.toString();
		allType = message.split(",");
		typesData.push({
			"text" : "全选",
			"id" : ""
		});
		for (var i = 0; i < allType.length; i++) {
			typesData.push({
				"text" : allType[i],
				"id" : allType[i]
			});
		}
		$("#QueryTypes").combobox("loadData", typesData);
	});

	function formatOper(val, row, index) {
		return '<a href="#" onclick="openDialog(' + index + ')">误操作回滚</a>';
	}
	function openDialog(index) {
		$("#itemTransList").datagrid('selectRow', index);// 关键在这里  
		var row = $('#itemTransList').datagrid('getSelected');
		$('#callBackRowDate').html(row.created);
		$('#callBackRowType').html(row.type);
		$('#callBackRowNum').html(row.num);
		$('#callBackRowDesc').html(row.reason);
		choose=row.id; 
		$('#ReceiveFeedBackDialog').dialog('open');
	}
	function closeDialog() {
		$('#receiveForm').form('reset');
		$('#ReceiveFeedBackDialog').dialog('close');
	}
	function formSubmit() { 
		var code =$('#receivePw').val();
		$.ajax({
					type : "POST",
					url : "${pageContext.request.contextPath}/access/liuchong/callBackData",
					dataType : "json",
					data : {
						"id" : choose,"code":code
					},
					success : function(data) {
						$.messager.alert('提示', data.message);
						if(data.code == 0){
							closeDialog();
							$("#itemTransList").datagrid('reload');
						}
					}
				}); 
	}
	$('#QueryTypes').combobox({
		filter : function(q, row) {
			var opts = $(this).combobox('options');
			return row[opts.textField].indexOf(q) > -1;
		}
	});
	$(function() {
		itemObj = {
			search : function() {
				$('#itemTransList').datagrid('load', {
					time : $('#timebox').datebox('getValue'),
					type : $("#QueryTypes").combobox('getValue'),
					orderType : $("#orderType").combobox('getValue')
				});
			}
		}
	})
	function getSelectionsIds() {
		var itemList = $("#itemTransList");
		var sels = itemList.datagrid("getSelections");
		var ids = [];
		for ( var i in sels) {
			ids.push(sels[i].id);
		}
		ids = ids.join(",");
		return ids;
	}

	var toolbar = '#tb';
</script>