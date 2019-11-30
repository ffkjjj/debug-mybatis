package xyz.littlezhu.debugmybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("xyz.littlezhu.debugmybatis.data.dao")
public class DebugMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(DebugMybatisApplication.class, args);
    }

}
