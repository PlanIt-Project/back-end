package com.sideProject.PlanIT.domain.file.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public interface FileService {
    public String saveFile(MultipartFile file);
    public Resource sendFile(String fileName);
}
