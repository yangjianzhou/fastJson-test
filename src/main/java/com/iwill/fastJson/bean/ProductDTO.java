package com.iwill.fastJson.bean;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {

    private long Id ;

    private String name ;

    private Date expireDate ;

    private String category ;

    private String country ;

    private RatingInfoDTO ratingInfo ;

    public RatingInfoDTO getRatingInfo() {
        return ratingInfo;
    }

    public void setRatingInfo(RatingInfoDTO ratingInfo) {
        this.ratingInfo = ratingInfo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
