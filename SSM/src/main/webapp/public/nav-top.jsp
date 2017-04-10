<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
function refresh(){
    $.ajax({
        type:'POST',
        url: '${ctx}/createLuceneIndex',
        success:function(data){
            if(data.result ==1){
                alert("生成索引成功！");
            }else {
                alert(data.msg) ;
            }
        }
    });
}
</script>

<nav class="navbar navbar-default navbar-inverse navbar-fixed-top">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed"
                    data-toggle="collapse" data-target="#demo-navbar"
                    aria-expanded="false">
                <span class="sr-only">Toggle navigation</span> <span
                    class="icon-bar"></span> <span class="icon-bar"></span> <span
                    class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${ctx}/index"></a>
        </div>

        <div class="collapse navbar-collapse" id="demo-navbar">
            <ul class="nav navbar-nav">
                <li class="active"><a title="COJ" href="${ctx}/index">
                    <span class="glyphicon glyphicon-th" aria-hidden="true"></span>
                    <span class="sr-only">(current)</span>
                </a></li>
                <li><a href="#" data-toggle="modal" data-target="#about">关于</a></li>
            </ul>
            <form class="navbar-form navbar-left" action="${ctx}/luceneQuery" method="post" role="search">
                <div class="form-group">
                    <input type="text" name="q" class="form-control" placeholder="Search">
                </div>
                <button type="submit" class="btn btn-default">查询</button>
                <button type="button" class="btn btn-danger" onclick="refresh();">重新生成索引</button>
            </form>

            <ul class="nav navbar-nav navbar-right">
                <c:choose>
                    <c:when test="${empty user}">
                        <li><a href="javascriot:void(0)" data-toggle="modal" data-target="#login">登录
                            <span class="glyphicon glyphicon-home"></span></a></li>
                        <li><a href="javascriot:void(0)" data-toggle="modal" data-target="#register">注册
                            <span class="glyphicon glyphicon-user"></span></a></li>
                        <li><a href="${ctx}/login.jsp" >后台
                            <span class="glyphicon glyphicon-lock"></span></a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle"
                               data-toggle="dropdown" role="button" aria-haspopup="true"
                               aria-expanded="false">
                                <img id="sm-name" src="
									<c:choose>
										<c:when test="${empty headimg}">
											${ctx}/include/img/person.gif
										</c:when>
										<c:otherwise>
											${ctx}/${headimg}
										</c:otherwise>
									</c:choose>
									" class="img-responsive center-block img-circle" alt="图片无效">
                            </a>
                            <ul class="dropdown-menu">
                                <li>
                                    <a href="${ctx}/user/frontUserSet/${user.id}">${user.username }<span
                                            class="glyphicon glyphicon-cog"></span></a>
                                    <a href="${ctx}/login/loginOut?type=1">退出登录<span
                                            class="glyphicon glyphicon-off"></span></a>
                                </li>
                            </ul>
                        </li>
                    </c:otherwise>
                </c:choose>

            </ul>

        </div>
    </div>
</nav>