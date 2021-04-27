package com.wyh.game_platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wyh.game_platform.dao")
public class GamePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(GamePlatformApplication.class, args);
    }

}
