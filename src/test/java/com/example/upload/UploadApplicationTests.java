package com.example.upload;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.upload.service.FilesStorageServiceImpl;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest()
class UploadApplicationTests {

	private final Path root = Paths.get("uploads");

    @Autowired
    public MockMvc mockMvc;

    @Autowired
	public FilesStorageServiceImpl filesStorageServiceImpl;
	
	@Test
    public void shouldUploadAndCompressJpgFile() throws Exception {
		
		Path sourceImagePath = Paths.get(getClass().getResource("/22.jpg").toURI());
		
		assertThat(Files.exists(sourceImagePath))
				.isTrue();
				
        MockMultipartFile sourceImageMultipartFile =
                new MockMultipartFile("file", "/22.jpg", "image/jpg",
                        Files.readAllBytes(sourceImagePath));

        mockMvc.perform(multipart("/upload")
                .file(sourceImageMultipartFile))
                .andExpect(status().isOk());

        Path compressedImagePath = root.resolve("22.jpg");

        assertThat(Files.exists(compressedImagePath))
                .isTrue();

        assertThat(Files.size(compressedImagePath))
                .isLessThan(Files.size(sourceImagePath));
    }
	@Test
	void contextLoads() {
	}

}
