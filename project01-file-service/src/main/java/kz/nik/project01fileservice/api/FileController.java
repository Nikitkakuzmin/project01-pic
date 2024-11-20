package kz.nik.project01fileservice.api;

import kz.nik.project01fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/file/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestHeader("Authorization") String token) {

        return fileService.uploadFile(file,token);
    }

    @GetMapping(value = "/file/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(name = "fileName") String fileName,
                                                          @RequestHeader("Authorization") String token){
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\"" + fileName + "\"")
                .body(fileService.downloadFile(fileName,token));
    }
}