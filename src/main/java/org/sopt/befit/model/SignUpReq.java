package org.sopt.befit.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpReq {
    private String email;
    private String password;
    private String gender; //female, male
    private String name;
    private int brand1_idx; //brand select
    private int brand2_idx; //brand select
    private String birthday;
}
