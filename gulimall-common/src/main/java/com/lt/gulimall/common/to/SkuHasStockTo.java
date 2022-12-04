package com.lt.gulimall.common.to;

import lombok.Data;

/**
 * @ClassName SkuHasStockTo
 * @Description:
 * @Author lite
 * @Date 2022/12/4
 * @Version V1.0
 **/
@Data
public class SkuHasStockTo {
    private Long skuId;

    private Integer stock;
}
