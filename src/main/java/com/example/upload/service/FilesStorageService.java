package com.example.upload.service;

import org.springframework.core.io.Resource;

public interface FilesStorageService {
  public void init();

  void compress(ImageSource imageSource, String imageName);

  public Resource load(String filename);

  public void deleteAll();
}
