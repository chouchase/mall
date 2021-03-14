package com.chouchase.service;

import com.chouchase.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    //将图片保存到指定路径的文件夹
    public String upload(MultipartFile file,String path);
}
