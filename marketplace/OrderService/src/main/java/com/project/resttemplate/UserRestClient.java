package com.project.resttemplate;

import com.project.dto.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class UserRestClient {

    public UserInfoResponseDto getInfo(Long userId) {

        RestClient restClient = RestClient.create();
        String uriBase = "http://127.0.0.1:8087";

        return restClient.get()
                .uri(uriBase + "/user/info?userId=" + userId)
                .retrieve()
                .body(UserInfoResponseDto.class);


    }
}
