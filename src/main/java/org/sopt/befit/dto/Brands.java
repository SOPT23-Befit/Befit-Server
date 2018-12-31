package org.sopt.befit.dto;


import lombok.Data;


@Data
public class Brands {
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
    private int likeFlag;
}
