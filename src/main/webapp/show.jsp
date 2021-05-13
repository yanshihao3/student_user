<%--
  Created by IntelliJ IDEA.
  User: yanshihao
  Date: 2019/11/9
  Time: 11:28 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link rel="stylesheet" type="text/css" href="../../themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../themes/icon.css">
<head>
    <title>成绩</title>
</head>
<body>
<table id="table_grid"></table>
</body>
<script src="/jquery.min.js"></script>
<script src="/js/jquery-3.2.1.min.js"></script>
<script src="/jquery.easyui.min.js"></script>

<script>

    $(function () {

        $('#table_grid').datagrid({
            url: '/show/selectInfo',
            fitColumns: true,
            queryParams: {
                username: '${username}'
            },
            columns: [[
                {field: 'grade', title: '级部', width: 100},
                {field: 'xtype', title: '类别', width: 100},
                {field: 'class', title: '班级', width: 100},
                {field: 'number', title: '考号', width: 100},
                {field: 'name', title: '姓名', width: 100},
                {field: 'total_score', title: '总分', width: 100},
                {field: 'language', title: '语文', width: 100},
                {field: 'mathematics', title: '数学', width: 100},
                {field: 'english', title: '英语', width: 100},
                {field: 'physics', title: '物理', width: 100},
                {field: 'chemistry', title: '化学', width: 100},
                {field: 'biology', title: '生物', width: 100},
                {field: 'politics', title: '政治', width: 100},
                {field: 'history', title: '历史', width: 100},
                {field: 'geography', title: '地理', width: 100},
                {field: 'synthesize', title: '综合', width: 100},
                {field: 'xorder', title: '全体排名', width: 100}
            ]],
            onLoadSuccess: function (data) {
                JSON.stringify(data)
                if (data.total == 0) {
                    alert("当前用户无成绩")
                } else {
                    if (data.rows[0].physics == null || data.rows[0].physics === '') {
                        $("#table_grid").datagrid("hideColumn", "physics"); // 设置隐藏列
                    }
                    if (data.rows[0].chemistry == null || data.rows[0].chemistry === '') {
                        $("#table_grid").datagrid("hideColumn", "chemistry"); // 设置隐藏列
                    }
                    if (data.rows[0].biology == null || data.rows[0].biology === '') {
                        $("#table_grid").datagrid("hideColumn", "biology"); // 设置隐藏列
                    }
                    if (data.rows[0].politics == null || data.rows[0].politics === '') {
                        $("#table_grid").datagrid("hideColumn", "politics"); // 设置隐藏列
                    }
                    if (data.rows[0].history == null || data.rows[0].history === '') {
                        $("#table_grid").datagrid("hideColumn", "history"); // 设置隐藏列
                    }
                    if (data.rows[0].geography == null || data.rows[0].geography === '') {
                        $("#table_grid").datagrid("hideColumn", "geography"); // 设置隐藏列
                    }
                    if (data.rows[0].synthesize == null || data.rows[0].synthesize === '') {
                        $("#table_grid").datagrid("hideColumn", "synthesize"); // 设置隐藏列
                    }
                }
            }
        });
    })

</script>
</html>
