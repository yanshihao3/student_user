<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>后台登录</title>
    <!-- for-mobile-apps -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script type="text/javascript" src="/js/js_utils.js"></script>
    <!-- //for-mobile-apps -->
    <link href="/css/style.css" rel="stylesheet" type="text/css" media="all"/>
</head>
<body>
<!-- main -->
<div class="main">
    <h1>
        <c:if test="${type=='student'}">
            成绩查询
        </c:if>
        <c:if test="${type=='teacher'}">
            工资查询
        </c:if>
    </h1>
    <form name="login_form">
        <input type="text" value="请输入用户名" id="username" name="username" onFocus="this.value = '';"
               onBlur="if (this.value == '') {this.value = '请输入用户名';}"
               required="">
        <input type="password" id="password" name="password" placeholder="请输入密码" onFocus="this.placeholder = ''; "
               onBlur="if (this.value == '') {this.placeholder = '请输入密码';}"
               required="">
        <input id="submit" value="登录" type="button" onclick="beforeSubmit()">
    </form>
    <div style="margin: 25px 0;">
        <c:if test="${type=='student'}">
            <a style="font-size: 1.2rem;float: left;color: #a5a5a5;" href="/register.html?type=${type}">
                注册
            </a>
            <a style="font-size: 1.2rem;float: right;color: #a5a5a5;" href="/forget.html?type=${type}">
                修改密码?
            </a>
        </c:if>
        <c:if test="${type=='teacher'}">
            <a style="font-size: 1.2rem;float: right;color: #a5a5a5;" href="/forget.html?type=${type}">
                修改密码?
            </a>
        </c:if>

    </div>
</div>

<!-- //main -->
</body>
<script src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>
<script>

    //在页面未加载完毕之前显示的loading Html自定义内容
    var _LoadingHtml = '<div id="loadingDiv" style="display: none; "><div id="over" style=" position: absolute;top: 0;left: 0; width: 100%;height: 100%; background-color: #f5f5f5;opacity:0.5;z-index: 1000;"></div><div id="layout" style="position: absolute;top: 40%; left: 40%;width: 20%; height: 20%;  z-index: 1001;text-align:center;"><img style="width: 36px;height: 36px" src="/css/loading.gif" /></div></div>';
    //呈现loading效果
    document.write(_LoadingHtml);

    //移除loading效果
    function completeLoading() {
        document.getElementById("loadingDiv").style.display="none";
    }
    //展示loading效果
    function showLoading() {
        document.getElementById("loadingDiv").style.display = "block";
    }


    function beforeSubmit() {
        var username = document.getElementById('username').value;
        var password = document.getElementById('password').value;
        // 1.创建一个FormData对象，直接把我们的表单传进去
        var formData = new FormData(document.forms.namedItem("login_form"));
        formData.append("type", '${type}');
        // 2.通过jquery发送出去
        showLoading()
        $.ajax({
            url: "/login/login",
            type: "POST",
            data: formData,
            processData: false,  // 告诉jQuery不要去处理发送的数据
            contentType: false   // 告诉jQuery不要去设置Content-Type请求头
        }).done(function (date) {
            completeLoading();
            if (date.success) {
                if ('${type}' == 'teacher') {
                    window.location.href = "/teacher.html?username=" + username;
                } else {
                    window.location.href = "/show?username=" + username;
                }
            } else {
                alert(date.msg)
            }
        }).fail(function (date) {
            MaskUtil.unmask();
            alert('fail!')
        });
    }

</script>
</html>