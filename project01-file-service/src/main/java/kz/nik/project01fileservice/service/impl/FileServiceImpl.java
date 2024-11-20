package kz.nik.project01fileservice.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import kz.nik.project01fileservice.dto.UserDto;
import kz.nik.project01fileservice.feign.UserFeignClient;
import kz.nik.project01fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final MinioClient minioClient;
    private final UserFeignClient userFeignClient;
    @Value("${minio.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file,String token) {
        ResponseEntity<UserDto> response = userFeignClient.getCurrentUser(token);
        try {
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(bucket)
                            .object(file.getOriginalFilename())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return "file uploaded success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "some errors on uploading file";
    }

    public ByteArrayResource downloadFile(String fileName,String token) {
        ResponseEntity<UserDto> response = userFeignClient.getCurrentUser(token);
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs
                    .builder()
                    .bucket(bucket)
                    .object(fileName)
                    .build();

            InputStream stream = minioClient.getObject(getObjectArgs);
            byte[] byteArray = IOUtils.toByteArray(stream);
            stream.close();

            return new ByteArrayResource(byteArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
}
