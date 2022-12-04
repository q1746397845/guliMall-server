package com.lt.gulimall.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName SkuEsModel
 * @Description:
 * @Author lite
 * @Date 2022/12/4
 * @Version V1.0
 **/
@Data
public class SkuEsModel {

    private Long skuId;

    private Long spuId;

    /**
     * sku标题
     */
    private String skuTitle;

    /**
     * sku价格
     */
    private BigDecimal skuPrice;

    /**
     * sku图片
     */
    private String skuImg;

    private Long saleCount;

    /**
     * 是否有库存
     */
    private Boolean hasStock;

    /**
     * sku热度
     */
    private Long hotScore;

    /**
     * 分类id
     */
    private Long catalogId;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 品牌logo
     */
    private String brandImg;

    /**
     * 分类名称
     */
    private String catalogName;

    /**
     * 可检索属性
     */
    private List<attr> attrs;

    @Data
    public static class attr{

        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
