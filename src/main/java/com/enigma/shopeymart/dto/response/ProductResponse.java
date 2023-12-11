package com.enigma.shopeymart.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductResponse {
    private String ProductId;
    private String ProductName;
    private  String description;
    private Integer stock;
    private Long price;
    private StoreResponse store;
}
