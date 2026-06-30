package com.library.smart.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /**
     * 上传文件
     *
     * @param file   上传的文件
     * @param subDir 子目录名称
     * @return 文件相对路径，如 /uploads/books/uuid.jpg
     */
    String uploadFile(MultipartFile file, String subDir);

    /**
     * 删除文件
     *
     * @param filePath 文件相对路径，如 /uploads/books/uuid.jpg
     */
    void deleteFile(String filePath);
}
