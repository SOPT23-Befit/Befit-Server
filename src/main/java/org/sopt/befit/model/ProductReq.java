package org.sopt.befit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sopt.befit.dto.Products;

import java.util.Date;

@Data
@AllArgsConstructor
public class ProductReq {
    private int idx;
    private String name;
    private String price;
    private String image_url;
    private String product_category;
    private String brand_idx;
    private Date date;
    private String link;
    private Object measure;
    private int like_score;
    //좋아요
    private int product_like;
    //한글이름
    private String brand_Korean_name;
}
