package kz.nik.project01pictureservice.service;

import kz.nik.project01pictureservice.dto.FileDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileDto uploadFile(Long pictureId, MultipartFile file);
    ResponseEntity<ByteArrayResource> downloadFile(Long fileId, Long pictureId);
}
