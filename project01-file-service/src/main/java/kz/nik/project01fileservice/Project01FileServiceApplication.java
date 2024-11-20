package kz.nik.project01fileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Project01FileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Project01FileServiceApplication.class, args);
    }

}
