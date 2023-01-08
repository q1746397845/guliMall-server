package com.lt.gulimall.product.web;

import com.lt.gulimall.product.service.SkuInfoService;
import com.lt.gulimall.product.vo.SkuItemVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName ItemController
 * @Description:
 * @Author lite
 * @Date 2023/1/6
 * @Version V1.0
 **/
@Controller
public class ItemController {

    @Resource
    private SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId")Long skuId, Model model) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = skuInfoService.skuItem(skuId);
        model.addAttribute("item",skuItemVo);
        return "item";
    }
}
