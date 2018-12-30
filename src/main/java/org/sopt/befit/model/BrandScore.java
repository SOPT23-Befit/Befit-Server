package org.sopt.befit.model;

import lombok.Data;

@Data
public class BrandScore {
    private double score;

    public BrandScore() {
    }

    public BrandScore(double score) {
        this.score = score;
    }
}
