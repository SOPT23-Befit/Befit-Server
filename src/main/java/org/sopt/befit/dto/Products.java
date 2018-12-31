package org.sopt.befit.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Products {
    private int idx;
    private String name;
    private String price;
    private String image_url;
    private int product_category_index;
    private int brand_idx;
    private String link;
    private Object measure;
    private Date date;
    private int like_score;
}
