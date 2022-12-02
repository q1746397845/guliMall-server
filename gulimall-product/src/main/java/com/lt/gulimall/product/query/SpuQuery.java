package com.lt.gulimall.product.query;

import lombok.Data;

/**
 * @ClassName SpuQuery
 * @Description:
 * @Author lite
 * @Date 2022/11/27
 * @Version V1.0
 **/
@Data
public class SpuQuery {

    private Integer status;

    private String key;

    private Long brandId;

    private Long catelogId;

    private Integer page;

    private Integer limit;
}
