package kz.nik.project01pictureservice.service.impl;

import kz.nik.project01pictureservice.dto.FileDto;
import kz.nik.project01pictureservice.dto.PictureDto;
import kz.nik.project01pictureservice.feign.FileFeignClient;
import kz.nik.project01pictureservice.mapper.FileMapper;
import kz.nik.project01pictureservice.repository.FileRepository;
import kz.nik.project01pictureservice.service.FileService;
import kz.nik.project01pictureservice.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileFeignClient fileFeignClient;
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final PictureService pictureService;

    @Override
    public FileDto uploadFile(Long pictureId, MultipartFile file) {
        PictureDto pictureDto = pictureService.getPicture(pictureId);
        if (pictureDto != null) {
            String response = fileFeignClient.uploadFile(file);
            if (response.equals("File Uploaded Successfully!")) {
                FileDto fileDTO = FileDto
                        .builder()
                        .pictureDto(pictureDto)
                        .path(file.getOriginalFilename())
                        .build();
                return fileMapper.toDto(fileRepository.save(fileMapper.toEntity(fileDTO)));
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<ByteArrayResource> downloadFile(Long fileId, Long pictureId) {
        FileDto fileDto = fileMapper.toDto(fileRepository.findById(fileId).orElse(null));
        if (fileDto != null && fileDto.getPictureDto().getId().equals(pictureId)) {
            return fileFeignClient.downloadFile(fileDto.getPath());
        }
        return null;
    }
}
