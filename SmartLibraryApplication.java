package com.library.smart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.library.smart.mapper")
public class SmartLibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartLibraryApplication.class, args);
    }
}
