package kz.nik.project01pictureservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FileDto {
    private Long id;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private String path;
    private PictureDto pictureDto;
}
