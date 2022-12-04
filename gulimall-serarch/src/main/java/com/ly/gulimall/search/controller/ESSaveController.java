package com.ly.gulimall.search.controller;

import com.lt.gulimall.common.exception.BizExceptionCodeEnums;
import com.lt.gulimall.common.to.es.SkuEsModel;
import com.lt.gulimall.common.utils.R;
import com.ly.gulimall.search.service.ProductSaveService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName ESSaveContorller
 * @Description:
 * @Author lite
 * @Date 2022/12/4
 * @Version V1.0
 **/
@RestController
@RequestMapping("/search/save")
@Slf4j
public class ESSaveController {

    @Resource
    private ProductSaveService productSaveService;
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> list) throws IOException {
        Boolean b = false;
        try {
            b = productSaveService.productStatusUp(list);
        } catch (IOException e) {
            log.info("ESSaveController商品上架错误",e);
            return R.error(BizExceptionCodeEnums.PRODUCT_UP_EXCEPTION.getCode(),BizExceptionCodeEnums.PRODUCT_UP_EXCEPTION.getMessage());
        }
        if(b){
            //失败
            return R.error(BizExceptionCodeEnums.PRODUCT_UP_EXCEPTION.getCode(),BizExceptionCodeEnums.PRODUCT_UP_EXCEPTION.getMessage());
        }else{
            //成功
            return R.ok();
        }
    }
}
