package org.sopt.befit.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordFind {
    private int userIdx;
    private String email;
    private String birthday;
    private String name;
    private String password;

    public boolean is_finding_content(){
        return(this.email!=null && this.birthday !=null && this.name!=null);
    }

    public boolean is_password(){
        return(this.password != null &&  this.userIdx != 0);
    }
}
