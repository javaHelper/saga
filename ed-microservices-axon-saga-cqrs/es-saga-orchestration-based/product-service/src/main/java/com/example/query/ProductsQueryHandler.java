package com.example.query;

import com.example.core.data.ProductEntity;
import com.example.query.controller.ProductRestModel;
import com.example.repository.ProductsRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductsQueryHandler {
    
    @Autowired
    private ProductsRepository productsRepository;

    @QueryHandler
    public List<ProductRestModel> findProducts(FindProductsQuery query){
        List<ProductRestModel> productsRest = new ArrayList<>();
        List<ProductEntity> storedProducts = productsRepository.findAll();
        
        for (ProductEntity productEntity: storedProducts) {
            ProductRestModel productRestModel = new ProductRestModel();
            BeanUtils.copyProperties(productEntity, productRestModel);
            productsRest.add(productRestModel);
        }
        return productsRest;
    }
}
