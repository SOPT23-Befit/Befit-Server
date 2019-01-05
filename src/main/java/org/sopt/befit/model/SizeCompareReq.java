package org.sopt.befit.model;

import lombok.Data;

@Data
public class SizeCompareReq {

    private int product_category_index;
    private String product_size;
    private Object measure;
}
