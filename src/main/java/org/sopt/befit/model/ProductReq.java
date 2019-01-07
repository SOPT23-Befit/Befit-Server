package org.sopt.befit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReq {
    private int idx;
    private String name;
    private String price;
    private String image_url;
    private int product_category_index;
    private int brand_idx;
    private Date date;
    private String link;
    private Object measure;
    private int like_score;
    //좋아요 플래그
    private int product_like;
    //한글이름
    private String name_korean;
    //영어이름
    private String name_english;
}
