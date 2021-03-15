package com.iwill.fastJson.service;

import com.iwill.fastJson.bean.ProductDTO;
import com.iwill.fastJson.bean.RatingInfoDTO;
import com.iwill.fastJson.constant.RedisConstants;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FastJsonService {

    @Cacheable(cacheNames = RedisConstants.LIVE_TOP_PRODUCT_CN, unless = "#result == null", key = "#p0 +'" + "_" + "'+#p1")
    public ProductDTO getProductByName(long id, String name) {
        return buildProductDTO(id, name);
    }

    private ProductDTO buildProductDTO(long id, String name) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategory("daily");
        productDTO.setExpireDate(new Date());
        productDTO.setName(name);
        productDTO.setId(id);
        //productDTO.setCountry("China");

        RatingInfoDTO ratingInfo = new RatingInfoDTO();
        ratingInfo.setAverage(5.5);
        ratingInfo.setReviewCount(123243L);

       // productDTO.setRatingInfo(ratingInfo);
        return productDTO;
    }
}
