package kz.nik.project01main.feign;

import kz.nik.project01main.config.FeignConfig;
import kz.nik.project01main.dto.FileDTO;
import kz.nik.project01main.dto.PictureDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "file-feign-client", url = "${file.feign.client.url}", configuration = FeignConfig.class)

public interface FileFeignClient {
    @GetMapping(value = "/pic")
    ResponseEntity<List<PictureDTO>> getPictures(@RequestHeader("Authorization") String token);

    @GetMapping(value = "/pic/{id}")
    ResponseEntity<PictureDTO> getPicture(@PathVariable(name = "id") Long id,
                                                 @RequestHeader("Authorization") String token);

    @PostMapping(value = "/pic/add-pic")
    ResponseEntity<PictureDTO> addPicture(@RequestBody PictureDTO pictureDTO,
                                                 @RequestHeader("Authorization") String token);

    @PutMapping(value ="/pic/update-pic")
    ResponseEntity<PictureDTO> updatePicture(@RequestBody PictureDTO pictureDTO,
                                                    @RequestHeader("Authorization") String token);

    @DeleteMapping(value = "/pic/delete-pic/{id}")
    ResponseEntity<Void> deletePicture(@PathVariable(name = "id") Long id,
                                              @RequestHeader("Authorization") String token);

    @PostMapping(value = "/pic/file/upload")
    ResponseEntity<FileDTO> uploadFile(@RequestPart("file") MultipartFile file,
                                       @RequestParam("picId") Long picId,
                                       @RequestHeader("Authorization") String token);

    @GetMapping(value = "/pic/file/download/{picId}/{fileId}")
    ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(name = "picId") Long picId,
                                                          @PathVariable(name = "fileId") Long fileId,
                                                          @RequestHeader("Authorization") String token);


}