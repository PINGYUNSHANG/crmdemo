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
    <title>Title</title>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>

</head>
<body>
<%--文件上传表单必须满足三个条件
    表单组件标签:
    <input type="text|password|radio|checkbox|hidden|button|submit|reset|file">
    <select>,<textarea> 等
    1 表单组件标签只能用： <input type="file">
    2.请求方式只能用post不能用get
    get：参数通过请求头提交到后台，参数拼接在url后面；
    简单的字符串可以拼接在url后面，只能向后台提交文本数据
    地址栏url的长度有限制，对提交的参数长度有限制，数据不安全，效率高

    post：参数通过请求体提交到后台,既能提交文本数据，又能提交二进制数据
    理论上对参数长度不受限制，相对安全，效率相对较低

    3表单的编码格式只能用 multipart/form-data。
    根据http协议的规定，浏览器每次向后台提交参数，都会对参数进行统一编码
    默认采用的编码格式是 urlencoded  特点是：只能对文本数据进行编码
    浏览器每次向后台提交参数，都会首先把所有的参数转成字符串，然后对这些数据统一进行urlencoded编码
    文件上传的表单编码格式只能用multipart/form-data。


    --%>
<form action="workbench/activity/fileUpload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="myFile"><br/>
    <input type="text" name="username"><br/>
    <input type="submit" value="提交">
</form>
</body>
</html>
