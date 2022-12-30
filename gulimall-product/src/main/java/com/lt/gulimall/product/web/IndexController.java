package com.lt.gulimall.product.web;

import com.lt.gulimall.product.entity.CategoryEntity;
import com.lt.gulimall.product.service.CategoryService;
import com.lt.gulimall.product.vo.Catalog2Vo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @ClassName IndexController
 * @Description:
 * @Author lite
 * @Date 2022/12/5
 * @Version V1.0
 **/
@Controller
public class IndexController {

    @Resource
    private CategoryService categoryService;

    @GetMapping({"/","/index"})
    public String indexPage(Model model){
        //查询一级分类
        List<CategoryEntity> categoryEntityList = categoryService.getLevel1Category();
        model.addAttribute("categorys",categoryEntityList);
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<Long,List<Catalog2Vo>> getCatalogJson(){
        return categoryService.getCatalogJson();
    }

    @ResponseBody
    @GetMapping("product/hello")
    public String hello(){
        return "hello";
    }
}
