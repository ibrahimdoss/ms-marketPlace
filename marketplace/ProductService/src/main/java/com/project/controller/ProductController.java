package com.project.controller;

import com.project.dto.ProductCountUpdateRequestDto;
import com.project.dto.ProductInfoResponseDto;
import com.project.dto.ProductResponseByCategoryDto;
import com.project.dto.ProductSaveRequestDto;
import com.project.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    public void save(@RequestBody ProductSaveRequestDto productSaveReqestDto) {
        productService.save(productSaveReqestDto);
    }

    @PutMapping("/updateProductCount")
    public void updateProductCount(@RequestBody ProductCountUpdateRequestDto productCountUpdateRequestDto) {

        productService.updateProductCount(productCountUpdateRequestDto);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam Long productId) {
        productService.delete(productId);
    }

    @GetMapping("/productListByCategory/{category}")
    public ResponseEntity<List<ProductResponseByCategoryDto>> productListByCategory(@PathVariable String category){
        Optional<List<ProductResponseByCategoryDto>> productResponseByCategoryDtos = productService.productListByCategory(category);

        if (productResponseByCategoryDtos.isPresent()){
            List<ProductResponseByCategoryDto> productResponseByCategoryDtos1 = productResponseByCategoryDtos.get();
            return ResponseEntity.ok(productResponseByCategoryDtos1);
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping("/info")
    public ResponseEntity<ProductInfoResponseDto> info(@RequestParam Long id){
        Optional<ProductInfoResponseDto> productInfoResponseDto = productService.info(id);

        if (productInfoResponseDto.isPresent()){
            ProductInfoResponseDto productResponseByCategoryDtos1 = productInfoResponseDto.get();
            return ResponseEntity.ok(productResponseByCategoryDtos1);
        }
        return ResponseEntity.notFound().build();
    }
}
