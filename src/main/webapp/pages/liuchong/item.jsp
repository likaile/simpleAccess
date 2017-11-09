<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="easyui-datagrid" id="itemList" title="商品列表" 
       data-options="singleSelect:false,collapsible:true,pagination:true,striped:true,rownumbers:true,url:'${pageContext.request.contextPath}/access/liuchong/typesCount',method:'get',pageSize:20,toolbar:toolbar">
    <thead>
        <tr>
            <th data-options="field:'type',width:150">商品种类</th>
            <th data-options="field:'count',width:150">实时库存量</th>
            <th data-options="field:'desc',width:300">种类描述</th>
            <th data-options="field:'updated',width:150,align:'center',formatter:TAOTAO.formatDateTime">最后更新时间</th>
        </tr>
    </thead>
</table>
<div id="tbitem" style="padding:5px">  
        <div>  
            查询种类： <input id="typeInfos"  class="easyui-combobox"  type="text" data-options="valueField:'id',textField:'text',width:171" />&nbsp;&nbsp;&nbsp;
            <a href="#" class="easyui-linkbutton"  onclick="obj.search()">查询</a>  
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
$("#typeInfos").combobox("loadData", typesData);
});


$('#typeInfos').combobox({
	filter: function(q, row){
		var opts = $(this).combobox('options');
		return row[opts.textField].indexOf(q) > -1;
	}
});
 $(function () {
    obj = {
        search: function () {
            $('#itemList').datagrid('load', {
                type:$("#typeInfos").combobox('getValue')
            });
        }
    }
})
    function getSelectionsIds(){
    	var itemList = $("#itemList");
    	var sels = itemList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    }
 var toolbar = '#tbitem';
</script>