package com.redpacket._15_7excelview;

import com.redpacket.model.RedpacketUser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
@Component
public class ExcelView extends AbstractXlsxView {
    public static final String DEFAULT_CHARSET = "UTF-8";
    private final Logger log = LoggerFactory.getLogger(ExcelView.class);

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        if (StringUtils.isEmpty(fileName)){
            return;
        }
        String reqCharset = request.getCharacterEncoding();
        if (StringUtils.isEmpty(reqCharset)){
            reqCharset = DEFAULT_CHARSET;
        }
        fileName = new String(fileName.getBytes(reqCharset),"ISO8859-1");
        response.setHeader("Content-disposition","attachment;filename="+fileName);
        makeWorkBook(model, workbook);
    }

    private void makeWorkBook(Map<String, Object> model, Workbook workbook) {
        List<RedpacketUser> userGrabList = (List<RedpacketUser>) model.get("records");
        Sheet sheet = workbook.createSheet("用户抢红包记录");
        Row title = sheet.createRow(0);
        title.createCell(0).setCellValue("记录编号");
        title.createCell(1).setCellValue("红包编号");
        title.createCell(2).setCellValue("用户编号");
        title.createCell(3).setCellValue("红包金额");
        title.createCell(4).setCellValue("抢到时间");
        int rowIndex = 1;
        for (RedpacketUser record : userGrabList) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(record.getId());
            row.createCell(1).setCellValue(record.getRedpacketId());
            row.createCell(2).setCellValue(record.getUserId());
            row.createCell(3).setCellValue(record.getAmount().toString());
            row.createCell(4).setCellValue(record.getGrabTime());
        }
    }
}
