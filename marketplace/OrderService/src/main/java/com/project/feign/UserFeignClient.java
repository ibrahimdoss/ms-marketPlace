package com.project.feign;

import com.project.dto.UserInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "UserService", url = "http://127.0.0.1:8087")

@FeignClient(name = "UserService")
public interface UserFeignClient {

    @GetMapping("/user/info")
    UserInfoResponseDto getInfo(@RequestParam Long userId);

    //Yukarıda url vermek yerine eğer eureka var ise feignclient ile servisin ismi ve eureka maplenerek servisi buluyor.
    //Daha ayrıntılı şekilde araştır. Servis Discovery.
    //Euraka server spring cloudda olan bir proje.
    //Yani user, product ve order servisleri, euraka run olduğun da euraka servera bu üç servis client gibi bağlansın. ( bağlantı isimleri üzerinden)
    //Daha sonra diyelim user producta erismek istiyor, user ise erismek istediğinin adını bilsin.(product servisin)
    //Burada akıs su sekilde user bunun için önce euraka servera istek atar. Euraka ise productun hangi portu müsait ise onu dönerek cevaplar.
    //euraka server aslında load balancer islemi yapar.
}
