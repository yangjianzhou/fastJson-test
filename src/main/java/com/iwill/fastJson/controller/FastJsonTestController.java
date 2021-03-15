package com.iwill.fastJson.controller;

import com.google.gson.Gson;
import com.iwill.fastJson.bean.ProductDTO;
import com.iwill.fastJson.service.FastJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("fastJson-test")
public class FastJsonTestController {

    @Autowired
    private FastJsonService fastJsonService ;

    @GetMapping("get-product-id")
    public ProductDTO getProductById(){
        ProductDTO productDTO = fastJsonService.getProductByName(123L,"name");
        return productDTO ;
    }

    @PostMapping("add-product")
    public String addProduct(@RequestBody ProductDTO productDTO){
        return new Gson().toJson(productDTO);
    }
}
