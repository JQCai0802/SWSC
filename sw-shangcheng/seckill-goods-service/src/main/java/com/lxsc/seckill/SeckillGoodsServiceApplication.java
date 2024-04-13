package com.lxsc.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages={"com.lxsc.seckill.goods.mapper"})
public class SeckillGoodsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillGoodsServiceApplication.class, args);
    }

}
