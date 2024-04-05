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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
            String fileName = getUniqueFileName(file.getOriginalFilename());
            File dest = new File(fileStorageDir + File.separator + fileName);
            file.transferTo(dest);

            String fileExtension = getFileExtension(fileName);
            if (isImageFile(fileExtension)) {
                return "/image/" + fileName;
            } else {
                return "/file/" + fileName;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            return "이미지 업로드 오류 발생";
        }
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

    private String getUniqueFileName(String fileName) {
        return UUID.randomUUID() + fileName;
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex > 0) {
            return fileName.substring(lastIndex + 1);
        }
        return "";
    }

    private boolean isImageFile(String fileExtension) {
        // 이미지 파일 확장자 리스트
        List<String> imageExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");
        return imageExtensions.contains(fileExtension.toLowerCase());
    }

}
