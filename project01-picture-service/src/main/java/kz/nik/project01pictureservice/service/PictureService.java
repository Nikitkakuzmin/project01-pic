package kz.nik.project01pictureservice.service;

import kz.nik.project01pictureservice.dto.PictureDto;

import java.util.List;

public interface PictureService {
    List<PictureDto> getPictures();
    PictureDto getPicture(Long id);
    PictureDto addPicture(PictureDto pictureDto,String token );
    PictureDto updatePicture(PictureDto pictureDto,String token);
    void deletePicture(Long id);
}
