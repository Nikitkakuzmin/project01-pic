package kz.nik.project01pictureservice.feign;

import kz.nik.project01pictureservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "file-feign-client", url = "${file.feign.client.url}", configuration = FeignConfig.class)
public interface FileFeignClient {

    @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadFile(@RequestPart("file") MultipartFile file);

    @GetMapping(value = "/file/download/{fileName}")
    ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(name = "fileName") String fileName);
}