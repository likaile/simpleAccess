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
<script>
var url = "${pageContext.request.contextPath}/access/liuchong/getTypes";
$.getJSON(url, function(data) {
typesData = [];
var message =data.message.toString();
allType = message.split(",");
typesData.push({ "text":"全选", "id": ""});
for (var i=0;i<allType.length;i++)
{	
	typesData.push({ "text":allType[i], "id": allType[i]});
}
$("#QueryTypes").combobox("loadData", typesData);
});

function formatOper(val,row,index){
	if(index == 0){
	}
    	return '<a href="#" onclick="callBackData('+index+')">误操作回滚</a>';  
}
function callBackData(index){  
    $("#itemTransList").datagrid('selectRow',index);// 关键在这里  
    var row = $('#itemTransList').datagrid('getSelected');  
    if (row){  
       var r=confirm("误操作回滚将该恢复该出入库操作之前的数据,且不可恢复，是否确认！")
       if (r==true)
       {
    	   $.ajax({
    	         type: "POST",
    	         url:"${pageContext.request.contextPath}/access/liuchong/callBackData",
    	         dataType: "json", 
    	         data:{"id":row.id}, 
    	         success: function(data){
    	        	alert(data.message);
    	        	 $("#itemTransList").datagrid('reload');  
    	         }
    	    });   
       }
    }  
}  
$('#QueryTypes').combobox({
	filter: function(q, row){
		var opts = $(this).combobox('options');
		return row[opts.textField].indexOf(q) > -1;
	}
});
 $(function () {
	 itemObj = {
        search: function () {
            $('#itemTransList').datagrid('load', {
                time:$('#timebox').datebox('getValue'),
                type:$("#QueryTypes").combobox('getValue'),
                orderType:$("#orderType").combobox('getValue')
            });
        }
    }
})
     function getSelectionsIds(){
    	var itemList = $("#itemTransList");
    	var sels = itemList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    } 
    
    var toolbar = '#tb';
</script>