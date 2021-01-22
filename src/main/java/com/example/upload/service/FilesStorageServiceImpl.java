package com.example.upload.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import com.example.upload.model.JpgImage;
@Service
public class FilesStorageServiceImpl implements FilesStorageService {

  private final Path root = Paths.get("uploads");

  @Override
  public void init() {
    try {
      Files.createDirectory(root);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  @Override
    public void compress(ImageSource imageSource, String imageName) {
        try {
            // Path imagesPath = Files.createDirectories(Paths.get(imagesPathAsString));
            String compressedImageFileName = imageName + "." + JpgImage.EXTENSION;
            File compressedImageFile = this.root.resolve(compressedImageFileName)
                    .toFile();

            File imageSourceFile = imageSource.asFile();

            new JpgImage(imageSourceFile)
                    .compressTo(compressedImageFile);

            imageSourceFile.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

  @Override
  public Resource load(String filename) {
    try {
      Path file = root.resolve(filename + "." + JpgImage.EXTENSION);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(root.toFile());
  }

}