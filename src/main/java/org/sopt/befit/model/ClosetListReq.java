package org.sopt.befit.model;

import lombok.Data;

@Data
public class ClosetListReq {
    private int closet_idx;
    private String name_korean;
    private String name_english;
    private String name;
    private String image_url;
    private String product_size;
    private Object measure;
}
