<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0,maximum-scale=1.0, user-scalable=no"/>
    <title>RC RTC消息发送</title>
    <script type="text/javascript" src="/static/js/jquery-3.5.1.min.js"></script>
    <style type="text/css">
        .container {
            padding: 20px 10px;
            font-size: 12px;
        }

        .div_header {
            font-weight: bold;
            font-size: 15px;
            color: #424242;
            padding: 10px;
            margin-bottom: 20px;
        }

        .div_row {
            display: flex;
            flex-direction: column;
            margin: 10px 0;
        }

        .input_text {
            border: 1px solid #f4f4f4;
            border-radius: 3px;
            padding: 8px 15px;
            margin: 10px 0;
            font-size: 12px;
        }

        .btn {
            font-size: 13px;
            color: #ffffff;
            border-radius: 3px;
            border: 0px;
            background-color: #169BD5;
            padding: 5px 15px;
        }


        .btn:active {
            background-color: #2fafd5;
            border: 0px;
        }

        .footer {
            font-size: 12px;
            color: #999999;
            text-align: center;
            position: fixed;
            bottom: 50px;
            width: 100%;
        }

        .div_row .btn-row {
            display: flex;
            flex-direction: row;
            align-items: center;
        }

        .div-tip {
            font-size: 12px;
            color: red;
            margin-left: 25px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="div_header">
        发送系统推送消息，到融云 RTC 全服房间
    </div>
    <div class="div_row">
        <input class="input_text" readonly type="text" value="我是一条系统广播消息，嘻嘻。">
        <div class="btn-row">
            <input class="btn" type="button" value="发 送">
            <div class="div-tip"></div>
        </div>
    </div>
    <div class="div_row">
        <input class="input_text" readonly type="text" value="欢迎使用融云 RTC ，请把您的宝贵意见反馈与我们。">
        <div class="btn-row">
            <input class="btn" type="button" value="发 送">
            <div class="div-tip"></div>
        </div>
    </div>
    <div class="div_row">
        <input class="input_text" readonly type="text" value="大家看到这条消息请打个1，谢谢。">
        <div class="btn-row">
            <input class="btn" type="button" value="发 送">
            <div class="div-tip"></div>
        </div>
    </div>
</div>
<div class="footer">
    北京云中融信网络科技有限公司
</div>

<script>
    $(".btn").bind("click", function () {
        var msg = $(this).parent().parent().find("input").val();
        var btn = this;
        $.ajax({
            type: 'get',
            data: {
                msg: msg
            },
            url: '/system/send?r=' + Math.random(),
            dataType: 'json',
            beforeSend: function () {
                $(this).attr("disabled", "disabled");
                $(this).text("发送中...");
            },
            success: function (res) {
                if (res.code == 10000) {
                    console.log("send success");
                    $(btn).parent().find(".div-tip").text("发送成功");
                } else {
                    $(btn).parent().find(".div-tip").text(res.msg);
                }
            },
            complete: function () {
                $(this).removeAttr("disabled");
                $(this).text("发 送");
            }
        })

        return false;
    });
</script>
</body>
</html>