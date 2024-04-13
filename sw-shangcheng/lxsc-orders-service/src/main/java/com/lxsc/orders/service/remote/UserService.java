package com.lxsc.orders.service.remote;

import com.lxsc.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("UserService")
public interface UserService {
    @GetMapping("/getUserId")
    JsonResult<Long>getUserId(@RequestParam String token);
}
