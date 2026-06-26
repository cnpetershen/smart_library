package com.library.smart.service.impl;

import com.library.smart.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final String UPLOAD_BASE_DIR = System.getProperty("user.dir")
            + File.separator + "uploads";

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(UPLOAD_BASE_DIR, "books"));
            log.info("上传目录初始化完成: {}", Paths.get(UPLOAD_BASE_DIR, "books"));
        } catch (IOException e) {
            log.error("上传目录初始化失败", e);
            throw new RuntimeException("无法创建上传目录", e);
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String subDir) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 提取原始文件扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 生成 UUID 文件名
        String filename = UUID.randomUUID().toString() + extension;

        try {
            // 确保子目录存在
            Path targetDir = Paths.get(UPLOAD_BASE_DIR, subDir);
            Files.createDirectories(targetDir);

            // 保存文件
            Path targetPath = targetDir.resolve(filename);
            file.transferTo(targetPath.toFile());

            String relativePath = "/uploads/" + subDir + "/" + filename;
            log.info("文件上传成功: {} (大小: {} bytes)", relativePath, file.getSize());
            return relativePath;
        } catch (IOException e) {
            log.error("文件上传失败: {}", originalFilename, e);
            throw new RuntimeException("文件上传失败", e);
        }
    }

    @Override
    public void deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return;
        }

        // 去掉开头的 / 以构建系统路径
        String relativePath = filePath.startsWith("/") ? filePath.substring(1) : filePath;
        Path path = Paths.get(System.getProperty("user.dir"), relativePath);

        try {
            boolean deleted = Files.deleteIfExists(path);
            if (deleted) {
                log.info("文件删除成功: {}", filePath);
            } else {
                log.warn("文件不存在，忽略删除: {}", filePath);
            }
        } catch (IOException e) {
            log.error("文件删除失败: {}", filePath, e);
            throw new RuntimeException("文件删除失败: " + filePath, e);
        }
    }
}
