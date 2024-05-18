package com.project.product;

import com.project.feign.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserFeignClient userFeignClient;

    @PostMapping("/infoByNumber")
    public void save(@RequestHeader String phoneNumber) {
        userFeignClient.info(phoneNumber);
    }
}
