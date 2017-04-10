<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>没权限</title>

    <!-- CSS -->
    <link href="${ctx }/css/nopower.css" rel="stylesheet" type="text/css" />


</head>

<body>
<div class="tail-left"></div>
<div id="main">
    <!-- header -->
    <div id="header">
        <h1>Nothing alive here!<span><strong>NO Power</strong> error - not found</span></h1>
    </div>
    <!-- content -->
    <div id="content">
        <ul class="nav">
            <li class="home"><a href="${ctx }/index">Home Page</a></li>
            <li class="site_map"><a href="#">Site Map</a></li>
            <li class="search"><a href="#">Website Search</a></li>
        </ul>
        <p>对不起，你没有权限访问此网页<br /> </p>
    </div>
    <!-- footer -->
    <div id="footer">
    </div>
</div>

</body>

</html>
