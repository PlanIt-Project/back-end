package com.sideProject.PlanIT.common.modules;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileHandler {
    @Value("${fileStorage.dir}")
    private String fileStorageDir;

    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String fileName = file.getOriginalFilename();
            File dest = new File(fileStorageDir + File.separator + fileName);
            file.transferTo(dest);

            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "이미지 업로드 오류 발생";
        }
    }
}
