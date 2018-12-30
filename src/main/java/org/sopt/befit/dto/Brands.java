package org.sopt.befit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor

@Entity

@Table(name = "brand")
@Data
public class Brands {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int idx;

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
