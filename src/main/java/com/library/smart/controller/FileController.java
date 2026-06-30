package com.library.smart.controller;

import com.library.smart.common.Result;
import com.library.smart.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file,
                                 @RequestParam("subDir") String subDir) {
        String filePath = fileStorageService.uploadFile(file, subDir);
        return Result.success(filePath);
    }

    @DeleteMapping("/delete")
    public Result<Void> delete(@RequestParam("filePath") String filePath) {
        fileStorageService.deleteFile(filePath);
        return Result.success();
    }
}
