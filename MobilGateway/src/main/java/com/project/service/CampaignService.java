package com.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dto.UserInfoResponseDto;
import com.project.feign.CampaignFeignClient;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final RedissonClient redissonClient;
    private final CampaignFeignClient campaignFeignClient;
    private final StringRedisTemplate redisTemplate;

    public String getOffer(Long offerId, String accessToken){
        // StringRedisTemplate -> Redis ile çalışırken String veri tipi ile çalışmamızı sağlar.

        try {
            String user = redisTemplate.opsForValue().get(accessToken);
            ObjectMapper objectMapper = new ObjectMapper();
            UserInfoResponseDto userInfoResponseDto = objectMapper.readValue(user, UserInfoResponseDto.class);
            String phoneNumber = userInfoResponseDto.phoneNumber();
            RLock lock = redissonClient.getLock("campaign:" +phoneNumber);
            //RLock -> Redisson'da bir anahtarın kilidini açmak ve kilitlemek için kullanılan bir araçtır.
            //Bunun kullanılma sebebi aynı anda aynı kaynağa erişim sağlanmasını engellemektir.
            boolean tryLock = lock.tryLock(2, 60, TimeUnit.SECONDS);
            if (tryLock) {
                try {
                    System.out.println("başarılı");

                    return campaignFeignClient.getOffer(offerId);

                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println("Başka bir istek zaten işlenmekte.");
                throw new RuntimeException();

            }


        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
