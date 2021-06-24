<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="common/tag.jsp"%>
<html lang="zh-CN">
<head pageContext.request.contextpath >
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>秒杀详情页</title>
    <jsp:include page="common/head.jsp"/>
    < />
</head>
<body>
<div class="container">
    <div class="panel panel-default text-center">
        <div class="panel-heading">
            <h1>${seckill.name}</h1>
        </div>
        <div class="panel-body">
            <h2 class="text-danger">
                <%-- 显示time图标--%>
                <span class="glyphicon glyphicon-time"></span>
                <%-- 展示倒计时 --%>
                <span class="countdown" id="seckill-box"></span>
            </h2>
        </div>
    </div>
</div>
<%-- 登陆弹出层，输入电话 --%>
<div id="killPhoneModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title text-center">
                    <span class="glyphicon glyphicon-phone"></span>秒杀电话:
                </h3>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        <input type="text" name="killPhone" id="killPhoneKey"
                                placeholder="填手机号^o^" class="form-control" />
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <%-- 验证信息 --%>
                <span id="killPhoneMessage" class="glyphicon"></span>
                <button type="button" id="killPhoneBtn" class="btn btn-success">
                    <span class="glyphicon glyphicon-phone"></span>
                    Submit
                </button>
            </div>
        </div>
    </div>
</div>
</body>

<!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
<script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js" integrity="sha384-nvAa0+6Qg9clwYCGGPpDQLVpLNn0fRaROjHqs13t4Ggj3Ez50XnGQqc/r8MhnRDZ" crossorigin="anonymous"></script>
<!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js" integrity="sha384-aJ21OjlMXNL5UyIl/XNwTMqvzeRMZH2w8c5cRVpzpU8Y5bApTppSuUkhZXN0VxHd" crossorigin="anonymous"></script>
<%-- jQuery cookie操作插件--%>
<script src="https://cdn.bootcdn.net/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<%-- jQuery countDown倒计时插件--%>
<script src="https://cdn.bootcss.com/jquery.countdown/2.2.0/jquery.countdown.min.js"></script>

<%-- 开始编写交互逻辑 --%>
<script src="${pageContext.request.contextPath}/resources/script/seckill.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function (){
        //使用EL表达式传入参数
        seckill.detail.init({
            seckillId : ${seckill.seckillId},
            startTime : ${seckill.startTime.time},
            endTime : ${seckill.endTime.time}
        });
    });
</script>

</html>