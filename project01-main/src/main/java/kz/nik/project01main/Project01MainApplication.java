package kz.nik.project01main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Project01MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(Project01MainApplication.class, args);
    }

}
