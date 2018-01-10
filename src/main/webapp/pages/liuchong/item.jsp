<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="easyui-datagrid" id="itemList" title="商品列表" 
       data-options="singleSelect:false,collapsible:true,pagination:true,striped:true,rownumbers:true,url:'${pageContext.request.contextPath}/access/liuchong/typesCount',method:'get',pageSize:20,toolbar:toolbar">
    <thead>
        <tr>
        	<th data-options="field:'id',hidden:'true'">ID</th>
            <th data-options="field:'type',width:150">商品种类</th>
            <th data-options="field:'count',width:150">实时库存量</th>
            <th data-options="field:'desc',width:300">种类描述</th>
            <th data-options="field:'updated',width:150,align:'center',formatter:TAOTAO.formatDateTime">最后更新时间</th>
            <th data-options="field:'action',width:150,align:'center',formatter:ActionOper">操作</th>  
        </tr>
    </thead>
</table>
<div id="tbitem" style="padding:5px">  
        <div>  
            查询种类： <input id="typeInfos"  class="easyui-combobox"  type="text" data-options="valueField:'id',textField:'text',width:171" />&nbsp;&nbsp;&nbsp;
            <a href="#" class="easyui-linkbutton"  onclick="obj.search()">查询</a>  
        </div>  
    </div>
    <div id="typeDialog" class="easyui-dialog" closed="true"  align="center" buttons="#dlg-typebuttons" title="警示" style="width:auto;height:auto;">
	<form id="typeForm" class="receiveForm" method="post">
	    <table cellpadding="5">
	        <tr>
	            <td>选中商品:</td>
	            <td><span id ="selectType" style="color:red"></span></td>
	        </tr>
	        <tr>
	            <td>现有库存:</td>
	            <td><span id ="selectNum" style="color:red"></span></td>
	        </tr>
	        <tr>
	            <td>描述:</td>
	            <td><span id ="selectDesc" style="color:red"></span></td>
	        </tr>
	        <tr>
	            <td><input class="easyui-numberbox" type="text" id="typePw" data-options="min:0,max:999999,required:true" style="width: 80px;"></input></td>
	            <td><input  id="btnSendCode" class="easyui-linkbutton" type="button" onclick="sendMessage()" value="免费发送验证码  "/></td>
	        </tr>
	    </table>
	</form>
</div> 
    <div id="dlg-typebuttons">
        <a href="#" class="easyui-linkbutton" onclick="deleteData();" id="receiveYes">确认</a> <a href="#"
            class="easyui-linkbutton" onclick="closeTypeDialog();">取消</a>
    </div>

<script>
	function sendMessage() { 
	    curCount = 60;  
        /* //设置button效果，开始计时  
        $("#btnSendCode").attr("disabled", "true");  
        $("#btnSendCode").val(curCount + "秒后重新获取");   */
	     //向后台发送处理数据 
	     $("#itemList").datagrid('selectRow', typeId);// 关键在这里  
		var row = $('#itemList').datagrid('getSelected');
		var code =$('#typePw').val();

		if (row) {
			if (row.count > 0) {
				$.messager.alert('警告', row.type + " 的库存量还有 " + row.count
						+ ",请先处理剩余库存！");
				return;
			}
		}
	     $.ajax({
				type : "POST",
				url : "${pageContext.request.contextPath}/access/liuchong/sendMessage",
				dataType : "json",
				data : {
					"action" : "删除"
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
	        $("#btnSendCode").removeAttr("disabled");//启用按钮  
	        $("#btnSendCode").val("重新发送验证码");  
	    }  
	    else {  
	        curCount--;  
	        $("#btnSendCode").val(curCount + "秒后重新获取");  
	    }  
	}  

    var typeId;
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
		$("#typeInfos").combobox("loadData", typesData);
	});

	function ActionOper(val, row, index) {
		return '<a href="#" onclick="openTypeDialog(' + index
				+ ')">删除该物品种类</a>';
	}
	function openTypeDialog(index) {
		$("#itemList").datagrid('selectRow', index);// 关键在这里  
		var row = $('#itemList').datagrid('getSelected');
		$('#selectType').html(row.type);
		$('#selectNum').html(row.count);
		$('#selectDesc').html(row.desc);typeId=index;
		$('#typeDialog').dialog('open');
	}
	function closeTypeDialog() {
		$('#typeForm').form('reset');
		$('#typeDialog').dialog('close');
	}
	

	function deleteData() {
		$("#itemList").datagrid('selectRow', typeId);// 关键在这里  
		var row = $('#itemList').datagrid('getSelected');
		var code =$('#typePw').val();

		if (row) {
			if (row.count > 0) {
				$.messager.alert('警告', row.type + " 的库存量还有 " + row.count
						+ ",请先处理剩余库存！");
				return;
			}
			$.messager
					.confirm(
							'警告',
							"该操作将删除 " + row.type + " 的所有记录,且不可恢复，请二次确认！",
							function(r) {
								if (r) {
									$
											.ajax({
												type : "POST",
												url : "${pageContext.request.contextPath}/access/liuchong/deleteData",
												dataType : "json",
												data : {
													"type" : row.type,"code":code
												},
												success : function(data) {
													$.messager.alert('提示',
															data.message);
													if(data.code == 0){
														closeTypeDialog();
														$("#itemList").datagrid('reload');
													}
												}
											});
								}
							});
		}
	}
	$('#typeInfos').combobox({
		filter : function(q, row) {
			var opts = $(this).combobox('options');
			return row[opts.textField].indexOf(q) > -1;
		}
	});
	$(function() {
		obj = {
			search : function() {
				$('#itemList').datagrid('load', {
					type : $("#typeInfos").combobox('getValue')
				});
			}
		}
	})
	function getSelectionsIds() {
		var itemList = $("#itemList");
		var sels = itemList.datagrid("getSelections");
		var ids = [];
		for ( var i in sels) {
			ids.push(sels[i].id);
		}
		ids = ids.join(",");
		return ids;
	}
	var toolbar = '#tbitem';
</script>