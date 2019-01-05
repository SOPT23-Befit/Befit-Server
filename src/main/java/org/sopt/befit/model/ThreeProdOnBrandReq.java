package org.sopt.befit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThreeProdOnBrandReq {
    private int idx;
    private String name_korean;
    private String name_english;
    private String gender;
    private String style1;
    private String style2;
    private int like_score;
    private String link;
    private String logo_url;
    private String mainpage_url;
    private String mainfeed_url;
    private int brand_like;
    private List<ProductReq> products;
}
