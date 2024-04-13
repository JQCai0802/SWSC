package com.lxsc.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages={"com.lxsc.seckill.orders.mapper"})
public class SeckillOrdersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillOrdersServiceApplication.class, args);
    }

}
