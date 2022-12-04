package com.ly.gulimall.search.service;

import com.lt.gulimall.common.to.es.SkuEsModel;
import com.lt.gulimall.common.utils.R;

import java.io.IOException;
import java.util.List;

public interface ProductSaveService {
    Boolean productStatusUp(List<SkuEsModel> list) throws IOException;
}
