<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<style> 
body{ text-align:center;
  /* background-image: url(http://bjhcyd.com/access/girl.html);
   background-repeat:no-repeat;background-position: center; */
  } 
.div{ margin:0 auto; width:400px; height:300px; border:1px solid #F00} 
p{
    font-weight:blod;
}
/* css注释：为了观察效果设置宽度 边框 高度等样式 */ 
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript">
function login(){
	 $.ajax({
         type: "POST",
         url:"${pageContext.request.contextPath}/access/liuchong/toLogin",
         dataType: "json", 
         data:{"username":$('#username').val(),"password":$('#password').val()}, 
         success: function(data){
        	if(data.code == 1){
        		window.location.href="${pageContext.request.contextPath}/access/liuchong.html"; 
        	}else{
        		$("#error").html(data.message);
        	}
         }
    });   
}
$(document).keyup(function(event){
	 if(event.keyCode ==13){
		 login();
	 }
	});


</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>仓库管理系统</title>
</head>

<body>

	<div class="div">
		<h1>仓库管理系统</h1>
		<input type="text" id="username" placeholder="liuchong" value="liuchong"><br />
		<br /> <input type="password"  id="password" ><br />
		<br /><br />
		<button onClick="login();">点我登录</button>
		<div>
			<p id="error" ></p>
		</div>
	</div>


</body>

</html>