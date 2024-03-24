package com.sideProject.PlanIT.common.modules;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
@Slf4j
public class FileHandler {
    @Value("${spring.fileStorage.dir}")
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

    public byte[] loadImage(String imageName) throws IOException {
        String allPath = fileStorageDir + File.separator + imageName;
        return readFileBytes(allPath);
    }

    public byte[] loadFile(String fileName) throws IOException {
        String allPath = fileStorageDir + File.separator + fileName;
        return readFileBytes(allPath);
    }

    private byte[] readFileBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                return IOUtils.toByteArray(fileInputStream);
            }
        } else {
            throw new CustomException("파일이 존재하지 않습니다.", ErrorCode.FILE_NOT_FOUND);
        }
    }
}
