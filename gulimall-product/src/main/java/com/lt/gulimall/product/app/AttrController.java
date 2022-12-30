package com.lt.gulimall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.lt.gulimall.product.entity.ProductAttrValueEntity;
import com.lt.gulimall.product.service.ProductAttrValueService;
import com.lt.gulimall.product.vo.AttrResponseVo;
import com.lt.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lt.gulimall.product.service.AttrService;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.R;



/**
 * 商品属性
 *
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 10:27:47
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    /**
     * 获取spu规格
     * @param supId
     * @return
     */
    @GetMapping("/base/listforspu/{spuId}")
    public R getBaseAttrList(@PathVariable("spuId")Long supId){
        List<ProductAttrValueEntity> list = productAttrValueService.getBaseAttrList(supId);
        return R.ok(list);
    }

    /**
     *
     * @return
     */
    @PostMapping("/update/{spuId}")
    public R updateBaseAttr(@PathVariable("spuId")Long spuId,@RequestBody List<ProductAttrValueEntity> productAttrValueEntities){
        productAttrValueService.updateBaseAttr(spuId,productAttrValueEntities);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @GetMapping("/{type}/list/{catelogId}")
    //@RequiresPermissions("product:attr:list")
    public R baseList(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId,@PathVariable("type") String type){
        PageUtils page = attrService.baseList(params,catelogId,type);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
        AttrResponseVo attr = attrService.getDetail(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attrVo){
		attrService.saveAttr(attrVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attrVo){
		attrService.updateAttr(attrVo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
