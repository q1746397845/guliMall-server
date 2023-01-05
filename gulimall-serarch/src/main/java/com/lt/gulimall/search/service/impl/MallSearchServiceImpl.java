package com.lt.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lt.gulimall.common.constant.ESConstant;
import com.lt.gulimall.common.to.es.SkuEsModel;
import com.lt.gulimall.common.utils.R;
import com.lt.gulimall.search.feign.ProductFeignService;
import com.lt.gulimall.search.service.MallSearchService;
import com.lt.gulimall.search.vo.AttrResponseVo;
import com.lt.gulimall.search.vo.BrandResponseVo;
import com.lt.gulimall.search.vo.SearchParam;
import com.lt.gulimall.search.config.GulimallElasticSearchConfig;
import com.lt.gulimall.search.vo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MallSearchServiceImpl
 * @Description:
 * @Author lite
 * @Date 2023/1/2
 * @Version V1.0
 **/
@Service
public class MallSearchServiceImpl implements MallSearchService {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private ProductFeignService productFeignService;

    @Override
    public SearchResult search(SearchParam searchParam,String queryString) {

        SearchRequest searchRequest = buildSearchRequest(searchParam);
        SearchResult searchResult = null;
        try {
            //执行检索请求
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
            //分析响应数据
            searchResult = buildSearchResult(searchResponse,searchParam,queryString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    private SearchResult buildSearchResult(SearchResponse searchResponse,SearchParam searchParam,String queryString) throws UnsupportedEncodingException {
        SearchResult searchResult = new SearchResult();
        List<SkuEsModel> list = new ArrayList<>();
        List<SearchResult.BrandVo> brands = new ArrayList<>();
        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();

        SearchHits hits = searchResponse.getHits();
        //获取商品信息
        if(hits.getHits() != null && hits.getHits().length != 0){
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel skuEsModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                if(StringUtils.isNotBlank(searchParam.getKeyword())){
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String s = skuTitle.getFragments()[0].toString();
                    skuEsModel.setSkuTitle(s);
                }
                list.add(skuEsModel);
            }
        }
        //获取品牌信息
        ParsedLongTerms brandAgg = searchResponse.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brandAgg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            long brandId = Long.parseLong(bucket.getKeyAsString());
            ParsedStringTerms brandImgAgg = bucket.getAggregations().get("brand_img_agg");
            ParsedStringTerms brandNameAgg = bucket.getAggregations().get("brand_name_agg");
            String brandImg = brandImgAgg.getBuckets().get(0).getKeyAsString();
            String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();
            brandVo.setBrandId(brandId);
            brandVo.setBrandImg(brandImg);
            brandVo.setBrandName(brandName);
            brands.add(brandVo);
        }

        //获取品牌信息
        ParsedLongTerms catalogAgg = searchResponse.getAggregations().get("catalog_agg");
        for (Terms.Bucket bucket : catalogAgg.getBuckets()) {
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            long catalogId = Long.parseLong(bucket.getKeyAsString());
            ParsedStringTerms catalogNameAgg = bucket.getAggregations().get("catalog_name_agg");
            String catalogName = catalogNameAgg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogId(catalogId);
            catalogVo.setCatalogName(catalogName);
            catalogVos.add(catalogVo);
        }

        //获取分类信息
        ParsedNested attrAgg = searchResponse.getAggregations().get("attr_agg");
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            Long attrId = Long.parseLong(bucket.getKeyAsString());
            ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attr_name_agg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            ParsedStringTerms attrValueAgg = bucket.getAggregations().get("attr_value_agg");
            List<String> attrValues = attrValueAgg.getBuckets().stream().map(attrValue -> {
                String keyAsString = attrValue.getKeyAsString();
                return keyAsString;
            }).collect(Collectors.toList());
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            attrVo.setAttrId(attrId);
            attrVo.setAttrName(attrName);
            attrVo.setAttrValue(attrValues);
            attrVos.add(attrVo);
        }

        //获取规格信息
        long total = hits.getTotalHits().value;
        searchResult.setTotal(total);
        searchResult.setTotalPages((int)total%ESConstant.PRODUCT_PAGE_SIZE == 0 ? (int)total/ESConstant.PRODUCT_PAGE_SIZE : (int)total/ESConstant.PRODUCT_PAGE_SIZE+1);
        if(searchParam.getPageNum() != null){
            searchResult.setPageNum(searchParam.getPageNum());
        }
        searchResult.setProducts(list);
        searchResult.setBrands(brands);
        searchResult.setAttrs(attrVos);
        searchResult.setCatalogs(catalogVos);
        List<Integer> pageNavs = new ArrayList<>();
        for (Integer i = 1; i <= searchResult.getTotalPages(); i++) {
            pageNavs.add(i);
        }
        searchResult.setPageNavs(pageNavs);

        //面包屑导航功能
        List<SearchResult.NavVo> navs = new ArrayList<>();
        List<Long> attrIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(searchParam.getAttrs())){
            for (String attr : searchParam.getAttrs()) {
                String[] s = attr.split("_");
                R r = productFeignService.getAttrInfo(Long.parseLong(s[0]));
                attrIds.add(Long.parseLong(s[0]));
                if(r.getCode().equals(0)){
                    SearchResult.NavVo navVo = new SearchResult.NavVo();
                    AttrResponseVo data = r.getData("attr",new TypeReference<AttrResponseVo>(){});
                    navVo.setNavName(data.getAttrName());
                    navVo.setNavValue(s[1]);
                    String link = replaceQueryString(queryString, "attrs", attr);
                    navVo.setLink(link);
                    navs.add(navVo);
                }
            }
        }
        if (!CollectionUtils.isEmpty(searchParam.getBrandId())) {
            R r = productFeignService.getBrandsByIds(searchParam.getBrandId());
            if(r.getCode().equals(0)){
                List<BrandResponseVo> brandVos = r.getData("brands", new TypeReference<List<BrandResponseVo>>() {});
                String brandNames = brandVos.stream().map(brand -> brand.getName()).collect(Collectors.joining(";"));
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                navVo.setNavValue(brandNames);
                String link = "";
                for (BrandResponseVo brandVo : brandVos) {
                    link = replaceQueryString(queryString,"brandId",brandVo.getBrandId().toString());
                }
                navVo.setLink(link);
                navVo.setNavName("品牌");
                navs.add(navVo);
            }
        }
        searchResult.setNavs(navs);
        searchResult.setAttrIds(attrIds);
        return searchResult;
    }

    private String replaceQueryString(String queryString,String key,String value) throws UnsupportedEncodingException {
        queryString = URLDecoder.decode(queryString,"UTF-8");
        queryString = queryString.replace("&"+ key +"="+value, "");
        queryString = queryString.replace(key +"="+value, "");
        if(StringUtils.isBlank(queryString)){
            return "http://search.gulimall.com/list.html";
        }else {
            return "http://search.gulimall.com/list.html?"+queryString;
        }
    }

    private SearchRequest buildSearchRequest(SearchParam searchParam) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //1.进行条件构建
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //1.1标题模糊匹配
        if(StringUtils.isNotBlank(searchParam.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", searchParam.getKeyword()));
        }
        //1.2按照分类查询
        if(searchParam.getCatalog3Id() != null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId",searchParam.getCatalog3Id()));
        }
        //1.3按照品牌名称查询
        if(!CollectionUtils.isEmpty(searchParam.getBrandId())){
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId",searchParam.getBrandId()));
        }
        //1.4按照价格区间查询
        if(StringUtils.isNotBlank(searchParam.getSkuPrice())){
            RangeQueryBuilder skuPrice = QueryBuilders.rangeQuery("skuPrice");
            String[] s = searchParam.getSkuPrice().split("_");
            if(searchParam.getSkuPrice().startsWith("_")){
                skuPrice.lt(s[1]);
            }else if(searchParam.getSkuPrice().endsWith("_")){
                skuPrice.gt(s[0]);
            }else{
                skuPrice.gt(s[0]).lt(s[1]);
            }
            boolQueryBuilder.filter(skuPrice);
        }
        //1.5按照是否有货查询
        if(searchParam.getHasStock() != null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock",searchParam.getHasStock() == 1));
        }
        //1.6 按照指定的属性进行查询
        if(!CollectionUtils.isEmpty(searchParam.getAttrs())){
            //attrs= 1_1.5寸:8寸&2_2022:2023
            for (String attr : searchParam.getAttrs()) {
                BoolQueryBuilder nestBoolQueryBuilder = QueryBuilders.boolQuery();
                String[] s = attr.split("_");
                String attrId = s[0];
                String[] attrValue = s[1].split(":");
                nestBoolQueryBuilder.must(QueryBuilders.termsQuery("attrs.attrId",attrId));
                nestBoolQueryBuilder.must(QueryBuilders.termsQuery("attrs.attrValue",attrValue));
                //每一个属性都得生成一个nested查询
                NestedQueryBuilder attrs = QueryBuilders.nestedQuery("attrs", nestBoolQueryBuilder, ScoreMode.None);
                boolQueryBuilder.filter(attrs);
            }
        }
        searchSourceBuilder.query(boolQueryBuilder);

        //2.排序
        if(StringUtils.isNotBlank(searchParam.getSort())){
            String sort = searchParam.getSort();
            String[] s = sort.split("_");
            searchSourceBuilder.sort(s[0],s[1].equalsIgnoreCase("asc") ? SortOrder.ASC:SortOrder.DESC);
        }
        //3.分页
        searchSourceBuilder.from((searchParam.getPageNum() - 1) * ESConstant.PRODUCT_PAGE_SIZE);
        searchSourceBuilder.size(ESConstant.PRODUCT_PAGE_SIZE);
        //4.高亮
        if(StringUtils.isNotBlank(searchParam.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        //5.聚合分析
        //5.1 品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);
        //品牌聚合的子聚合
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        searchSourceBuilder.aggregation(brand_agg);
        //5.2分类聚合
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg");
        catalog_agg.field("catalogId").size(50);
        catalog_agg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        searchSourceBuilder.aggregation(catalog_agg);
        //5.3属性聚合
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId").size(10);
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(10));
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(10));
        attr_agg.subAggregation(attr_id_agg);
        searchSourceBuilder.aggregation(attr_agg);

        String s = searchSourceBuilder.toString();
        System.out.println(s);
        SearchRequest searchRequest = new SearchRequest(new String[]{ESConstant.PRODUCT_INDEX},searchSourceBuilder);
        return searchRequest;

    }
}
