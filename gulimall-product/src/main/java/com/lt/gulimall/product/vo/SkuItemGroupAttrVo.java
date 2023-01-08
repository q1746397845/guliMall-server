package com.lt.gulimall.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName SkuItemGroupAttrVo
 * @Description:
 * @Author lite
 * @Date 2023/1/8
 * @Version V1.0
 **/
@Data
public class SkuItemGroupAttrVo {
    private String groupName;
    private List<Attr> attrs;
}
