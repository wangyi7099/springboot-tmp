/**
 * 详情对话框
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

    //获取详情信息，填充表单
    var ajax = new $ax(Feng.ctxPath + "/leaveBill/detail?id=" + Feng.getUrlParam("id"));
    var result = ajax.start();
    form.val('leaveBillForm', result.data);

    //表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/leaveBill/editItem", function (data) {
            Feng.success("更新成功！");
            window.location.href = Feng.ctxPath + '/leaveBill'
        }, function (data) {
            Feng.error("更新失败！" + data.responseJSON.message)
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