<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="${pageContext.request.contextPath}/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div style="padding:10px 10px 10px 10px">
	<form id="ItmeChangePwdForm" class="itemForm" method="post">
	    <table cellpadding="5">
	    	<tr>
	            <td>当前手机号:</td>
	            <td><span style="color:red" > ${mobile}</span></td>
	        </tr>
	        
	        <tr>
	            <td>旧密码:</td>
	            <td><input id="oldPassword"  validType="length[6,20]" class="easyui-validatebox" required="true" type="password" value=""/></td>
	        </tr>
	      	<tr>
	            <td>新密码:</td>
	            <td><input id="newPassword"  validType="length[6,20]" class="easyui-validatebox" required="true" type="password" value=""/></td>
	        </tr>
	        <tr>
	            <td>再次输入密码:</td>
	            <td><input id="newPassword2"  validType="length[6,20]" class="easyui-validatebox" required="true" type="password" value=""/></td>
	        </tr>
		      
	    </table>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitChangePwdForm()">提交</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearChangePwdForm()">重置</a>
	</div>
</div>
<script type="text/javascript">
	
	//提交表单
	function submitChangePwdForm(){
		
		var oldPassword = $('#oldPassword').val();
		var newPassword = $('#newPassword').val();
		var password2 = $('#newPassword2').val();
		if(newPassword != password2 ){
			$.messager.alert('警告',"两次输入新密码不一致！");return;
		}
		//有效性验证
		if( oldPassword==""||newPassword==""){
			$.messager.alert('警告','请提交完整数据!');
			return ;
		}
		
		 $.ajax({
	         type: "POST",
	         url:"${pageContext.request.contextPath}/access/liuchong/changePwd",
	         dataType: "json", 
	         data:{"oldPassword":oldPassword,"newPassword":newPassword}, 
	         success: function(data){
	        	if(data.code == 1){
	        		alert("密码修改成功！请刷新页面,重新登录！");
	        		window.location.href="about:blank";
	        		window.close();
	        	}else{
	        		alert(data.message);
	        	//	$('#ItmeChangePwdForm').form('reset');
	        	}
	         }
	    });   
	}
	
	function clearChangePwdForm(){
		$('#ItmeChangePwdForm').form('reset');
	}
</script>
