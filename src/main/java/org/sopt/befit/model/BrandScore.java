package org.sopt.befit.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BrandScore {
    private double score;

    public BrandScore(double score) {
        this.score = score;
    }
}
