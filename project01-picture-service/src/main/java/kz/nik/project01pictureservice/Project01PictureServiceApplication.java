package kz.nik.project01pictureservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Project01PictureServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Project01PictureServiceApplication.class, args);
    }

}
