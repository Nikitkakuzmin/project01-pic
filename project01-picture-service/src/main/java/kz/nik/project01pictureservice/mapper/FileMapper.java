package kz.nik.project01pictureservice.mapper;

import kz.nik.project01pictureservice.dto.FileDto;
import kz.nik.project01pictureservice.model.File;
import lombok.Getter;
import org.mapstruct.Mapper;


import java.util.List;


@Mapper(componentModel = "spring")
public interface FileMapper {
    FileDto toDto(File file);
    File toEntity(FileDto fileDto);
    List<FileDto> toDtoList(List<File> list);
}
