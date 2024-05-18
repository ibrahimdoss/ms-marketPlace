package com.project.feign;
import com.project.dto.UserInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "userService")
public interface UserFeignClient {


    @GetMapping("/user/infoByNumber")
    UserInfoResponseDto info(@RequestParam String phoneNumber);
}
