package com.lt.gulimall.search.vo;

import com.lt.gulimall.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * @ClassName SearchResult
 * @Description:
 * @Author lite
 * @Date 2023/1/2
 * @Version V1.0
 **/
@Data
public class SearchResult {

    /**
     * 商品信息
     */
    private List<SkuEsModel> products;

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页码
     */
    private Integer totalPages;

    /**
     * 当前查询到的结果所涉及到的品牌
     */
    private List<BrandVo> brands;

    /**
     * 当前查询到的结果所涉及到的属性
     */
    private List<AttrVo> attrs;

    /**
     * 当前查询到的结果所涉及到的分类
     */
    private List<CatalogVo> catalogs;

    private List<Integer> pageNavs;

    //面包屑导航数据
    private List<NavVo> navs;

    private List<Long> attrIds;

    @Data
    public static class NavVo{
        private String navName;

        private String navValue;

        private String link;
    }

    @Data
    public static class BrandVo{
        private Long brandId;

        private String brandName;

        private String brandImg;
    }

    @Data
    public static class AttrVo{
        private Long attrId;

        private String attrName;

        private List<String> attrValue;
    }

    @Data
    public static class CatalogVo{
        private Long catalogId;

        private String catalogName;

    }


}
