package kz.nik.project01pictureservice.service.impl;


import kz.nik.project01pictureservice.dto.PictureDto;
import kz.nik.project01pictureservice.dto.UserDto;
import kz.nik.project01pictureservice.exception.UserNotFoundException;
import kz.nik.project01pictureservice.feign.UserFeignClient;
import kz.nik.project01pictureservice.mapper.PictureMapper;
import kz.nik.project01pictureservice.model.Picture;
import kz.nik.project01pictureservice.repository.PictureRepository;
import kz.nik.project01pictureservice.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final UserFeignClient userFeignClient;
    private final PictureMapper pictureMapper;



    @Override
    public List<PictureDto> getPictures() {
        return pictureMapper.toDtoList(pictureRepository.findAll());
    }

    @Override
    public PictureDto getPicture(Long id) {
        return pictureMapper.toDto(pictureRepository.findById(id).orElse(null));
    }

    @Override
    public PictureDto addPicture(PictureDto pictureDto, String token) {
        ResponseEntity<UserDto> response = userFeignClient.getCurrentUser(token);
        UserDto userDTO = response.getBody();

        if (userDTO != null) {
            pictureDto.setDateCreated(LocalDateTime.now());
            return pictureMapper.toDto(pictureRepository.save(pictureMapper.toEntity(pictureDto)));
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public PictureDto updatePicture(PictureDto pictureDto, String token) {
        ResponseEntity<UserDto> response = userFeignClient.getCurrentUser(token);
        UserDto userDto = response.getBody();

        if (userDto != null) {
            return pictureMapper.toDto(pictureRepository.save(pictureMapper.toEntity(pictureDto)));
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public void deletePicture(Long id) {
        Picture deletePicture = pictureRepository.findById(id).orElse(null);
        if(deletePicture != null) {
            pictureRepository.delete(deletePicture);
        } else {
            /*throw new PictureNotFoundException("Picture not found");*/
        }
    }
}
