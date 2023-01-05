package com.lt.gulimall.search.feign;

import com.lt.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("gulimall-product")
public interface ProductFeignService {

    /**
     * 信息
     */
    @RequestMapping("/product/attr/info/{attrId}")
    public R getAttrInfo(@PathVariable("attrId") Long attrId);

    @RequestMapping("/product/brand/getInfoByIds/{brandIds}")
    //@RequiresPermissions("product:brand:info")
    public R getBrandsByIds(@PathVariable("brandIds") List<Long> brandIds);
}
