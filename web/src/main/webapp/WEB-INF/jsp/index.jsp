<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link href="https://cdn.bootcss.com/twitter-bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div style="text-align: center">
    <h2><spring:message code="welcome"/> </h2>
    <p><spring:message code="source"/> </p>
    <a href="/index/main?language=en_US">English</a>&emsp;<a href="/index/main?language=zh_CN">中文</a>
    <br><br>
    <a href="/redpacket/grab">去抢红包！</a>
    <br><br>
    <button onclick="testReturn()">测试返回功能</button><br><br>
    <a href="/exportExcel">试试导出excel文件</a><br><br>
</div>
<div style="width: 100%;padding: 30px;">
    <p style="color: aqua;"> + 多种方式实现的Web文件上传功能</p>
    <div class="row" style="width: 100%;font-size: 14px;">
        <div class="col-xm-4 col" >
            <form method="post" action="/file/upload" enctype="multipart/form-data">
                <input type="file" name="file" value="选择上传的文件">
                <input type="submit" value="提交带有servlet-http-api的">
            </form>
        </div>
        <div class="col-xm-4 col" >
            <form method="post" action="/file/uploadMultipart" enctype="multipart/form-data">
                <input type="file" name="file" value="选择上传的文件">
                <input type="submit" value="提交springMVC的multipart">
            </form>
        </div>
        <div class="col-xm-4 col" >
            <form method="post" action="/file/uploadPart" enctype="multipart/form-data">
                <input type="file" name="file" value="选择上传的文件">
                <input type="submit" value="提交ServletApi中的Part">
            </form>
        </div>
    </div><br>
    <div style="width: 100%;border: 2px solid black;">
        <p style="color: aqua;"> + springframework之@Async异步注解探究</p>
        <div class="row" style="text-align: center;" >
            <div class="col-xm-4 col">
                <a href="/manyfunc/testVoidAsyncImpact">@Async标在空返回的方法上</a>
            </div>
            <div class="col-xm-4 col">
                <a href="/manyfunc/testNonVoidAsyncImpact">@Async标在返回Future<>的方法上</a>
            </div>
            <div class="col-xm-4 col">
                <a href="/manyfunc/testAsyncTxImpact">检查@Async注解对于事务的影响</a>
            </div>
        </div>
    </div>


<div style="width: 100%;padding: 50px;">

    </div>

</div>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.0.js"></script>
<script src="https://cdn.bootcss.com/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script type="text/javascript">
    function testReturn() {
        $.post({
            url:'/redpacket/test-return',
            success:function (result) {
                console.log(result.data);
            }
        });
    }
</script>
<p>current language:${pageContext.response.locale}</p>
</body>
</html>
