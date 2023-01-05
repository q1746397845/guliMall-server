package com.lt.gulimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName SearchParam
 * @Description:
 * @Author lite
 * @Date 2023/1/2
 * @Version V1.0
 **/
@Data
public class SearchParam {

    /**
     * 标题全文检索
     */
    private String keyword;

    /**
     * 三级分类id
     */
    private Long catalog3Id;

    /**
     * 品牌id
     */
    private List<Long> brandId;

    /**
     * 排序字段
     */
    private String sort;

    /**
     * 是否显示有货
     */
    private Integer  hasStock;

    /**
     * 价格区间
     */
    private String skuPrice;

    /**
     * 属性
     */
    private List<String> attrs;

    /**
     * 页码
     */
    private Integer pageNum=1;

}
