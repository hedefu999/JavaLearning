<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<div style="text-align: center">
    <h2>高并发抢红包DEMO</h2>
    <p>《JavaEE互联网轻量级框架整合开发》- chapter 22</p>
    <a href="/redpacket/grab">去抢红包！</a>
    <br><br>
    <button onclick="testReturn()">测试返回功能</button><br><br>
    <a href="/exportExcel">试试导出excel文件</a><br><br>
    <form method="post" action="/file/upload" enctype="multipart/form-data">
        <input type="file" name="file" value="选择上传的文件">
        <input type="submit" value="提交带有servlet-http-api的">
    </form>
    <form method="post" action="/file/uploadMultipart" enctype="multipart/form-data">
        <input type="file" name="file" value="选择上传的文件">
        <input type="submit" value="提交springMVC的multipart">
    </form>
    <form method="post" action="/file/uploadPart" enctype="multipart/form-data">
        <input type="file" name="file" value="选择上传的文件">
        <input type="submit" value="提交ServletApi中的Part">
    </form>
</div>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.0.js"></script>
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
</body>
</html>
