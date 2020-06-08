package com.usts.lem.util;

import com.usts.lem.model.Buy;
import org.apache.poi.hssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

public class BuyExportExcelUtil {
    public void export(String[] titles, List<Buy> list, ServletOutputStream out) throws Exception {
        try {
            // 第一步，创建一个workbook，对应一个Excel文件
            HSSFWorkbook workbook = new HSSFWorkbook();

            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet hssfSheet = workbook.createSheet("sheet1");

            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short

            HSSFRow row = hssfSheet.createRow(0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle hssfCellStyle = workbook.createCellStyle();

            //居中样式
            hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            HSSFCell hssfCell = null;
            for (int i = 0; i < titles.length; i++) {
                hssfCell = row.createCell(i);//列索引从0开始
                hssfCell.setCellValue(titles[i]);//列名1
                hssfCell.setCellStyle(hssfCellStyle);//列居中显示
            }

            // 第五步，写入实体数据
            for (int i = 0; i < list.size(); i++) {
                row = hssfSheet.createRow(i + 1);
                Buy buy = list.get(i);

                // 第六步，创建单元格，并设置值
                String serialNumber = String.valueOf(buy.getSerialNumber());
                row.createCell(0).setCellValue(serialNumber);

                String type = "";
                if (buy.getType() != null)
                    type = buy.getType();
                row.createCell(1).setCellValue(type);

                String name = "";
                if (buy.getName() != null)
                    name = buy.getName();
                row.createCell(2).setCellValue(name);

                String spec = "";
                if (buy.getSpec() != null)
                    spec = buy.getSpec();
                row.createCell(3).setCellValue(spec);

                double unitPrice = 0;
                if (buy.getUnitPrice() != 0)
                    unitPrice = buy.getUnitPrice();
                row.createCell(4).setCellValue(unitPrice);



                String manufacture = "";
                if (buy.getManufacture() != null)
                    manufacture = buy.getManufacture();
                row.createCell(5).setCellValue(manufacture);

                String applyDate = "";
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//Date转换String
                if (buy.getApplyDate() != null)
                    applyDate = formatter.format(buy.getApplyDate());
                row.createCell(6).setCellValue(applyDate);

                String approver = "";
                if (buy.getApprover() != null)
                    approver = buy.getApprover();
                row.createCell(7).setCellValue(approver);
            }

            //第七步，将文件输出到客户端浏览器
            try {
                workbook.write(out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("导出信息失败！");
        }


    }
}
