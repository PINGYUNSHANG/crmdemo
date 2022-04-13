<%--
  Created by IntelliJ IDEA.
  User: pingyunshangpingyunshang
  Date: 2022/4/5
  Time: 9:15 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page session="true" contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" +
            request.getServerPort() + "/" + request.getContextPath() + "/";
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <base href="<%=basePath%>"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <title>Title</title>
    <script type="text/javascript">
        $(function () {
            //给下载按钮添加单击事件
            $("#fileDownloadBtn").click(function () {
                alert("aaaaa");
                //发送文件下载请求 ，同步请求
                window.location.href="workbench/activity/fileDownload.do";

            })
        })
    </script>

</head>
<body>
<input type="button" value="下载" id="fileDownloadBtn">


</body>
</html>
