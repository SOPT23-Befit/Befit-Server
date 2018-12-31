package org.sopt.befit.model;

import lombok.Data;

@Data
public class ClosetReq {
    private int closet_idx;
    private String name_korean;
    private String name_english;
    private String name;
    private String image_url;
    private int product_category_index;
    private String product_size;
    private Object measure;
}
