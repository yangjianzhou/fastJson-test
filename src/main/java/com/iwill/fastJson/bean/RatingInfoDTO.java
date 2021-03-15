package com.iwill.fastJson.bean;

public class RatingInfoDTO {

    private Long reviewCount ;

    private Double average ;

    public Long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }
}
