package com.redpacket._15_8multipartfile;

import com.redpacket.FileUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.charset.Charset;

@Controller
@RequestMapping("/file")
public class FileController {
    private final Logger log = LoggerFactory.getLogger(FileController.class);

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public ModelAndView upload(HttpServletRequest request) {
        MultipartHttpServletRequest mhsr = (MultipartHttpServletRequest) request;
        MultipartFile file = mhsr.getFile("file");
        ModelAndView mv = new ModelAndView();
        mv.setView(new MappingJackson2JsonView());
        String filepath = "directory on server"+file.getOriginalFilename();//必须有效
        System.out.println(filepath);
        System.out.println(file.getSize());
        File dest = new File(filepath);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        mv.addObject("success",true);
        mv.addObject("msg","上传文件成功");
        return mv;
    }
    /*=====-=-=-=- 使用springMVC的MultipartFile 和 servlet API的Part可以做到与ServletAPI解耦 =-=-=-=-======*/
    @RequestMapping(value = "/uploadMultipart",method = RequestMethod.POST)
    public ModelAndView uploadMultipartFile(MultipartFile file) throws IOException {
        ModelAndView mv = new ModelAndView();
        mv.setView(new MappingJackson2JsonView());
        InputStream inputStream = file.getInputStream();
        printIt(inputStream);
        //String fileName = file.getOriginalFilename();
        //File dest = new File(fileName);
        //file.transferTo(dest);
        mv.addObject("success",true);
        mv.addObject("msg","上传文件成功");
        return mv;
    }
    @RequestMapping("/uploadPart")
    public ModelAndView uploadPart(Part file) throws IOException {
        ModelAndView mv = new ModelAndView();
        mv.setView(new MappingJackson2JsonView());
        String fileName = file.getSubmittedFileName();
        file.write("/"+fileName);//不起作用，在WebAppInitializer配置的upload文件夹下没有文件
        printIt(file.getInputStream());
        mv.addObject("success",true);
        mv.addObject("msg","上传文件成功");
        return mv;
    }

    void printIt(InputStream is){
        try (StringBuilderWriter builderWriter = new StringBuilderWriter()){
            InputStreamReader streamReader = new InputStreamReader(is, Charset.forName("UTF-8"));
            int count = 0,EOF = -1;
            char[] DEFAULT_BUFFER = new char[4*1024];
            while (EOF != (count = streamReader.read(DEFAULT_BUFFER))){
                builderWriter.write(DEFAULT_BUFFER,0,count);
            }
            String fileStr = builderWriter.toString();
            System.out.println(fileStr);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
