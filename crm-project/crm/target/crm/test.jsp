<%--
  Created by IntelliJ IDEA.
  User: pingyunshangpingyunshang
  Date: 2022/4/2
  Time: 12:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+
            request.getServerPort()+"/"+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>

    <link rel="stylesheet" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css">
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


    <link rel="stylesheet" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <title>Title</title>

    <script type="text/javascript">
        $(function () {
            //当容器加载完成后，对容器调用工具函数
            $("#myDate").datetimepicker({
                language:'zh-CN',             //语言
                format:'yyyy-mm-dd',         //格式
                minView:'month',           //可以选择的最小视图
                initialDate:new Date(),    //初始化选择日期
                autoclose:true,    //设置选择完日期或时间之后，是否自动关闭日历
                todayBtn:true,    //设置是否显示今天按钮 ，默认为false
                clearBtn:true//设置是否显示清空按钮，默认是false
            })
        })
    </script>
</head>
<body>

<input type="text" id="myDate" readonly/>


</body>
</html>
