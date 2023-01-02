package com.xinnn.reggie.controller;

import com.xinnn.reggie.utils.Result;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@Data
@ConfigurationProperties(prefix = "file-save")
public class FileController {
    private String path;
    @PostMapping("/upload")
    public Result<String> fileUpload(MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        String hz = name.substring(name.lastIndexOf("."));
        name = (UUID.randomUUID().toString() + hz).replaceAll("-", "");
        File file1 = new File(path + name);
        file.transferTo(file1);
        return Result.success(name);
    }
    @GetMapping("/download")
    public void fileDownload(String name, HttpServletResponse response){
        try{
            InputStream inputStream = new FileInputStream(path + name);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.close();
            inputStream.close();
        }catch (Exception e){
            throw new RuntimeException("图片不存在");
        }
    }
}
