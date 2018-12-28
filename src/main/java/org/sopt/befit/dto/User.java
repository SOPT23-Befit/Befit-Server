package org.sopt.befit.dto;

import lombok.Data;

@Data
public class User {
    private int idx;
    private String email;
    private String password;
    private String gender; //female, male
    private String name;
    private int brand1_idx;
    private int brand2_idx;
    private int height; //default 0
    private int weight; //default 0
    private String birthday;
    private String phone;
    private String post_number;
    private String home_address;
    private String detail_address;
}
