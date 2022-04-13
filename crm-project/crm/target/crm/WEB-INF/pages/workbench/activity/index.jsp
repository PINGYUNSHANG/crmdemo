<%@ page session="true" contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" +
            request.getServerPort() + "/" + request.getContextPath() + "/";
%>

<html>
<head>
    <base href="<%=basePath%>"/>
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

    <script type="text/javascript">


        $(function () {
            //	给"创建"添加单击事件
            $("#createActionButton").click(function () {
                //初始化工作 ,弹出创建市场活动的模态窗口前，清空表单
                //拿到表单的dom对象  重置表单
                $("#createActivityForm").get(0).reset();

                //弹出创建市场活动的模态窗口
                $("#createActivityModal").modal("show");

            });

            //给保存按钮添加单击事件
            $("#saveCreateActivity").click(function () {
                //收集参数
                var owner = $("#create-marketActivityOwner").val();
                var name = $.trim($("#create-marketActivityName").val());
                var startDate = $("#create-startTime").val();
                var endDate = $("#create-endTime").val();
                var cost = $.trim($("#create-cost").val());
                var description = $.trim($("#create-describe").val());
                //表单验证
                if (owner == "") {
                    alert("所有者不能为空～");
                    return;
                }
                if (name == "") {
                    alert("名称不能为空～");
                    return;
                }
                if (startDate != "" && endDate != "") {
                    //使用字符串来代替日期比较大小
                    if (endDate < startDate) {
                        alert("结束日期不能比开始日期小");
                        return;
                    }
                }
                /**
                 * 正则表达式:
                 * 1 语法：定义一种字符串的匹配模式，
                 * 可以用来判断指定的具体字符串是不是符合字符串的匹配模式
                 * 非负整数的匹配模式：^(([1-9]\d*)|0)$
                 * js种定义一个正则表达式  var regExp = /.../;
                 * ^ 开头 $ 结尾
                 * []  匹配字符集中任意一位字符
                 * {} 匹配次数   {m}:匹配m次 {m,n}：匹配m～n次，{m,}:m或更多次
                 */
                var regExp = /^(([1-9]\d*)|0)$/;
                if (!regExp.test(cost)) {
                    alert("成本只能是非负整数");
                    return;
                }
                //发送请求
                $.ajax({
                    url: 'workbench/activity/saveCreateActivity.do',
                    data: {
                        owner: owner,
                        name: name,
                        startDate: startDate,
                        endDate: endDate,
                        cost: cost,
                        description: description
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            //关闭模态窗口
                            $("#createActivityModal").modal("hide");
                            //列表需要刷新，显示第一页数据
                            queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                        } else {
                            alert(data.message);
                            //模态窗口不关闭
                            $("#createActivityModal").modal("show");
                        }
                    }
                });
            });
            //当容器加载完成后，对容器调用工具函数
            $(".myDate").datetimepicker({
                language: 'zh-CN',             //语言
                format: 'yyyy-mm-dd',         //格式
                minView: 'month',           //可以选择的最小视图
                initialDate: new Date(),    //初始化选择日期
                autoclose: true,    //设置选择完日期或时间之后，是否自动关闭日历
                todayBtn: true,    //设置是否显示今天按钮 ，默认为false
                clearBtn: true//设置是否显示清空按钮，默认是false
            });

            //当市场活动主页面加载完成，查询所有数据的第一页以及所有数据的总条数
            queryActivityByConditionForPage(1, 10);

            //给查询按钮添加单击事件
            $("#queryActivityButton").click(function () {
                //当用户点击'查询'按钮，查询所有符合条件的数据的第一页以及所有符合条件数据的总条数

                queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));

            });
            //给全选按钮加单击事件
            $("#checkAll").click(function () {
                //如果全选按钮是选中状态，则列表中所有checkbox都选中
                //获取属性值
                // if(this.checked) {
                //   $("#tBody input[type='checkbox']").prop("checked",true);
                // }else {
                //     $("#tBody input[type='checkbox']").prop("checked",false);
                // }
                $("#tBody input[type='checkbox']").prop("checked", this.checked);
            });
            /*
                        $("#tBody input[type='checkbox']").click(function () {
                            //如果列表中的所有checkbox都选中，则全选按钮也选中
                            if($("#tBody input[type='checkbox']").size()==
                            $("#tBody input[type='checkbox']:checked").size()){
                                $("#checkAll").prop("checked",true);
                            }else {
                                //如果至少有一个没选中，则全选按钮也取消
                                $("#checkAll").prop("checked",false);
                            }
                        });
                        */

            $("#tBody").on("click", "input[type='checkbox']", function () {
                if ($("#tBody input[type='checkbox']").size() ==
                    $("#tBody input[type='checkbox']:checked").size()) {
                    $("#checkAll").prop("checked", true);
                } else {
                    //如果至少有一个没选中，则全选按钮也取消
                    $("#checkAll").prop("checked", false);
                }
            });

            //给删除按钮添加单击事件
            $("#deleteActivityBtn").click(function () {
                //收集参数
                //获取列表中所有被选中的checkbox
                var checkedIds = $("#tBody input[type='checkbox']:checked");
                if (checkedIds.size() == 0) {
                    alert("请选择要删除的市场活动");
                    return;
                }
                if (window.confirm("确定删除吗？")) {
                    var ids = "";
                    $.each(checkedIds, function () {
                        ids += "id=" + this.value + "&";
                    });
                    ids.substr(0, ids.length - 1);//id=xxx&id=xxx...&id=xxx
                    //发送请求
                    $.ajax({
                        url: "workbench/activity/deleteActivityIds.do",
                        data: ids,
                        type: 'post',
                        dataType: 'json',
                        success: function (data) {
                            if (data.code == "1") {
                                //刷新市场活动列表，显示第一页数据
                                queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                            } else {
                                alert(data.message);
                            }
                        }
                    });
                }
            });

            //给修改按钮添加单击事件
            $("#editActionButton").click(function () {
                //收集参数 id
                //获取列表中被选中的checkbox
                var checkedIds = $("#tBody input[type='checkbox']:checked");
                if (checkedIds.size() == 0) {
                    alert("请选择要修改的市场活动");
                    return;
                }
                if (checkedIds.size() > 1) {
                    alert("只能修改一条市场活动");
                    return;
                }
                var id = checkedIds.val();
                //发送请求
                $.ajax({
                    url: "workbench/activity/queryActivityById.do",
                    data: {
                        id:id
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        //把市场活动的信息显示在模态窗口上
                        $("#edit-id").val(data.id);
                        $("#edit-marketActivityOwner").val(data.owner);
                        $("#edit-marketActivityName").val(data.name);
                        $("#edit-startTime").val(data.startDate);
                        $("#edit-endTime").val(data.endDate);
                        $("#edit-cost").val(data.cost);
                        $("#edit-describe").val(data.description);
                        //弹出模态窗口
                        $("#editActivityModal").modal("show");
                    }
                });
            });

            //给更新按钮添加单击事件
            $("#saveEditActivityBtn").click(function () {
                //收集参数
                var id = $("#edit-id").val();
                var owner = $("#edit-marketActivityOwner").val();
                var name = $.trim($("#edit-marketActivityName").val());
                var startDate = $("#edit-startTime").val();
                var endDate = $("#edit-endTime").val();
                var cost = $.trim($("#edit-cost").val());
                var description = $.trim($("#edit-describe").val());
                //表单验证
                if (owner == "") {
                    alert("所有者不能为空～");
                    return;
                }
                if (name == "") {
                    alert("名称不能为空～");
                    return;
                }
                if (startDate != "" && endDate != "") {
                    //使用字符串来代替日期比较大小
                    if (endDate < startDate) {
                        alert("结束日期不能比开始日期小");
                        return;
                    }
                }
                var regExp = /^(([1-9]\d*)|0)$/;
                if (!regExp.test(cost)) {
                    alert("成本只能是非负整数");
                    return;
                }
                //发送请求
                $.ajax({
                    url: "workbench/activity/SaveEditActivity.do",
                    data:{
                        id:id,
                        owner:owner,
                        name:name,
                        startDate:startDate,
                        endDate:endDate,
                        cost:cost,
                        description:description
                    },
                    type:"post",
                    dataType: "json",
                    success: function (data) {
                        if (data.code == "1") {
                            //关闭模态窗口
                            $("#editActivityModal").modal("hide");
                            //列表需要刷新，显示更新之前的页面数据
                            queryActivityByConditionForPage($("#demo_pag1").bs_pagination('getOption', 'currentPage'), $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                        } else {
                            alert(data.message);
                            //模态窗口不关闭
                            $("#editActivityModal").modal("show");
                        }
                    }
                });
            });
            //给批量导出按钮添加单击事件
            $("#exportActivityAllBtn").click(function () {
                window.location.href="workbench/activity/exportAllActivities.do";
            });
            //给导入按钮添加单击事件
            $("#importActivityBtn").click(function () {
                //收集参数
                var activityFileName = $("#activityFile").val();
                //根据文件名判断是不是excel文件
                var suffix = activityFileName.substr(activityFileName.lastIndexOf(".")+1).toLocaleLowerCase();
                if(suffix!="xls"){
                    alert("只支持xls文件");
                    return;
                }
               var activityFile = $("#activityFile")[0].files[0];
               if( activityFile.size>5*1024*1024){
                   alert("文件大小不能超过5MB");
                   return;
               }
               //发送请求
               //  FormData  是ajax提供的一个接口，可以模拟键值对向后台提交参数
                // 不但能提交字符串， 还能提交二进制数据 （文件。。。）
                var formdata = new FormData();
                formdata.append("activityFile",activityFile);
                $.ajax({
                    url:"workbench/activity/importActivity.do",
                    data:formdata,
                    processData:false, //设置ajax向后台提交参数之前，是否将参数统一转换成字符串，true--是
                    contentType:false,//设置ajax向后台提交参数之前，是的把所有参数统一按urlencoded编码  true--是
                    type:"POST",
                    dataType:"json",
                    success:function (data) {
                        if(data.code=="1"){
                            //提示成功导入记录条数
                            alert("成功导入"+data.retData+"条记录～");
                            //关闭模态窗口
                             $("#importActivityModal").modal("hide");
                             //刷新市场活动，显示第一页数据
                            queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));

                        }else {
                            //提示信息
                            alert(data.message);
                            $("#importActivityModal").modal("show");
                        }
                    }
                });
            });
        });

        //封装函数
        function queryActivityByConditionForPage(pageNo, pageSize) {
            //收集参数
            var name = $("#query-name").val();
            var owner = $("#query-owner").val();
            var startDate = $("#query-startDate").val();
            var endDate = $("#query-endDate").val();
            //var pageNo = 1;
            //var pageSize = 10;
            //发送请求
            $.ajax({
                url: 'workbench/activity/queryActivityByConditionForPage.do',
                data: {
                    name: name,
                    owner: owner,
                    startDate: startDate,
                    endDate: endDate,
                    pageNo: pageNo,
                    pageSize: pageSize
                },
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    //显示总条数
                    // $("#totalRows").text(data.totalRows);
                    //显示列表 遍历数组
                    var htmlStr = "";
                    $.each(data.activityList, function (index, obj) {
                        htmlStr+="<tr class=\"active\">";
                        htmlStr+="<td><input type=\"checkbox\" value=\""+obj.id+"\"/></td>";
                        htmlStr+="<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/detailActivity.do?id="+obj.id+"'\">"+obj.name+"</a></td>";
                        htmlStr+="<td>"+obj.owner+"</td>";
                        htmlStr+="<td>"+obj.startDate+"</td>";
                        htmlStr+="<td>"+obj.endDate+"</td>";
                        htmlStr+="</tr>";
                    });
                    $("#tBody").html(htmlStr);

                    //每次查询完后都要刷新列表 ,取消全选按钮
                    $("#checkAll").prop("checked", false);

                    //计算总页数
                    var totalPages = 1;
                    if (data.totalRows % pageSize == 0) {
                        totalPages = data.totalRows / pageSize;
                    } else {
                        totalPages = parseInt(data.totalRows / pageSize) + 1;
                    }
                    //对容器 调用分页函数
                    $("#demo_pag1").bs_pagination({
                        totalPages: totalPages, //总页数,必填参数
                        currentPage: pageNo,    //当前页号，相当于pageNo
                        rowsPerPage: pageSize,    //每页显示条数  pageSize
                        totalRows: data.totalRows,     //总条数
                        visiblePageLinks: 5,  //最多可以选择的卡片数
                        showGoToPage: true,        //控制是否显示 要跳转到''
                        showRowsInfo: true,  //是否显示记录的信息，默认true --显示
                        onChangePage: function (event, pageObj) {
                            //用户每次切换页号，都自动触发本函数
                            //每次返回切换页号之后的pageNo 和 pageSize
                            queryActivityByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage);
                        }
                    });
                }
            });
        }
    </script>
</head>
<body>

<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" id="createActivityForm" role="form">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-marketActivityOwner">
                                <c:forEach items="${userList}" var="u">
                                    <option value="${u.id}">${u.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control myDate" id="create-startTime" readonly>
                        </div>
                        <label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control myDate" id="create-endTime" readonly>
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-describe"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveCreateActivity">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">
                    <input type="hidden" id="edit-id">
                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-marketActivityOwner">
                                <c:forEach items="${userList}" var="u">
                                    <option value="${u.id}">${u.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control myDate" id="edit-startTime" value="2020-10-10">
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control myDate" id="edit-endTime" value="2020-10-20">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" value="5,000">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveEditActivityBtn">更新</button>
            </div>
        </div>
    </div>
</div>

<!-- 导入市场活动的模态窗口 -->
<div class="modal fade" id="importActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
            </div>
            <div class="modal-body" style="height: 350px;">
                <div style="position: relative;top: 20px; left: 50px;">
                    请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                </div>
                <div style="position: relative;top: 40px; left: 50px;">
                    <input type="file" id="activityFile">
                </div>
                <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;">
                    <h3>重要提示</h3>
                    <ul>
                        <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                        <li>给定文件的第一行将视为字段名。</li>
                        <li>请确认您的文件大小不超过5MB。</li>
                        <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                        <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                        <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                        <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="query-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="query-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control" type="text" id="query-startDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control" type="text" id="query-endDate">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="queryActivityButton">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="createActionButton"><span
                        class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" id="editActionButton"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span
                        class="glyphicon glyphicon-minus"></span> 删除
                </button>
            </div>
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal">
                    <span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）
                </button>
                <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span
                        class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）
                </button>
                <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span
                        class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）
                </button>
            </div>
        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="checkAll"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="tBody">
                <%--                <tr class="active">--%>
                <%--                    <td><input type="checkbox"/></td>--%>
                <%--                    <td><a style="text-decoration: none; cursor: pointer;"--%>
                <%--                           onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
                <%--                    <td>zhangsan</td>--%>
                <%--                    <td>2020-10-10</td>--%>
                <%--                    <td>2020-10-20</td>--%>
                <%--                </tr>--%>
                <%--                <tr class="active">--%>
                <%--                    <td><input type="checkbox"/></td>--%>
                <%--                    <td><a style="text-decoration: none; cursor: pointer;"--%>
                <%--                           onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
                <%--                    <td>zhangsan</td>--%>
                <%--                    <td>2020-10-10</td>--%>
                <%--                    <td>2020-10-20</td>--%>
                <%--                </tr>--%>
                </tbody>
            </table>
            <div id="demo_pag1"></div>
        </div>
        <%--        <div style="height: 50px; position: relative;top: 30px;">--%>
        <%--            <div>--%>
        <%--                <button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRows"></b>条记录--%>
        <%--                </button>--%>
        <%--            </div>--%>
        <%--            <div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
        <%--                <button type="button" class="btn btn-default" style="cursor: default;">显示</button>--%>
        <%--                <div class="btn-group">--%>
        <%--                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
        <%--                        10--%>
        <%--                        <span class="caret"></span>--%>
        <%--                    </button>--%>
        <%--                    <ul class="dropdown-menu" role="menu">--%>
        <%--                        <li><a href="#">20</a></li>--%>
        <%--                        <li><a href="#">30</a></li>--%>
        <%--                    </ul>--%>
        <%--                </div>--%>
        <%--                <button type="button" class="btn btn-default" style="cursor: default;">条/页</button>--%>
        <%--            </div>--%>
        <%--            <div style="position: relative;top: -88px; left: 285px;">--%>
        <%--                <nav>--%>
        <%--                    <ul class="pagination">--%>
        <%--                        <li class="disabled"><a href="#">首页</a></li>--%>
        <%--                        <li class="disabled"><a href="#">上一页</a></li>--%>
        <%--                        <li class="active"><a href="#">1</a></li>--%>
        <%--                        <li><a href="#">2</a></li>--%>
        <%--                        <li><a href="#">3</a></li>--%>
        <%--                        <li><a href="#">4</a></li>--%>
        <%--                        <li><a href="#">5</a></li>--%>
        <%--                        <li><a href="#">下一页</a></li>--%>
        <%--                        <li class="disabled"><a href="#">末页</a></li>--%>
        <%--                    </ul>--%>
        <%--                </nav>--%>
        <%--            </div>--%>
        <%--            --%>
        <%--        </div>--%>

    </div>
</div>

</body>
</html>