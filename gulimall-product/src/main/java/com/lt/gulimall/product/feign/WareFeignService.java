package com.lt.gulimall.product.feign;

import com.lt.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @ClassName WareFeignService
 * @Description:
 * @Author lite
 * @Date 2022/12/4
 * @Version V1.0
 **/
@FeignClient("gulimall-ware")
public interface WareFeignService {

    @PostMapping("/ware/waresku/getSkuStock")
    public R getSkuStock(@RequestBody List<Long> skuIds);
}
