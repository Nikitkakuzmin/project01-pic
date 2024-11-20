package kz.nik.project01pictureservice.mapper;

import kz.nik.project01pictureservice.dto.PictureDto;
import kz.nik.project01pictureservice.model.Picture;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;


import java.util.List;

@Mapper(componentModel = "spring")
public interface PictureMapper {

    PictureDto toDto(Picture picture);
    Picture toEntity(PictureDto pictureDto);

    List<PictureDto> toDtoList(List<Picture> pictures);
}
