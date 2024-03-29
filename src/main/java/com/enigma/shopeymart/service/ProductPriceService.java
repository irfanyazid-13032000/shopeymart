package com.enigma.shopeymart.service;

import com.enigma.shopeymart.dto.request.ProductRequest;
import com.enigma.shopeymart.entity.ProductPrice;

public interface ProductPriceService {
    ProductPrice create(ProductPrice productPrice);

    ProductPrice getById(String id);

    ProductPrice findProductIsActive(String productId,Boolean active);

}
