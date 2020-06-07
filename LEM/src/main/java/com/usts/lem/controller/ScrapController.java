package com.usts.lem.controller;

import com.alibaba.fastjson.JSONObject;
import com.usts.lem.model.DataList;
import com.usts.lem.model.Scrap;
import com.usts.lem.service.IScrapService;
import com.usts.lem.util.ScrapExportExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

@Controller
@RequestMapping("/scrap")
public class ScrapController {

    ScrapExportExcelUtil exportExcelUtil = new ScrapExportExcelUtil();

    // 类型于编号映射
    Map<String, String> specMapper = new HashMap<String, String>() {{
        put("显微镜", "A");
        put("精密仪器", "B");
        put("普通实验仪器", "C");
    }};

    @Resource
    IScrapService scrapService;

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
        Scrap scrap = (Scrap)request.getSession().getAttribute("scrapObj");
        log.debug("SERVER Get Scrap: " + scrap);
        int size = scrapService.count();
        DataList<Scrap> scrapList = new DataList<>();
        List<Scrap> list = scrapService.findAllData(offset,limit,sort,order);
        if(list != null) {
            scrapList.setRows(list);
            scrapList.setTotal(size);
        }
        writeJSON2Response(scrapList.toString(),response);
    }

    @PostMapping(value = "/insertData")
    @ResponseBody
    public void insertData(@RequestBody Scrap scrap,HttpServletRequest request,
                           HttpServletResponse response) {
        log.debug("SERVER Get Scrap: " + scrap.toString());
        String spec = scrap.getSpec();

        String serialNumber;
        int specAmount;
        if(!specMapper.containsKey(spec)) {
            specAmount = scrapService.countSpec("其他");
            serialNumber = "T" + String.format("%04d",specAmount+1);
        } else {
            serialNumber = specMapper.get(spec);
            specAmount = scrapService.countSpec(spec);
            serialNumber += String.format("%04d",specAmount+1);
        }
        scrap.setSerialNumber(serialNumber);

        JSONObject result = new JSONObject();

        if(scrapService.insert(scrap)>0)
            result.put("flag",true);
        else
            result.put("flag",false);
        writeJSON2Response(result,response);
    }

    @PostMapping(value = "/updateData")
    @ResponseBody
    public void updateData(@RequestBody Scrap scrap,
                           HttpServletResponse response,HttpServletRequest request) {
        log.debug("SERVER Get Scrap " + scrap.toString());
        JSONObject result = new JSONObject();
        if(scrapService.update(scrap)>0)
            result.put("flag",true);
        else
            result.put("flag",false);
        writeJSON2Response(result,response);
    }

    @PostMapping(value = "/findBySerialNumber")
    @ResponseBody
    public void findByTime(@RequestParam(value = "serialNumber") String serialNumber,
                           HttpServletRequest request,HttpServletResponse response) {
        DataList<Scrap> scrapList = new DataList<>();
        try {
            List<Scrap> list = new ArrayList<>();
            list.add(scrapService.findBySerialNumber(serialNumber));
            scrapList.setRows(list);
            scrapList.setTotal(list.size());
        } catch (Exception e) {
            log.error("SERVER " + e.toString());
        }

        writeJSON2Response(scrapList.toString(),response);
    }

    @PostMapping(value = "/fuzzSearch")
    @ResponseBody
    public void findByKeyWord(@RequestParam(value = "keyword") String keyword,
                              HttpServletResponse response,HttpServletRequest request) {
        DataList<Scrap> scrapList = new DataList<>();
        try {
            List<Scrap> list = scrapService.fuzzSearch(keyword);
            scrapList.setRows(list);
            scrapList.setTotal(list.size());
        } catch (Exception e) {
            log.error("SERVER " + e.toString());
        }
        writeJSON2Response(scrapList.toString(),response);
    }

    @RequestMapping(value = "downloadExcel")
    public @ResponseBody
    String downloadExcel(HttpServletResponse response) {
        response.setContentType("application/binary;charset=UTF-8");
        try {
            ServletOutputStream out = response.getOutputStream();
            try {
                response.setHeader("Content-Disposition",
                        "attachment;fileName=" + URLEncoder.encode("data" + ".xls","UTF-8"));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            String[] titles = {"serialNumber","type","name","spec","manufacture","scrapDate","approver"};
            exportExcelUtil.export(titles,scrapService.findAll(),out);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "导出信息失败";
        }
    }
}
