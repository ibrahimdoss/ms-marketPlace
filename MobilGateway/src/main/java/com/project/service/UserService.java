package com.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dto.UserInfoResponseDto;
import com.project.feign.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final StringRedisTemplate redisTemplate;
    private final UserFeignClient userFeignClient;


    public String userInfo(String phoneNumber){

        UserInfoResponseDto info = userFeignClient.info(phoneNumber);
        String token = UUID.randomUUID().toString();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String userJson = objectMapper.writeValueAsString(info);
            redisTemplate.opsForValue().set("user:" + token, userJson, 40, TimeUnit.SECONDS);
            //Redis'te 40 saniye boyunca kullanıcı bilgilerini saklar.
            return "user:" + token;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();

        }


    }
}
