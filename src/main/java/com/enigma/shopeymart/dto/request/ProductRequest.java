package com.enigma.shopeymart.dto.request;


import com.enigma.shopeymart.dto.response.StoreResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductRequest {


    @NotBlank(message = "product id is required")
    private String ProductId;

    @NotBlank(message = "product name is required")
    private String ProductName;

    @NotBlank(message = "description is required")
    private  String description;

    @NotBlank(message = "stock is required")
    private Integer stock;

    @NotBlank(message = "price is required")
    private Long price;

    @NotBlank(message = "store id is required")
    private StoreResponse storeId;
}
