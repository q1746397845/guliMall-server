package com.lt.gulimill.gulimallproduct;


import com.lt.gulimall.product.GulimallProductApplication;
import com.lt.gulimall.product.entity.AttrEntity;
import com.lt.gulimall.product.entity.AttrGroupEntity;
import com.lt.gulimall.product.service.AttrGroupService;
import com.lt.gulimall.product.service.AttrService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = GulimallProductApplication.class)
@RunWith(SpringRunner.class)
class GulimallProductApplicationTests {

    @Autowired
    private AttrService attrService;

    @Test
    void contextLoads() {
        AttrEntity attrEntity = new AttrEntity();
        attrEntity.setAttrName("ceshi1");
        attrService.save(attrEntity);
    }

}
