package com.sideProject.PlanIT.domain.file.service;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{
    @Value("${spring.fileStorage.dir}")
    private String fileStorageDir;

    @Override
    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + file.getOriginalFilename();
            File dest = new File(fileStorageDir + File.separator + fileName);
            file.transferTo(dest);

            return fileName;
        } catch (IOException e) {
            return "이미지 업로드 오류 발생";
        }
    }

    @Override
    public Resource sendFile(String fileName) {
        try {
            File file = new File(fileStorageDir + File.separator + fileName);

            Resource resource = new FileSystemResource(file);

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new CustomException(ErrorCode.FILE_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FILE_NOT_FOUND);
        }
    }
}
