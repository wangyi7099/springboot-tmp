/**
 * 添加或者修改页面
 */
var LeaveBillInfoDlg = {
    data: {
        applicant: "",
        appTime: "",
        days: "",
        title: "",
        content: ""
    }
};

layui.use(['form', 'admin', 'laydate', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var laydate = layui.laydate;

    //让当前iframe弹层高度适应
    admin.iframeAuto();

    // 渲染时间选择框
    laydate.render({
        elem: '#appTime'
    });

    //表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/leaveBill/addItem", function (data) {
            Feng.success("添加成功！");
            window.location.href = Feng.ctxPath + '/leaveBill'
        }, function (data) {
            Feng.error("添加失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();

        return false;
    });

    //返回按钮
    $("#backupPage").click(function () {
        window.location.href = Feng.ctxPath + '/leaveBill'
    });

});