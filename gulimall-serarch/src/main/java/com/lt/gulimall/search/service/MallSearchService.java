package com.lt.gulimall.search.service;

import com.lt.gulimall.search.vo.SearchParam;
import com.lt.gulimall.search.vo.SearchResult;

/**
 * @ClassName MallSearchService
 * @Description:
 * @Author lite
 * @Date 2023/1/2
 * @Version V1.0
 **/
public interface MallSearchService {
    SearchResult search(SearchParam searchParam,String queryString);
}
