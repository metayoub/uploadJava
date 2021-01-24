package com.example.upload;

import javax.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.upload.service.FilesStorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication
public class UploadApplication implements CommandLineRunner {
	@Resource
	FilesStorageService storageService;
	  
	private static final Logger log = LoggerFactory.getLogger(UploadApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(UploadApplication.class, args);
		log.info("\n---------------------start--------------------------------\n\t");
	}

	@Override
	public void run(String... arg) throws Exception {
		storageService.deleteAll();
		storageService.init();
		log.info("\n---------------------run----------------------------------\n\t");
	}

}
