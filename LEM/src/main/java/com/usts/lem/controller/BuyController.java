package com.usts.lem.controller;
import com.alibaba.fastjson.JSONObject;
import com.usts.lem.model.DataList;
import com.usts.lem.model.Buy;
import com.usts.lem.service.IBuyService;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.usts.lem.util.BuyExportExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/buy")
public class BuyController {

    BuyExportExcelUtil buyexportExcelUtil = new BuyExportExcelUtil();
    // 类型于编号映射
    Map<String, String> specMapper = new HashMap<String, String>() {{
        put("显微镜", "A");
        put("精密仪器", "B");
        put("普通实验仪器", "C");
    }};
    @Resource
    IBuyService buyService;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    protected void writeJSON2Response(Object out, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(out);
            log.debug("SERVER TO FRONT END:" + out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/getAllInfo")
    @ResponseBody
    public void getAllInfo(@RequestParam(value = "limit",defaultValue = "10") Integer limit,
                           @RequestParam(value = "offset",defaultValue = "1") Integer offset,
                           @RequestParam(value = "sort") String sort,
                           @RequestParam(value = "order") String order,
                           HttpServletRequest request,HttpServletResponse response) {
        Buy buy = (Buy)request.getSession().getAttribute("buyObj");
        log.debug("SERVER Get Buy: " + buy);
        int size = buyService.count();
        DataList<Buy> buyList = new DataList<>();
        List<Buy> list = buyService.findAllData(offset,limit,sort,order);
        if(list != null) {
            buyList.setRows(list);
            buyList.setTotal(size);
        }
        writeJSON2Response(buyList.toString(),response);
    }
    @PostMapping(value = "/insertData")
    @ResponseBody
    public void insertData(@RequestBody Buy buy,HttpServletRequest request,
                           HttpServletResponse response) {
        log.debug("SERVER Get Buy: " + buy.toString());
        String spec = buy.getSpec();

        String serialNumber;
        int specAmount;
        if(!specMapper.containsKey(spec)) {
            specAmount = buyService.countSpec("其他");
            serialNumber = "T" + String.format("%04d",specAmount+1);
        } else {
            serialNumber = specMapper.get(spec);
            specAmount = buyService.countSpec(spec);
            serialNumber += String.format("%04d",specAmount+1);
        }
        buy.setSerialNumber(serialNumber);

        JSONObject result = new JSONObject();

        if(buyService.insert(buy)>0)
            result.put("flag",true);
        else
            result.put("flag",false);
        writeJSON2Response(result,response);
    }
    @PostMapping(value = "/updateData")
    @ResponseBody
    public void updateData(@RequestBody Buy buy,
                           HttpServletResponse response,HttpServletRequest request) {
        log.debug("SERVER Get Buy " + buy.toString());
        JSONObject result = new JSONObject();
        if(buyService.update(buy)>0)
            result.put("flag",true);
        else
            result.put("flag",false);
        writeJSON2Response(result,response);
    }
    @PostMapping(value = "/findBySerialNumber")
    @ResponseBody
    public void findByTime(@RequestParam(value = "serialNumber") String serialNumber,
                           HttpServletRequest request,HttpServletResponse response) {
        DataList<Buy> buyList = new DataList<>();
        try {
            List<Buy> list = new ArrayList<>();
            list.add(buyService.findBySerialNumber(serialNumber));
            buyList.setRows(list);
            buyList.setTotal(list.size());
        } catch (Exception e) {
            log.error("SERVER " + e.toString());
        }

        writeJSON2Response(buyList.toString(),response);
    }

    @PostMapping(value = "/fuzzSearch")
    @ResponseBody
    public void findByKeyWord(@RequestParam(value = "keyword") String keyword,
                              HttpServletResponse response,HttpServletRequest request) {
        DataList<Buy> buyList = new DataList<>();
        try {
            List<Buy> list = buyService.fuzzSearch(keyword);
            buyList.setRows(list);
            buyList.setTotal(list.size());
        } catch (Exception e) {
            log.error("SERVER " + e.toString());
        }
        writeJSON2Response(buyList.toString(),response);
    }

    @RequestMapping(value = "downloadbuyExcel")
    public @ResponseBody
    String downloadbuyExcel(HttpServletResponse response) {
        response.setContentType("application/binary;charset=UTF-8");
        try {
            ServletOutputStream out = response.getOutputStream();
            try {
                response.setHeader("Content-Disposition",
                        "attachment;fileName=" + URLEncoder.encode("data" + ".xls","UTF-8"));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            String[] titles = {"serialNumber","type","name","spec","unitPrice","manufacture","applyDate","approver"};
            buyexportExcelUtil.export(titles,buyService.findAll(),out);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "导出信息失败";
        }
    }
}