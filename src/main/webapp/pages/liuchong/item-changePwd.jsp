<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/js/kindeditor-all-min.js"></script>
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
	        <tr>
	        	<td>验证码:</td>
	            <td><input class="easyui-numberbox" type="text" id="changePwCode" data-options="min:0,max:999999,required:true" style="width: 100px;"></input>&nbsp;&nbsp;&nbsp;&nbsp;
	            <input class="easyui-linkbutton" id="changePw" type="button" onclick="changeCode()" value="免费发送验证码 "/></td>
	        </tr>
		      
	    </table>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitChangePwdForm()">提交</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearChangePwdForm()">重置</a>
	</div>
</div>
<script type="text/javascript">
	function changeCode() { 
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
					"action" : "入库"
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
	        $("#changePw").removeAttr("disabled");//启用按钮  
	        $("#changePw").val("重新发送验证码");  
	    }  
	    else {  
	        curCount--;  
	        $("#changePw").val(curCount + "秒后重新获取");  
	    }  
	} 
	//提交表单
	function submitChangePwdForm(){
		
		var oldPassword = $('#oldPassword').val();
		var newPassword = $('#newPassword').val();
		var password2 = $('#newPassword2').val();
		var code = $('#changePwCode').val();
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
	         data:{"oldPassword":oldPassword,"newPassword":newPassword,"code":code}, 
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
