
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <title>抢红包</title>
</head>
<body>
<br>
<h2>&nbsp;&nbsp;抢红包大战，来耍猴啦！</h2>
<br>
    <table width="100%" style="text-align: center;">
        <thead>
            <tr>
                <td>入口1</td>
                <td>入口2</td>
                <td>入口3</td>
                <td>入口4</td>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><button style="width:300px;height:80px;font-size: 18px;" id="grabOneTime" onclick="grabRedpacket('/grab-red-packet')">点我抢红包,只有一次机会,<br>存在超发的情况</button></td>
                <td><button style="width:300px;height:80px;font-size: 18px;" id="grabOneTimeWithVersion" onclick="grabRedpacket('/grab-red-packet_with-version')">点我抢红包,只有一次机会,<br>存在少发的情况</button></td>
                <td><button style="width:300px;height:80px;font-size: 18px;" id="grabWithinTime" onclick="grabRedpacket('/grab-red-packet_retry-fixed-duration')">点我抢红包，限时100</button></td>
                <td><button style="width:300px;height:80px;font-size: 18px;" id="grabThreeTimes" onclick="grabRedpacket('/grab-red-packet_retry-fixed-times')">点我抢红包，你只有三次机会</button></td>
            </tr>
            <tr>
                <td colspan="4"><button style="margin-top:200px;padding:10px;font-size: 20px;color: coral;" onclick="grabRedpacket('/grab-red-packet_with-redis')">员工内部通道，使用redis抢红包</button></td>
            </tr>
        </tbody>
    </table>


    <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.0.js"></script>
    <script type="text/javascript">
        function grabRedpacket(url){
            console.log("开始抢红包");
            var max = 250;
            var timestamp = Date.parse(new Date());
            for (var i = 1; i <= max; i++) {
                $.post({
                    url:'/redpacket-user'+url+'?redpacketId=1&userId='+i,
                    //异步请求模拟高并发，此时success里看到的i始终是终值
                    success:function (result) {
                        var userId = result.userId;
                        console.log("用户"+userId+"抢红包结果："+result.message);
                        if (userId>=250){
                            var timeConsume = Date.parse(new Date()) - timestamp;
                            console.log(url+"方式的抢红包共耗时"+timeConsume+"毫秒");
                        }
                    }
                });
            }

        }

    </script>

</body>
</html>
