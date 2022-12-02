package com.lt.gulimall.ware.feign;

import com.lt.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @ClassName ProductFeignService
 * @Description:
 * @Author lite
 * @Date 2022/11/30
 * @Version V1.0
 **/
@FeignClient("gulimall-product")
public interface ProductFeignService {

    @GetMapping("product/skuinfo/info/{skuId}")
    public R getSkuInfo(@PathVariable("skuId")Long skuId);
}
