<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>仓库管理系统</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jquery-easyui-1.4.1/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jquery-easyui-1.4.1/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/taotao.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.4.1/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.4.1/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.4.1/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js"></script>
<style type="text/css">
	.content {
		padding: 10px 10px 10px 10px;
	}
</style>
</head>
<body class="easyui-layout">
    <div data-options="region:'west',title:'菜单',split:true" style="width:180px;">
    	<ul id="menu" class="easyui-tree" style="margin-top: 10px;margin-left: 5px;">
         	<li>
         		<span>仓库物品管理</span>
         		<ul>
	         		<li data-options="attributes:{'url':'${pageContext.request.contextPath}/access/page/item-param-add.html'}">商品种类管理</li>
	         		<li data-options="attributes:{'url':'${pageContext.request.contextPath}/access/page/item.html'}">现有商品清点</li>
	         	</ul>
         	</li>
         	<li>
         		<span>出入库管理</span>
         		<ul>
	         		<li data-options="attributes:{'url':'${pageContext.request.contextPath}/access/page/item-add.html'}">新增入库</li>
	         		<li data-options="attributes:{'url':'${pageContext.request.contextPath}/access/page/item-edit.html'}">新增出库</li>
	         		<li data-options="attributes:{'url':'${pageContext.request.contextPath}/access/page/item-list.html'}">出入库记录查询</li>
	         	</ul>
         	</li>
         	<li>
         		<span>系统设置</span>
         		<ul>
         		<c:choose>
				   <c:when test="${role=='admin'}">  
	         		<li data-options="attributes:{'url':'${pageContext.request.contextPath}/access/page/item-user.html'}">新增用户</li>
<%-- 	         		<li data-options="attributes:{'url':'${pageContext.request.contextPath}/access/page/user-list.html'}">用户管理</li>
 --%>				   </c:when>
				</c:choose>
	         		<li data-options="attributes:{'url':'${pageContext.request.contextPath}/access/page/item-changePwd.html'}">修改密码</li>
	         	</ul>
         	</li>
         	<li>
         		<span>退出登录</span>
         		<ul>
	         		<li data-options="attributes:{'url':'${pageContext.request.contextPath}/access/page/loginOut.html'}">退出登录</li>
	         	</ul>
         	</li>
         </ul>
    </div>
    <div data-options="region:'center',title:''">
    	<div id="tabs" class="easyui-tabs">
		    <div title="首页" style="padding:20px;">
		    
		   	 您好 ,<span style="color:red;">  ${username}</span><br/><br/>
	                         现在是北京时间 <span style="white-space:pre;color:red;" id="localtime" ></span>
		     
			 <br/><br/>当前的地址为：<span id = "province"  style="color:#F00"></span> 省, <span id = "city"  style="color:#F00"></span> 市, IP：<span id = "ipAddress"  style="color:#F00"></span>
		     <br/>本页面仅作个人研究用，如有问题请点击
			<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=417357422&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:417357422:51" alt="点击同我联系" title="点击同我联系"/></a>		   
			</div>
		</div>
    </div>
<script>(function(T,h,i,n,k,P,a,g,e){g=function(){P=h.createElement(i);a=h.getElementsByTagName(i)[0];P.src=k;P.charset="utf-8";P.async=1;a.parentNode.insertBefore(P,a)};T["ThinkPageWeatherWidgetObject"]=n;T[n]||(T[n]=function(){(T[n].q=T[n].q||[]).push(arguments)});T[n].l=+new Date();if(T.attachEvent){T.attachEvent("onload",g)}else{T.addEventListener("load",g,false)}}(window,document,"script","tpwidget","//widget.seniverse.com/widget/chameleon.js"))</script>
<script>
tpwidget("init", {
    "flavor": "bubble",
    "location": "WX4D4S7291H4",
    "geolocation": "disabled",
    "position": "top-right",
    "margin": "10px 10px",
    "language": "zh-chs",
    "unit": "c",
    "theme": "chameleon",
    "uid": "UB41BA6CC1",
    "hash": "51c3f31903ef692848b66c0a881f8bae"
});
tpwidget("show");</script>
<!-- 下面两个js为获取城市和IP组件 -->
<script src="http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js"type="text/ecmascript"></script>
<script src="http://pv.sohu.com/cityjson?ie=utf-8"></script>  
<script type="text/javascript">
setInterval("document.getElementById('localtime').innerHTML=new Date().toLocaleString()+' 星期'+'日一二三四五六'.charAt(new Date().getDay());",1000);  
$("#province").html(remote_ip_info["province"]);
$("#city").html(remote_ip_info["city"]);
$("#ipAddress").html(returnCitySN["cip"]);
$(function(){
	$('#menu').tree({
		onClick: function(node){
			if($('#menu').tree("isLeaf",node.target)){
				var tabs = $("#tabs");
				var tab = tabs.tabs("getTab",node.text);
				if(tab){
					tabs.tabs("select",node.text);
				}else{
					tabs.tabs('add',{
					    title:node.text,
					    href: node.attributes.url,
					    closable:true,
					    bodyCls:"content"
					});
				}
			}
		}
	});
});
</script>
</body>
</html>