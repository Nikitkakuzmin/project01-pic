package kz.nik.project01main.api;




import jakarta.servlet.http.HttpServletRequest;
import kz.nik.project01main.dto.FileDTO;
import kz.nik.project01main.dto.PictureDTO;
import kz.nik.project01main.dto.UserDTO;
import kz.nik.project01main.feign.FileFeignClient;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileController {


    private final FileFeignClient fileFeignClient;

    @GetMapping(value ="/pic")
    public ResponseEntity<List<PictureDTO>> getPictures(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        return fileFeignClient.getPictures(token);
    }



    @GetMapping(value = "/pic/{id}")
    public ResponseEntity<PictureDTO> getPicture(@PathVariable(name = "id") Long id,
                                                 HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        return fileFeignClient.getPicture(id, token);
    }

    @PostMapping(value ="pic/add-pic")
    public ResponseEntity<PictureDTO> addPicture(@RequestBody PictureDTO pictureDTO,
                                                 HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
       return fileFeignClient.addPicture(pictureDTO,token);
    }



    @PutMapping(value ="pic/update-pic")
    public ResponseEntity<PictureDTO> updatePicture(@RequestBody PictureDTO pictureDTO,
                                                    HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
       return fileFeignClient.updatePicture(pictureDTO,token);
    }

    @DeleteMapping(value = "pic/delete-pic/{id}")
    public ResponseEntity<Void> deletePicture(@PathVariable(name = "id") Long id,
                                              HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        return fileFeignClient.deletePicture(id, token);
    }

    @PostMapping(value = "/pic/file/upload")
    public ResponseEntity<FileDTO> uploadFile(@RequestPart("file") MultipartFile file,
                                              @RequestParam("picId") Long picId,
                                              HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        return fileFeignClient.uploadFile(file, picId, token);
    }

    @GetMapping(value = "/pic/file/download/{picId}/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(name = "picId") Long picId,
                                                          @PathVariable(name = "fileId") Long fileId,
                                                          HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        return fileFeignClient.downloadFile(picId, fileId, token);
    }


}

