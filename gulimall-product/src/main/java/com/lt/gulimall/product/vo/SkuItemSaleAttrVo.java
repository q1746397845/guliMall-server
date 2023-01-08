package com.lt.gulimall.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName SkuItemSaleAttrVo
 * @Description:
 * @Author lite
 * @Date 2023/1/8
 * @Version V1.0
 **/
@Data
public class SkuItemSaleAttrVo {
    private String attrId;

    private String attrName;

    private List<AttrValueWithSkuIds> attrValues;
}
