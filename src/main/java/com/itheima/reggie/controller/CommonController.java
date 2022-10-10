package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 文件上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${Reggie.path}")
    private String pathName;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info("文件上传...");
        String Filename = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        Filename= UUID.randomUUID().toString()+ Filename;
        log.info("上传后生成的文件名字"+Filename);
        //判断pathName这个路径存不存在，如果不存在会抛出异常，不会自动创建
        File dir = new File(pathName);
        if(!dir.exists()){
            //如果不存在，创建一个
            dir.mkdirs();
        }
        //确定图片文件的存放地址
        File uploadFile = new File(pathName+Filename);
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(Filename);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            File file = new File(pathName + name);
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(file);
            //通过输出流向浏览器写回数据，展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            //设置响应的类型
            response.setContentType("image/jpeg");
            //将流读入Byte数组中
            byte[] bytes =new byte[1024];
            int len=0;
            while((len=fileInputStream.read(bytes))!=-1){
                     outputStream.write(bytes,0,len);
                     outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
