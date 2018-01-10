<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript" charset="utf-8"
	src="${pageContext.request.contextPath}/js/kindeditor-all-min.js"></script>

<div style="padding: 10px 10px 10px 10px">
	<form id="itemAddForm" class="itemForm" method="post">
		<table cellpadding="5">
			<tr>
				<td>入库库商品种类:</td>
				<td>
				<input id="addTypes" class="easyui-combobox"  type="text" data-options="valueField:'id',textField:'text',width:171" />&nbsp;&nbsp;&nbsp;实时库存数量：<span id ="count">0</span>
				</td>
			</tr>
			<tr>
				<td>入库商品数量:</td>
				<td><input class="easyui-numberbox" type="text" id="addNum"
					data-options="min:1,max:99999999,precision:0,required:true" /></td>
			</tr>
			<tr>
				<td>入库时间:</td>
				<td><input id="addTime" type="text" name="addTime"
					class="easyui-datebox"></td>
			</tr>
			<tr>
				<td>商品描述:</td>
				<td><textarea
						style="width: 300px; height: 100px; visibility: hidden;" id="adDesc"
						class="easyui-textbox"></textarea></td>
			</tr>
		</table>
	</form>
	<div style="padding: 5px">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			onclick="submitAddForm()">提交</a> <a href="javascript:void(0)"
			class="easyui-linkbutton" onclick="clearAddForm()">重置</a>
	</div>
</div>
<script type="text/javascript">
	var url = "${pageContext.request.contextPath}/access/liuchong/getTypes";
	$.getJSON(url, function(data) {
		typesData = [];
		var message =data.message.toString();
		allType = message.split(",");
		for (var i=0;i<allType.length;i++)
		{	
			typesData.push({ "text":allType[i], "id": allType[i]});
		}
		$("#addTypes").combobox("loadData", typesData);
	}); 
 

	//提交表单
	function submitAddForm() {
		//有效性验证
		var type = $("#addTypes").combobox('getValue');
		var num = $('#addNum').val();
		var desc = $('#adDesc').val();
		var time = $('#addTime').datebox('getValue');
		
		if( type=="" || num=="" ){
			$.messager.alert('提示','请提交完整数据!');
			return ;
		}
		 $.ajax({
	         type: "POST",
	         url:"liuchong/saveItem",
	         dataType: "json", 
	         data:{"type":type,"desc":desc,num:num,time:time}, 
	         success: function(data){
	        	$.messager.alert('提示',data.message);
	        	if(data.code == 1){
	        		$('#itemAddForm').form('reset');
	        	}
	         }
	    });   
	}
	function clearAddForm() {
		$('#itemAddForm').form('reset');
	}
	$('#addTypes').combobox({
		onChange:function(newValue,oldValue){
			$.ajax({
		         type: "POST",
		         url:"${pageContext.request.contextPath}/access/liuchong/getCountByType",
		         dataType: "json", 
		         data:{"type":newValue}, 
		         success: function(data){
		        	 $("#count").html(data.message);
		         }
		    });
	    },  
		filter: function(q, row){
			var opts = $(this).combobox('options');
			return row[opts.textField].indexOf(q) > -1;
		}
	});
</script>
