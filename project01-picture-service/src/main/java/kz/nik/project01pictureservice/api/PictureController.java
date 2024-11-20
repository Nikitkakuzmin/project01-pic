package kz.nik.project01pictureservice.api;

import jakarta.servlet.http.HttpServletRequest;
import kz.nik.project01pictureservice.client.AuthClient;
import kz.nik.project01pictureservice.dto.FileDto;
import kz.nik.project01pictureservice.dto.PictureDto;
import kz.nik.project01pictureservice.feign.UserFeignClient;
import kz.nik.project01pictureservice.service.FileService;
import kz.nik.project01pictureservice.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/pic")
public class PictureController {
    private final PictureService pictureService;
    private final FileService fileService;
    private final AuthClient authClient;

    @GetMapping
    public ResponseEntity<List<PictureDto>> getPictures(@RequestHeader("Authorization") String token) {
        if (!authClient.isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<PictureDto> pictures = pictureService.getPictures();
        return ResponseEntity.ok(pictures);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<PictureDto> getPicture(@PathVariable(name = "id") Long id,
                                                 @RequestHeader("Authorization") String token) {
        if (!authClient.isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        PictureDto picture = pictureService.getPicture(id);
        return ResponseEntity.ok(picture);
    }

    @PostMapping(value ="/add-pic")
    public ResponseEntity<PictureDto> addPicture(@RequestBody PictureDto pictureDto,
                                                 @RequestHeader("Authorization") String token) {
        if (!authClient.isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        PictureDto addedPicture = pictureService.addPicture(pictureDto, token);
        return ResponseEntity.ok(addedPicture);
    }

    @PutMapping(value ="/update-pic")
    public ResponseEntity<PictureDto> updatePicture(@RequestBody PictureDto pictureDto,
                                                    @RequestHeader("Authorization") String token) {
        if (!authClient.isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        PictureDto updatedPicture = pictureService.updatePicture(pictureDto, token);
        return ResponseEntity.ok(updatedPicture);
    }

    @DeleteMapping(value = "/delete-pic/{id}")
    public ResponseEntity<Void> deletePicture(@PathVariable(name = "id") Long id,
                                              @RequestHeader("Authorization") String token) {
        if (!authClient.isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        pictureService.deletePicture(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/file/upload")
    public ResponseEntity<FileDto> uploadFile(@RequestPart("file") MultipartFile file,
                                              @RequestParam("picId") Long picId,
                                              @RequestHeader("Authorization") String token) {
        if (!authClient.isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        FileDto uploadedFile = fileService.uploadFile(picId, file);
        return ResponseEntity.ok(uploadedFile);
    }

    @GetMapping(value = "/file/download/{picId}/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(name = "picId") Long picId,
                                                          @PathVariable(name = "fileId") Long fileId,
                                                          @RequestHeader("Authorization") String token) {
        if (!authClient.isAuthenticated(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ResponseEntity<ByteArrayResource> response = fileService.downloadFile(fileId, picId);
        return response;
    }

}