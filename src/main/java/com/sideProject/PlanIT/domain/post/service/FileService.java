package com.sideProject.PlanIT.domain.post.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {
    @Value("${fileStorage.dir}")
    private String fileStorageDir;

    public String saveFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            File dest = new File(fileStorageDir + File.separator + fileName);
            file.transferTo(dest);

            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "이미지 업로드 오류 발생";
        }
    }
}
