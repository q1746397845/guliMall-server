package com.lt.gulimall.search;

import com.alibaba.fastjson.JSONObject;
import com.lt.gulimall.search.config.GulimallElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @ClassName com.lt.gulimall.search.ElasticSearchTest
 * @Description:
 * @Author lite
 * @Date 2022/12/2
 * @Version V1.0
 **/
@SpringBootTest(classes = GulimallSearchApplication.class)
@RunWith(SpringRunner.class)
public class ElasticSearchTest {

    @Resource
    private RestHighLevelClient client;

    @Test
    public void searchData() throws IOException {
        //构建查询参数
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("address", ",mill");
        sourceBuilder.query(matchQueryBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(10);

        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        sourceBuilder.aggregation(ageAgg);
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
        sourceBuilder.aggregation(balanceAvg);

        System.out.println("搜索参数: " + sourceBuilder);

        SearchRequest searchRequest= new SearchRequest("bank");
        SearchRequest source = searchRequest.source(sourceBuilder);
        //执行查询
        SearchResponse search = client.search(source, GulimallElasticSearchConfig.COMMON_OPTIONS);

        System.out.println(search);
    }

    @Test
    public void test1() throws IOException {
        IndexRequest request = new IndexRequest("users");
        request.id("1");
        User user = new User(1l,18,"张三");
        String s = JSONObject.toJSONString(user);
        IndexRequest source = request.source(s, XContentType.JSON);
        IndexResponse index = client.index(source,GulimallElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(index);
    }

    @Data
    class User{
        private Long userId;

        private Integer age;

        private String userName;

        public User(Long userId, Integer age, String userName) {
            this.userId = userId;
            this.age = age;
            this.userName = userName;
        }
    }
}
