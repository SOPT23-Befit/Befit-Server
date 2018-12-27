package org.sopt.befit.model;

import lombok.Data;

@Data
public class UserupdateReq {
    private int brand1_idx; //brand select
    private int brand2_idx; //brand select
    private String phone;
    private String post_number;
    private String home_address;
    private String detail_address;

    public boolean is_brand(){
        return(this.brand1_idx != 0 && this.brand2_idx !=0 ); //brand 참조
    }

    public boolean is_combineForm(){
        return(this.phone!=null && this.post_number!= null && this.home_address !=null && this.detail_address !=null); //combineForm 참조
    }

}

