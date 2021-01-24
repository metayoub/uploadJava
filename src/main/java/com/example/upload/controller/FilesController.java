package com.example.upload.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.example.upload.service.ImageMultipart;
import com.example.upload.model.ResponseMessage;
import com.example.upload.service.FilesStorageService;

@Controller
@CrossOrigin("http://localhost:4200")
public class FilesController {

  private final FilesStorageService storageService;

  @Autowired
  public FilesController(FilesStorageService storageService) {
    this.storageService = storageService;
  }

  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";
    String fileName = UUID.randomUUID().toString().replaceAll("-", "");
    try {
      storageService.compress(new ImageMultipart(file), fileName);
      message = "Uploaded the file successfully: " + fileName;
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, fileName));
    } catch (Exception e) {
      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
    }
  }

  @GetMapping("/file/{filename}")
  @ResponseBody
  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
    try {
      Resource file = storageService.load(filename);
      return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

  }

}