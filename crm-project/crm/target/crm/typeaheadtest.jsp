<%--
  Created by IntelliJ IDEA.
  User: pingyunshangpingyunshang
  Date: 2022/4/8
  Time: 8:11 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" +
            request.getServerPort() + "/" + request.getContextPath() + "/";
%>

<html>
<head>
    <base href="<%=basePath%>"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>

    <link rel="stylesheet" type="text/css" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css">
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>

    <meta charset="UTF-8">

    <script type="text/javascript" >
        $(function () {
            $("#username").typeahead({
                source:['京东','阿里巴巴','腾讯','字节跳动','百度']
            });
        });
    </script>
</head>
<body>
<input type="text" id="username">


</body>
</html>
