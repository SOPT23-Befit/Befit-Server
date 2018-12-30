package org.sopt.befit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sopt.befit.dto.Products;

@Data
@AllArgsConstructor
public class ProductReq {
    private Products products;
    private String brand_Korean_name;
    private String brand_English_name;
    private boolean product_like;
}
