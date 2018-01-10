<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="${pageContext.request.contextPath}/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div style="padding:10px 10px 10px 10px">
	<form id="itemAddUserForm" class="itemForm" method="post">
	    <table cellpadding="5">
	        <tr>
	            <td>姓名:</td>
	            <td><input class="easyui-combobox" type="text" id="userlist" data-options="required:true" style="width: 171px;"/><span style="color:red"> * 姓名为系统出入库记录用</span></td>
	        </tr>
		        <tr>
		            <td>入库权限:</td>
		            <td>
		                <input id="Add"  class="easyui-combobox"  type="text" data-options="valueField:'id',textField:'text',width:171" />
		            </td>
		        </tr>
		        <tr>
		            <td>出库权限:</td>
		            <td>
		                <input id="Edit"  class="easyui-combobox"  type="text" data-options="valueField:'id',textField:'text',width:171" />
		            </td>
		        </tr>
		        <tr>
		            <td>新增商品种类权限:</td>
		            <td>
		                <input id="Save"  class="easyui-combobox"  type="text" data-options="valueField:'id',textField:'text',width:171" />
		            </td>
		        </tr>
		        <tr>
		            <td>删除商品种类权限:</td>
		            <td>
		                <input id="delete"  class="easyui-combobox"  type="text" data-options="valueField:'id',textField:'text',width:171" />
		            </td>
		        </tr>
		        <tr>
		            <td>误操作回滚权限:</td>
		            <td>
		                <input id="CallBack"  class="easyui-combobox"  type="text" data-options="valueField:'id',textField:'text',width:171" />
		            </td>
		        </tr>
	    </table>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitAddUserForm()">提交</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearAddUserForm()">重置</a>
	</div>
</div>
<script type="text/javascript">
	var url = "${pageContext.request.contextPath}/access/liuchong/getUsers";
	$.getJSON(url, function(data) {
         var obj = eval(data);typesData = [];
         $(obj).each(function (index) {
             var val = obj[index];
             typesData.push({ "text":val.username, "id": val.mobile});
         });
		$("#userlist").combobox("loadData", typesData);
	}); 
	$('#userlist').combobox({
		onChange:function(newValue,oldValue){
			$.ajax({
		         type: "POST",
		         url:"${pageContext.request.contextPath}/access/liuchong/getUserStatus",
		         dataType: "json", 
		         data:{"type":newValue}, 
		         success: function(data){
		        	 if(indexOf("入库") != -1){
		        	 	$('#cc').combobox('setValue', '001');
		        	 }else{
		        		 
		        	 }
		         }
		    });
	    },  
		filter: function(q, row){
			var opts = $(this).combobox('options');
			return row[opts.textField].indexOf(q) > -1;
		}
	});
	//提交表单
	function submitAddUserForm(){
		var username = $('#username').val();
		var mobile = $('#mobile').val();
		var password = $('#password').val();
		var isAdd = $('#isAdd').combobox('getValue');
		var isEdit = $('#isEdit').combobox('getValue');
		var isSave = $('#isSave').combobox('getValue');
		var isCallBack = $('#isCallBack').combobox('getValue');
		//有效性验证
		if( username==""||password==""){
			$.messager.alert('提示','请提交完整数据!');
			return ;
		}
		
		 $.ajax({
	         type: "POST",
	         url:"${pageContext.request.contextPath}/access/liuchong/saveUser",
	         dataType: "json", 
	         data:{"mobile":mobile,"username":username,"password":password,"isAdd":isAdd,"isEdit":isEdit,"isSave":isSave,"isCallBack":isCallBack}, 
	         success: function(data){
	        	if(data.code == 1){
	        		alert("新增用户成功");
	        		$('#itemAddUserForm').form('reset');
	        	}else{
	        		alert(data.message);
	        	//	$('#itemAddUserForm').form('reset');
	        	}
	         }
	    });   
	}
	
	function clearAddUserForm(){
		$('#itemAddUserForm').form('reset');
	}
</script>
