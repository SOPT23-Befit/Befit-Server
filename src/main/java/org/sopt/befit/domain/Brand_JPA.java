package org.sopt.befit.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity

@Table(name = "brand")
public class Brand_JPA {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int idx;

    //column

//    @Column()
    private String name_korean;
    private String name_english;
    private String gender;
    private String style1;
    private String style2;
    private String link;
    private String logo;
    private double score;
    private int likeFlag;

}
