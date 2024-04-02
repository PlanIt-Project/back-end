package com.sideProject.PlanIT.common.modules;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class FileHandlerTest {

//    @Value("${spring.fileStorage.dir}")
//    private String fileStorageDir;
//
//    @Autowired
//    FileHandler fileHandler;
//
//    @Test
//    @DisplayName("파일을 저장합니다")
//    void saveFile() {
//
//        // given
//        File testFile = new File(fileStorageDir + "test.txt");
//
//        try {
//            FileWriter writer = new FileWriter(testFile);
//            writer.write("Hello, World!");
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        MultipartFile multipartFile = null;
//        try (InputStream input = new FileInputStream(testFile)){
//            multipartFile = new MockMultipartFile("file", testFile.getName(), "test/plain", input);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // when
//        String fileName = fileHandler.saveFile(multipartFile);
//
//        // then
//        File savedFile = new File(fileStorageDir + File.separator + fileName);
//        assertThat(savedFile.exists()).isTrue();
//    }
//
//    @Test
//    @DisplayName("저장된 파일을 가져옵니다")
//    void downloadFile() throws IOException {
//        // given
//        File testFile = new File(fileStorageDir + "test.txt");
//
//        try {
//            FileWriter writer = new FileWriter(testFile);
//            writer.write("Hello, World!");
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        MultipartFile multipartFile = null;
//        try (InputStream input = new FileInputStream(testFile)){
//            multipartFile = new MockMultipartFile("file", testFile.getName(), "test/plain", input);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String fileName = "";
//        try {
//            UUID uuid = UUID.randomUUID();
//            assert multipartFile != null;
//            fileName = uuid + multipartFile.getOriginalFilename();
//            File dest = new File(fileStorageDir + File.separator + fileName);
//            multipartFile.transferTo(dest);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // when
//        byte[] fileBytes = fileHandler.loadFile(fileName);
//
//        // then
//        assertThat(fileBytes).isNotNull();
//
//        String fileContent = new String(fileBytes);
//        assertThat(fileContent).isEqualTo("Hello, World!");
//    }


}
