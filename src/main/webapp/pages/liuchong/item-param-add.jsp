<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="${pageContext.request.contextPath}/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div style="padding:10px 10px 10px 10px">
	<form id="itemParamAddForm" class="itemForm" method="post">
	    <table cellpadding="5">
	        <tr>
	            <td>新增商品种类:</td>
	            <td><input class="easyui-textbox" type="text" id="type" data-options="required:true" style="width: 150px;"></input></td>
	        </tr>
	        <tr>
	            <td>种类描述:</td>
	            <td>
	                <textarea style="width:500px;height:150px;visibility:hidden;" class="easyui-textbox" id="desc"></textarea>
	            </td>
	        </tr>
	    </table>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">重置</a>
	</div>
</div>
<script type="text/javascript">
	//提交表单
	function submitForm(){
		var type = $('#type').val();
		var desc = $('#desc').val();
		//有效性验证
		if( type==""){
			$.messager.alert('提示','请提交完整数据!');
			return ;
		}
		
		 $.ajax({
	         type: "POST",
	         url:"${pageContext.request.contextPath}/access/liuchong/saveType",
	         dataType: "json", 
	         data:{"type":type,"desc":desc}, 
	         success: function(data){
	        	if(data.code == 1){
	        		$.messager.alert('提示','新增商品种类成功');
	        		$('#itemParamAddForm').form('reset');
	        	}else{
	        		$.messager.alert('提示',data.message);
	        		$('#itemParamAddForm').form('reset');
	        	}
	         }
	    });   
	}
	
	function clearForm(){
		$('#itemParamAddForm').form('reset');
	}
</script>
