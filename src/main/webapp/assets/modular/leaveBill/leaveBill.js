layui.use(['table', 'admin', 'ax'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;

    /**
     * 管理
     */
    var LeaveBill = {
        tableId: "leaveBillTable"
    };

    /**
     * 初始化表格的列
     */
    LeaveBill.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: ''},
            {field: 'applicant', sort: true, title: '申请人'},
            {field: 'appTime', sort: true, title: '申请时间'},
            {field: 'days', sort: true, title: '请假天数'},
            {field: 'title', sort: true, title: '标题'},
            {
                field: 'center', sort: true, title: '操作', templet: function (d) {

                    console.log(d);

                    var submit = '<a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="edit">提交流程</a>';
                    var edit = '<a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="edit">修改</a><a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete">删除</a>';

                    if (d.state == 0) {
                        return submit + edit;
                    } else {
                        return "";
                    }
                }
            }
            /*{align: 'center', toolbar: '#tableBar', title: '操作'}*/
        ]];
    };

    /**
     * 点击查询按钮
     */
    LeaveBill.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        table.reload(LeaveBill.tableId, {where: queryData});
    };

    /**
     * 弹出添加对话框
     */
    LeaveBill.openAddDlg = function () {
        window.location.href = Feng.ctxPath + '/leaveBill/add';
    };

    /**
     * 导出excel按钮
     */
    LeaveBill.exportExcel = function () {
        var checkRows = table.checkStatus(LeaveBill.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 点击编辑
     *
     * @param data 点击按钮时候的行数据
     */
    LeaveBill.openEditDlg = function (data) {
        window.location.href = Feng.ctxPath + '/leaveBill/edit?id=' + data.id;
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    LeaveBill.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/leaveBill/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(LeaveBill.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", data.id);
            ajax.start();
        };
        Feng.confirm("是否删除?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + LeaveBill.tableId,
        url: Feng.ctxPath + '/leaveBill/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: LeaveBill.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        LeaveBill.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        LeaveBill.openAddDlg();
    });

    // 导出excel
    $('#btnExp').click(function () {
        LeaveBill.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + LeaveBill.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            LeaveBill.openEditDlg(data);
        } else if (layEvent === 'delete') {
            LeaveBill.onDeleteItem(data);
        }
    });
});
