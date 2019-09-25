package com.redpacket._15_7excelview;

import com.redpacket.model.RedpacketUser;
import com.redpacket.repository.RedpacketUserMapper;
import com.utils.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ExcelController {
    private final Logger log = LoggerFactory.getLogger(ExcelController.class);

    @Autowired
    private ExcelView excelView;
    @Autowired
    private RedpacketUserMapper redpacketUserMapper;

    @RequestMapping(path = "/exportExcel")
    public ModelAndView exportExcel(){
        ModelAndView modelAndView = new ModelAndView();
        excelView.setFileName("测试springMVC的excel视图功能.xlsx");
        int total = redpacketUserMapper.getGrabRecordCount(1);
        PageHelper pageHelper = new PageHelper(total,200,3);
        log.info("入参：{} - {}", pageHelper.getMysqlOffset(),pageHelper.getMysqlCount());
        List<RedpacketUser> records =
                redpacketUserMapper.getGrabRecord(1,pageHelper.getMysqlOffset(),pageHelper.getMysqlCount());
        log.info("记录列表大小：{}", records.size());
        modelAndView.addObject("records",records);
        modelAndView.setView(excelView);
        return modelAndView;
    }
}
