package com.project.product;

import com.project.dto.ProductSaveReqestDto;
import com.project.feign.ProductFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {


    private final ProductFeignClient productFeignClient;

    @PostMapping("/save")
    public void save(@RequestBody ProductSaveReqestDto productSaveReqestDto) {
        productFeignClient.save(productSaveReqestDto);
    }
}
