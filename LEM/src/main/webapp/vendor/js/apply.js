function getPath() {
    var curPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curPath.indexOf(pathName);
    var localhostPath = curPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    return localhostPath + projectName;
}


$(document).ready(function () {
    initTable();
});

//加载表格
function initTable() {
    $('#table').bootstrapTable('destroy');
    $("#table").bootstrapTable({
        method: "get",
        url: getPath() + "/apply/getVisiableInfo",
        pagination: true, //启动分页
        pageNumber: 1,
        pageSize: 5,
        pageList: [10, 25, 50, 100], //记录数可选列表
        search: false,  //是否启用查询
        showColumns: true,  //显示下拉框勾选要显示的列
        sidePagination: "server", //表示服务端请求
        queryParamsType: "limit",
        clickToSelect: true,
        queryParams: function queryParams(params) {   //设置查询参数
            var param = {
                limit: params.limit,   //页面大小
                offset: params.offset,  //页码
                search: params.search,
                sort: params.sort,
                order: params.order,
            };
            return param;
        },
        onLoadSuccess: function () {  //加载成功时执行
            $('#mResult').addClass('alert-success');
            $('#mResult').html("加载成功!");
            setTimeout(function () {
                $('#mResult').removeClass('alert-success');
                $('#mResult').html('');
            }, 5000);
        },
        onLoadError: function () {  //加载失败时执行
            $('#mResult').addClass('alert-danger');
            $('#mResult').html("由于服务器的原因,加载失败!");
            setTimeout(function () {
                $('#mResult').removeClass('alert-danger');
                $('#mResult').html('');
            }, 2000);
        },
    });
}


// 刷新表格
$('#refreshBtn').click(function () {
    initTable();
});

// 添加信息
$('#insertSave').click(function () {
    var insertType = $('#insertType').val();
    var insertName = $('#insertName').val();
    var insertSpec = $('#insertSpec').val();
    var insertPrice = $('#insertPrice').val();
    var insertMnfc = $('#insertMnfct').val();
    var insertPDate = $('#insertPDate').val();
    console.log(insertPDate + ":00");
    var insertManager = $('#insertManager').val();
    if (insertPDate == "")
        insertPDate = new Date().pattern("yyyy-MM-dd hh:mm:ss");
    else
        insertPDate += ":00";

    $.ajax({
        type: "post",
        url: getPath() + "/equip/insertData",
        async: false,
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "id": 0,
            "serialNumber": "",
            "type": insertType,
            "name": insertName,
            "spec": insertSpec,
            "unitPrice": insertPrice,
            "manufacture": insertMnfc,
            "isvisible": true,
            "applicant":insertManager,
            // "manager": insertManager,
            // "eState": 1,
        }),
        success: function (data) {
            if (data.flag) {
                $('#insertModal').modal("hide");
                $('#insertType').val('');
                $('#insertName').val('');
                $('#insertSpec').val('');
                $('#insertPrice').val('');
                $('#insertMnfct').val('');
                $('#insertPDate').val('');
                $('#insertManager').val('');
                bootbox.alert({
                    centerVertical: true,
                    title: "成功",
                    message: "添加成功!",
                    locale: "zh_CN"
                })
                initTable();
            } else {
                bootbox.alert({
                    centerVertical: true,
                    title: "失败",
                    message: "添加失败!",
                    locale: "zh_CN"
                })
                initTable();
            }

        },
        error: function () {
            $('#mResult').addClass('alert-danger');
            $('#mResult').html("由于服务器原因，添加失败!");
            setTimeout(function () {
                $('#mResult').removeClass('alert-danger');
                $('#mResult').html('');
            }, 2000);
        }
    })

});

// 审批通过
$('#approvalBtn').click(function () {
    var rows = $('#table').bootstrapTable('getSelections');
    if (rows.length == 0) {
        bootbox.alert({
            centerVertical: true,
            title: "错误",
            message: "审批操作至少需要选择一条数据!",
            locale: "zh_CN"
        });

    } else {
        var ids = '';
        $.each(rows, function () {
            ids += this.id + ",";
        });
        ids = ids.substring(0, ids.length - 1);

        bootbox.confirm({
            centerVertical: true,
            title: "审批确认",
            message: "确认批准所选?",
            buttons: {
                cancel: {
                    label: '<i class="fa fa-times"></i> 取消'
                },
                confirm: {
                    label: '<i class="fa fa-check"></i> 确认'
                }
            },
            callback: function (result) {
                if (result) {
                    $.ajax({
                        type: 'POST',
                        url: getPath() + "/apply/approval",
                        data: {ids: ids},
                        dataType: "json",
                        success: function (data) {
                            if (data.flag) {
                                bootbox.alert({
                                    centerVertical: true,
                                    title: "成功",
                                    message: "信息审批成功!",
                                    locale: "zh_CN"
                                });
                                initTable();
                            } else {
                                bootbox.alert({
                                    centerVertical: true,
                                    title: "失败",
                                    message: "信息审批失败!",
                                    locale: "zh_CN"
                                });
                                initTable();
                            }
                        },
                        error: function (data) {
                            $('#mResult').addClass('alert-danger');
                            $('#mResult').html("由于服务器原因，信息审批失败!");
                            setTimeout(function () {
                                $('#mResult').removeClass('alert-danger');
                                $('#mResult').html('');
                            }, 2000);
                        },
                    });
                }
            }
        });
    }
});

// 审批通过
$('#refuseBtn').click(function () {
    var rows = $('#table').bootstrapTable('getSelections');
    if (rows.length == 0) {
        bootbox.alert({
            centerVertical: true,
            title: "错误",
            message: "审批操作至少需要选择一条数据!",
            locale: "zh_CN"
        });

    } else {
        var ids = '';
        $.each(rows, function () {
            ids += this.id + ",";
        });
        ids = ids.substring(0, ids.length - 1);

        bootbox.confirm({
            centerVertical: true,
            title: "审批确认",
            message: "确认驳回所选?",
            buttons: {
                cancel: {
                    label: '<i class="fa fa-times"></i> 取消'
                },
                confirm: {
                    label: '<i class="fa fa-check"></i> 确认'
                }
            },
            callback: function (result) {
                if (result) {
                    $.ajax({
                        type: 'POST',
                        url: getPath() + "/apply/refuse",
                        data: {ids: ids},
                        dataType: "json",
                        success: function (data) {
                            if (data.flag) {
                                bootbox.alert({
                                    centerVertical: true,
                                    title: "成功",
                                    message: "信息审批成功!",
                                    locale: "zh_CN"
                                });
                                initTable();
                            } else {
                                bootbox.alert({
                                    centerVertical: true,
                                    title: "失败",
                                    message: "信息审批失败!",
                                    locale: "zh_CN"
                                });
                                initTable();
                            }
                        },
                        error: function (data) {
                            $('#mResult').addClass('alert-danger');
                            $('#mResult').html("由于服务器原因，信息审批失败!");
                            setTimeout(function () {
                                $('#mResult').removeClass('alert-danger');
                                $('#mResult').html('');
                            }, 2000);
                        },
                    });
                }
            }
        });
    }
});