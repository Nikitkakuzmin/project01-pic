package kz.nik.project01pictureservice.feign;

import kz.nik.project01pictureservice.config.FeignConfig;
import kz.nik.project01pictureservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-feign-client",url = "http://localhost:8001",configuration =  FeignConfig.class)
public interface UserFeignClient {
    @GetMapping(value ="/user/current")
    ResponseEntity<UserDto> getCurrentUser(@RequestHeader("Authorization") String token);
}
