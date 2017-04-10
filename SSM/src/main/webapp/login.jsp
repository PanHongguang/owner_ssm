<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login</title>
    
    <script src="${ctx }/js/jquery-1.11.1.min.js"></script>

    <!-- CSS -->
    <link rel="stylesheet" href="${ctx }/css/reset.css">
    <link rel="stylesheet" href="${ctx }/css/supersized.css">
    <link rel="stylesheet" href="${ctx }/css/style.css">

</head>

<body oncontextmenu="return false">

<div class="page-container">
    <input type="hidden" id="error" value="${error}"/>
    <h1>Login</h1>
    <form action="${ctx }/shiro/login" method="post">
        <div>
            <input type="text" name="userName" class="username" placeholder="Username" autocomplete="off"/>
        </div>
        <div>
            <input type="password" name="password" class="password" placeholder="Password" oncontextmenu="return false"
                   onpaste="return false" />
        </div>
        <button id="submit" type="submit">Sign in</button>
    </form>
    <div class="connect">
        <p>You never know what you can do till you try.</p>
        <p style="margin-top:20px;">除非你亲自尝试一下,否则你永远不知道你能做什么。</p>
    </div>
</div>
<div class="alert" style="display:none">
    <h2>消息</h2>
    <div class="alert_con">
        <p id="ts"></p>
        <p style="line-height:70px"><a class="btn">确定</a></p>
    </div>
</div>

<!-- Javascript -->
<script src="${ctx }/js/supersized.3.2.7.min.js"></script>
<script src="${ctx }/js/supersized-init.js"></script>
<script>
    $(".btn").click(function(){
        is_hide();
    })
    var u = $("input[name=username]");
    var p = $("input[name=password]");
    $("#submit").live('click',function(){
        if(u.val() == '' || p.val() =='')
        {
            $("#ts").html("用户名或密码不能为空~");
            is_show();
            return false;
        }
        else{
            var reg = /^[0-9A-Za-z]+$/;
            if(!reg.exec(u.val()))
            {
                $("#ts").html("用户名错误");
                is_show();
                return false;
            }
        }
    });
    window.onload = function()
    {
        var error = $("#error").val() ;
        if(error != ""){
            $("#ts").html("用户名或密码错误！");
            is_show();
        }
        $(".connect p").eq(0).animate({"left":"0%"}, 600);
        $(".connect p").eq(1).animate({"left":"0%"}, 400);
    }
    function is_hide(){ $(".alert").animate({"top":"-40%"}, 300) }
    function is_show(){ $(".alert").show().animate({"top":"45%"}, 300) }
</script>
</body>

</html>
