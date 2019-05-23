layui.use(['layer', 'ax', 'table'], function () {
    var $ = layui.$;
    var layer = layui.layer;
    var $ax = layui.ax;
    var table = layui.table;

    /**
     * 系统管理--用户管理
     */
    var FlowModel = {
        tableId: "flowTable",    //表格id
        condition: {
            name: "",
            deptId: "",
            timeLimit: ""
        }
    };

    /**
     * 初始化表格的列
     */
    FlowModel.initColumn = function () {
        return [[
            {field: 'id', hide: true, sort: true, title: 'modelid'},
            {field: 'name', sort: true, title: '名称'},
            {field: 'key', sort: true, title: 'key'},
            {field: 'description', sort: true, title: '描述'},
            {field: 'version', sort: true, title: '版本'},
            {field: 'created', sort: true, title: '创建时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 280}
        ]];
    };


    /**
     * 点击查询按钮
     */
    FlowModel.search = function () {
        var queryData = {};
        queryData['name'] = $("#flowname").val();
        queryData['key'] = $("#flowkey").val();
        table.reload(FlowModel.tableId, {where: queryData});
    };


    /**
     * 点击删除按钮
     *
     * @param data 点击按钮时候的行数据
     */
    FlowModel.onDeleteModel = function (data) {

        var operation = function () {
            $.ajax({
                url: Feng.ctxPath + "/app/rest/models/" + data.id + "?cascade=false",
                type: "delete",
                success: function (data) {
                    table.reload(FlowModel.tableId);
                    Feng.success("删除成功!");
                },
                error: function (xhr) {
                    console.log(xhr);
                    Feng.error("删除失败!" + xhr + "!");
                }

            })
        }
        Feng.confirm("是否删除模型" + data.name + "?", operation);

    };


    // 渲染表格
    var tableResult = table.render({
        elem: '#' + FlowModel.tableId,
        url: Feng.ctxPath + '/gunsApi/flow/list',
        page: true,
        height: "full-98",
        cellMinWidth: 100,
        cols: FlowModel.initColumn()
    });


    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        FlowModel.search();
    });

    $('#btnAdd').click(function () {
        window.open(Feng.ctxPath + "/assets/modeler/index.html?trigger=createProcess");
    });

    FlowModel.deploy = function (id) {
        var ajax = new $ax(Feng.ctxPath + "/gunsApi/flow/deploy", function () {
            table.reload(MgrUser.tableId);
            Feng.success("部署成功!");
        }, function (data) {
            Feng.error("署失败!" + data.responseJSON.message + "!");
        });
        ajax.set("modelId", id);
        ajax.start();
    };


    // 工具条点击事件
    table.on('tool(' + FlowModel.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'view') {
            window.open(Feng.ctxPath + "/assets/modeler/index.html#/editor/" + data.id);
        } else if (layEvent === 'deploy') {
            FlowModel.deploy(data.id);
        } else if (layEvent === 'delete') {
            FlowModel.onDeleteModel(data);
        }
    });

});
