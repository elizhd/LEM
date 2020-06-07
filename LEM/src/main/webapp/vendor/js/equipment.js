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


// 加载表格
function initTable() {
    $('#table').bootstrapTable('destroy');
    $("#table").bootstrapTable({
        method: "get",
        url: getPath() + "/equip/getAllInfo",
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
            "purchaseDate": insertPDate,
            "manager": insertManager,
            "eState": 1,
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


//维修信息
$('#repairBtn').click(function () {
    var rows = $('#table').bootstrapTable('getSelections');
    if (rows.length == 0) {
        bootbox.alert({
            centerVertical: true,
            title: "错误",
            message: "维修操作至少需要选择一条数据!",
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
            title: "维修确认",
            message: "确认维修所选?",
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
                        url: getPath() + "/equip/repairByIds",
                        data: {ids: ids},
                        dataType: "json",
                        success: function (data) {
                            if (data.flag) {
                                bootbox.alert({
                                    centerVertical: true,
                                    title: "成功",
                                    message: "状态更改为维修成功!",
                                    locale: "zh_CN"
                                });
                                initTable();
                            } else {
                                bootbox.alert({
                                    centerVertical: true,
                                    title: "失败",
                                    message: "状态更改为维修失败!",
                                    locale: "zh_CN"
                                });
                                initTable();
                            }
                        },
                        error: function (data) {
                            $('#mResult').addClass('alert-danger');
                            $('#mResult').html("由于服务器原因，状态更改为维修失败!");
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

// 更新信息
$('#updateBtn').click(function () {
    var rows = $('#table').bootstrapTable('getSelections');
    if (rows.length != 1) {
        bootbox.alert({
            centerVertical: true,
            title: "错误",
            message: "修改操作只能选择一条数据！",
            locale: "zh_CN"
        });
    } else {
        $('#updateType').val(rows[0].type);
        $('#updateName').val(rows[0].name);
        $('#updateSpec').val(rows[0].spec);
        $('#updatePrice').val(rows[0].unitPrice);
        $('#updateMnfct').val(rows[0].manufacture);
        $('#updatePDate').val(rows[0].purchaseDate);
        $('#updateManager').val(rows[0].manager);
        $('#updateModal').modal("toggle");
    }
});

$('#updateSave').click(function () {
    var rows = $('#table').bootstrapTable('getSelections');
    var updateId = rows[0].id;
    var updateType = $('#updateType').val();
    var updateName = $('#updateName').val();
    var updateSpec = $('#updateSpec').val();
    var updatePrice = $('#updatePrice').val();
    var updateMnfc = $('#updateMnfct').val();
    var updatePDate = $('#updatePDate').val();
    var updateManager = $('#updateManager').val();
    if (updatePDate == "")
        updatePDate = new Date().pattern("yyyy-mm-dd hh:mm:ss");
    else {
        updatePDate = updatePDate + ":00";
    }


    $.ajax({
        type: "post",
        url: getPath() + "/equip/updateData",
        async: false,
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            "id": updateId,
            "serialNumber": rows[0].serialNumber,
            "type": updateType,
            "name": updateName,
            "spec": updateSpec,
            "unitPrice": updatePrice,
            "manufacture": updateMnfc,
            "purchaseDate": updatePDate,
            "manager": updateManager,
            "eState": 1,
        }),
        success: function (data) {
            if (data.flag) {
                $('#updateModal').modal("hide");
                $('#updateType').val('');
                $('#updateName').val('');
                $('#updateSpec').val('');
                $('#updatePrice').val('');
                $('#updateMnfct').val('');
                $('#updatePDate').val('');
                $('#updateManager').val('');
                bootbox.alert({
                    centerVertical: true,
                    title: "成功",
                    message: "修改成功!",
                    locale: "zh_CN"
                })
                initTable();
            } else {
                bootbox.alert({
                    centerVertical: true,
                    title: "失败",
                    message: "修改失败!",
                    locale: "zh_CN"
                });
                initTable();
            }

        },
        error: function () {
            $('#updateModal').modal("hide");
            $('#updateType').val('');
            $('#updateName').val('');
            $('#updateSpec').val('');
            $('#updatePrice').val('');
            $('#updateMnfct').val('');
            $('#updatePDate').val('');
            $('#updateManager').val('');
            $('#mResult').addClass('alert-danger');
            $('#mResult').html("由于服务器原因，添加失败!");
            setTimeout(function () {
                $('#mResult').removeClass('alert-danger');
                $('#mResult').html('');
            }, 200);
        }
    })
});


// 编号搜索
$('#findBySerialNumberBtn').click(function () {
    var serialNumber = $("#serialNumber").val();
    $.ajax({
        type: "post",
        url: getPath() + "/equip/findBySerialNumber",
        async: false,
        dataType: 'json',
        data: {serialNumber: serialNumber},
        success: function (data) {
            console.log(data);
            if (data.total != 0) {
                $('#findBySerialNumberModal').modal("hide");
                $("#serialNumber").val("");
                $('#table').bootstrapTable('destroy');
                $('#table').bootstrapTable();
                $('#table').bootstrapTable('load', data);
            } else {
                $('#findBySerialNumberModal').modal("hide");
                $("#serialNumber").val("");
                bootbox.alert({
                    centerVertical: true,
                    title: "失败",
                    message: "搜索失败!",
                    locale: "zh_CN"
                })
                initTable();
            }

        },
        error: function () {
            $('#mResult').addClass('alert-danger');
            $('#mResult').html("由于服务器原因，搜索失败!");
            setTimeout(function () {
                $('#mResult').removeClass('alert-danger');
                $('#mResult').html('');
            }, 2000);
        }
    })
});


// 模糊搜索
$('#fuzzSearchBtn').click(function () {
    var keyWord = $('#searchKeyWord').val();
    $.ajax({
        type: "post",
        url: getPath() + "/equip/fuzzSearch",
        async: false,
        data: {keyword: keyWord},
        dataType: "json",
        success: function (data) {
            if (data.total != 0) {
                $('#findByKeywordModal').modal("hide");
                $('#searchKeyWord').val("");
                $('#table').bootstrapTable('destroy');
                $('#table').bootstrapTable();
                $('#table').bootstrapTable('load', data);
            } else {
                bootbox.alert({
                    centerVertical: true,
                    title: "失败",
                    message: "搜索失败!",
                    locale: "zh_CN"
                })
                initTable();
            }
        },
        error: function () {
            $('#mResult').addClass('alert-danger');
            $('#mResult').html("由于服务器原因，搜索失败!");
            setTimeout(function () {
                $('#mResult').removeClass('alert-danger');
                $('#mResult').html('');
            }, 2000);
        }
    })
});

