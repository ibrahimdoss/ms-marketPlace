package com.project.resttemplate;

import com.project.dto.ProductCountUpdateRequestDto;
import com.project.dto.ProductInfoResponseDto;
import com.project.dto.UserInfoResponseDto;
import com.project.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ProductClient {

    private final RestTemplate restTemplate;

    public ProductInfoResponseDto getProductInfoDto(Long productId){
        HttpHeaders httpHeaders = new HttpHeaders();

        HttpEntity<UserInfoResponseDto> httpEntity = new HttpEntity(httpHeaders);

        String url = "http://127.0.0.1:8089/product/info?id="+productId;
        try{
            ResponseEntity<ProductInfoResponseDto> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, ProductInfoResponseDto.class);
            return responseEntity.getBody();
        }catch (Exception e){
            throw new BusinessException("Product verisini çekerken bir hata ile karşılaşıldı.");
        }
    }

    public void updateProductCount(ProductCountUpdateRequestDto productCountUpdateRequestDto) {

        HttpHeaders httpHeaders = new HttpHeaders();

        HttpEntity<ProductCountUpdateRequestDto> httpEntity = new HttpEntity<>(productCountUpdateRequestDto, httpHeaders);

        String url = "http://127.0.0.1:8089/product/updateProductCount";
        try {
            restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Void.class);

        } catch (Exception e) {
            throw new BusinessException("Product update edilemedi.");
        }

    }
}
