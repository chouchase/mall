package com.chouchase.service.impl;

import com.chouchase.common.ServerResponse;
import com.chouchase.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
@Service
public class FileServiceImpl implements FileService {
    @Override
    public String upload(MultipartFile file, String path) {
        //获取源文件名
        String originName = file.getOriginalFilename();
        //获取文件扩展名
        String extensionName = StringUtils.getFilenameExtension(originName);
        //生成新的文件名
        String targetName = UUID.randomUUID().toString() + "." + extensionName;
        //生成文件目录
        File dir = new File(path);
        if(!dir.exists()){
            dir.setWritable(true);
            dir.mkdirs();
        }
        File targetFile = new File(path,targetName);
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return targetFile.getName();


    }
}
