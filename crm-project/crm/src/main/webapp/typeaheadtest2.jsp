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
                source:function (jquery, process) {
                    //每次键盘弹起，都自动触发本函数；我们可以向后台送请求，查询客户表中所有的名称，把客户名称以[]字符串形式返回前台，赋值给source
                    //process：是个函数，能够将['xxx','xxxxx','xxxxxx',.....]字符串赋值给source，从而完成自动补全
                    //jquery：在容器中输入的关键字
                    $.ajax({
                        url:'workbench/transaction/queryAllCustomerName.do',
                        data:{
                            customerName:jquery
                        },
                        type: 'post',
                        dataType: 'json',
                        success: function(data){
                                process(data);
                        }
                    });
                }
            });
        });
    </script>
</head>
<body>
<input type="text" id="username">


</body>
</html>
