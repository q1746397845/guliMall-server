package com.lt.gulimall.product.app;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.lt.gulimall.product.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lt.gulimall.product.entity.SpuInfoEntity;
import com.lt.gulimall.product.service.SpuInfoService;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.R;



/**
 * spu信息
 *
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 10:27:47
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;


    @PostMapping("/{spuId}/up")
    public R spuUp(@PathVariable("spuId")Long spuId){
        return spuInfoService.spuUp(spuId);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:spuinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        //PageUtils page = spuInfoService.queryPage(params);
        PageUtils page = spuInfoService.querybByCondition(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:spuinfo:info")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:spuinfo:save")
    public R save(@RequestBody SpuSaveVo spuSaveVo){
		spuInfoService.saveInfo(spuSaveVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:spuinfo:delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
