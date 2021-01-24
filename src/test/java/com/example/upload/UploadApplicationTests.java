package com.example.upload;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.upload.model.ResponseMessage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.upload.service.FilesStorageServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest()
class UploadApplicationTests {

        private final Path root = Paths.get("uploads");

        @Autowired
        public MockMvc mockMvc;

        @Autowired
        public FilesStorageServiceImpl filesStorageServiceImpl;
        
        @Autowired
        private ObjectMapper objectMapper;
	
	@Test
        public void shouldUploadAndCompressAndDownloadJpgFile() throws Exception {

		Resource fileResource = new ClassPathResource("22.jpg");

                assertNotNull(fileResource);    

                Path sourceImagePath = Paths.get(fileResource.getFile().getAbsolutePath());

                MockMultipartFile sourceImageMultipartFile = new MockMultipartFile( 
                       "file",fileResource.getFilename(),
                        MediaType.MULTIPART_FORM_DATA_VALUE,
                        fileResource.getInputStream());  

                assertNotNull(sourceImageMultipartFile);

                ResultActions resultActions = mockMvc.perform(multipart("/upload")
                        .file(sourceImageMultipartFile))
                        .andExpect(status().isOk());

                MvcResult result = resultActions.andReturn();
                String  response = result.getResponse().getContentAsString();

                assertNotNull(response);

                ResponseMessage message = objectMapper.readValue(response, ResponseMessage.class);
                String fileName = message.getUrl();
                
                assertNotNull(fileName);
                
                Path compressedImagePath = root.resolve(fileName + ".jpg");

                assertThat(Files.exists(compressedImagePath)).isTrue();

                assertThat(Files.size(compressedImagePath)).isLessThan(Files.size(sourceImagePath));

                MvcResult resultUpl = mockMvc.perform(MockMvcRequestBuilders.get("/file/" + fileName)
                        .contentType(MediaType.IMAGE_JPEG))
                .andExpect(status().isOk())
                .andReturn();

                assertThat(resultUpl.getResponse().getContentType()).isEqualTo(MediaType.IMAGE_JPEG.toString());
        } 
}
