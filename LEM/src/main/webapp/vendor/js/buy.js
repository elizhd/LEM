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
        url: getPath() + "/buy/getAllInfo",
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

        showExport: true,              //是否显示导出按钮(此方法是自己写的目的是判断终端是电脑还是手机,电脑则返回true,手机返回false,手机不显示按钮)
        exportDataType: "basic",              //basic', 'all', 'selected'.
        exportTypes: ['excel'],	    //导出类型
        exportOptions: {
            fileName: '数据导出',              //文件名称设置
            worksheetName: 'Sheet1',          //表格工作区名称
            tableName: '报废记录表',
            excelstyles: ['background-color', 'color', 'font-size', 'font-weight'],
        }
    });
}

// 刷新表格
$('#refreshBtn').click(function () {
    initTable();
});

//编号搜索
$('#findBySerialNumberBtn').click(function () {
    var serialNumber = $("#serialNumber").val();
    $.ajax({
        type: "post",
        url: getPath() + "/buy/findBySerialNumber",
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



//导出数据
$('#exportBtn').click(function () {
        var url = getPath() + "/buy/downloadbuyExcel";
        window.open(url);
    }
);
// 模糊搜索
$('#fuzzSearchBtn').click(function () {
    var keyWord = $('#searchKeyWord').val();
    $.ajax({
        type: "post",
        url: getPath() + "/buy/fuzzSearch",
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


