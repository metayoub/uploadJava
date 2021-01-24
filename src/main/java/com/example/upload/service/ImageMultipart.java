package com.example.upload.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;


public class ImageMultipart implements ImageSource {
    
    private final MultipartFile multipartFile;

    public ImageMultipart(MultipartFile multipartFile) {
        this.multipartFile = Objects.requireNonNull(multipartFile);
    }

    @Override
    public File asFile() throws IOException {
        String name = UUID.randomUUID().toString().replaceAll("-", "");
        File imageFile = Files.createTempFile(name, ".tmp").toFile();

        if (imageFile.exists() & imageFile.isFile()) {
            Files.write(Paths.get(imageFile.getAbsolutePath()), multipartFile.getBytes());
        }

        return imageFile;
    }
}
