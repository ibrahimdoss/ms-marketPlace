package com.project.service;

import com.project.dto.ProductCountUpdateRequestDto;
import com.project.dto.ProductInfoResponseDto;
import com.project.dto.ProductResponseByCategoryDto;
import com.project.dto.ProductSaveRequestDto;
import com.project.exception.BusinessException;
import com.project.model.ProductEntity;
import com.project.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;

        this.modelMapper = modelMapper;
    }

    public void save(ProductSaveRequestDto productSaveReqestDto){
        ProductEntity product =  modelMapper.map(productSaveReqestDto, ProductEntity.class);
        product.setCategory(productSaveReqestDto.getCategory());
        product.setName(productSaveReqestDto.getName());
        saveProduct(product);

    }

    public void updateProductCount(ProductCountUpdateRequestDto productCountUpdateRequestDto){

        Optional<ProductEntity> productOptional = productRepository.findById(productCountUpdateRequestDto.id());
        ProductEntity product = productOptional.orElseThrow(BusinessException::new);
        product.setNumberOfProduct(productCountUpdateRequestDto.numberOfProduct());
        saveProduct(product);

    }

    public void saveProduct(ProductEntity product) {
        productRepository.save(product);


    }

    public ProductEntity findProductById(Long id){
        return productRepository.findById(id).get();
    }


    public void delete(Long productId) {
        ProductEntity product = productRepository.findById(productId).get();
        productRepository.delete(product);

    }

    public Optional<ProductInfoResponseDto> info(Long productId) {
        Optional<ProductEntity> productOptional = productRepository.findById(productId);
        ProductEntity product = productOptional.orElseThrow(BusinessException::new);
        return Optional.ofNullable(modelMapper.map(product, ProductInfoResponseDto.class));

    }
    @Cacheable(value = "productsByCategory3", key = "#category", cacheManager = "cManager")
    public Optional<List<ProductResponseByCategoryDto>> productListByCategory(String category) {
        List<ProductEntity> allByCategory = productRepository.findAllByCategory(category);

        List<ProductResponseByCategoryDto> list = allByCategory.stream().map(product -> {

            return modelMapper.map(product, ProductResponseByCategoryDto.class);

        }).toList();

        return Optional.ofNullable(list);

    }


    @CachePut(value = "productsByCategory3", key = "#category", cacheManager = "cManager")
    public Optional<List<ProductResponseByCategoryDto>> productListByCategoryUpdate(String category) {
        List<ProductEntity> allByCategory = productRepository.findAllByCategory(category);

        List<ProductResponseByCategoryDto> list = allByCategory.stream().map(product -> {

            return modelMapper.map(product, ProductResponseByCategoryDto.class);

        }).toList();

        return Optional.ofNullable(list);

    }

    @CacheEvict(value = "productsByCategory2", key = "#category")
    public void delete(){
        System.out.println();
    }
}
