package com.ly.gulimall.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lt.gulimall.common.constant.ESConstant;
import com.lt.gulimall.common.to.es.SkuEsModel;
import com.lt.gulimall.common.utils.R;
import com.ly.gulimall.search.config.GulimallElasticSearchConfig;
import com.ly.gulimall.search.service.ProductSaveService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ProductSaveServiceImpl
 * @Description:
 * @Author lite
 * @Date 2022/12/4
 * @Version V1.0
 **/
@Service
@Slf4j
public class ProductSaveServiceImpl implements ProductSaveService {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public Boolean productStatusUp(List<SkuEsModel> list) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : list) {
            IndexRequest indexRequest = new IndexRequest(ESConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String s = JSONObject.toJSONString(skuEsModel);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.asList(bulk.getItems()).stream().map(item -> item.getId()).collect(Collectors.toList());
        log.info("商品上架成功,skuId: {}",collect);
        return b;
    }
}
