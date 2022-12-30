package com.lt.gulimall.product.app;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.lt.gulimall.common.validator.group.AddGroup;
import com.lt.gulimall.common.validator.group.UpdateGroup;
import com.lt.gulimall.common.validator.group.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lt.gulimall.product.entity.BrandEntity;
import com.lt.gulimall.product.service.BrandService;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.R;


/**
 * 品牌
 *
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 10:27:47
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    private ObjectError fieldError;


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:brand:save")
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand){
        brandService.save(brand);
        return R.ok();

    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@Validated({UpdateGroup.class})  @RequestBody BrandEntity brand){
		brandService.updateDetail(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/updateStatus")
    //@RequiresPermissions("product:brand:update")
    public R updateStatus(@Validated({UpdateStatusGroup.class})  @RequestBody BrandEntity brand){
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(brand.getBrandId());
        brandEntity.setShowStatus(brand.getShowStatus());
        brandService.updateById(brandEntity);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
