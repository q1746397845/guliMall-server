package com.lt.gulimall.product.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.lt.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.lt.gulimall.product.entity.AttrEntity;
import com.lt.gulimall.product.service.AttrAttrgroupRelationService;
import com.lt.gulimall.product.service.AttrService;
import com.lt.gulimall.product.service.CategoryService;
import com.lt.gulimall.product.vo.AttrGroupRelationVo;
import com.lt.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lt.gulimall.product.entity.AttrGroupEntity;
import com.lt.gulimall.product.service.AttrGroupService;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.R;


/**
 * 属性分组
 *
 * @author lt
 * @email 1746397845@qq.com
 * @date 2022-10-26 10:27:47
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * 获取分类下的属性分组信息和属性信息
     * @param catelogId
     * @return
     */
    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId){
        List<AttrGroupWithAttrsVo> vos = attrGroupService.getAttrGroupWithAttrs(catelogId);
        return R.ok(vos);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId) {
        //PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPageByParams(params, catelogId);

        return R.ok().put("page", page);
    }

    /**
     * 获取当前分组关联的规格参数
     * @param attrGroupId
     * @return
     */
    @GetMapping("/{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrGroupId") Long attrGroupId) {
        List<AttrEntity> attrList = attrService.getRelationAttr(attrGroupId);
        return R.ok(attrList);
    }

    /**
     * 获取未被关联的规格参数
     * @param attrGroupId
     * @param params
     * @return
     */
    @GetMapping("/{attrGroupId}/noattr/relation")
    public R noattrRelation(@PathVariable("attrGroupId") Long attrGroupId,@RequestParam Map<String, Object> params) {
        PageUtils pageUtils = attrService.getNoattrRelation(attrGroupId,params);
        return R.ok().put("page", pageUtils);
    }

    /**
     * 批量移除分组关联的规格参数
     * @param attrroupRelationVos
     * @return
     */
    @PostMapping("/attr/relation/delete")
    public R relationDelete(@RequestBody AttrGroupRelationVo[] attrroupRelationVos) {
        attrAttrgroupRelationService.relationDelete(attrroupRelationVos);
        return R.ok();
    }

    @PostMapping("/attr/relation")
    public R saveAttrRelation(@RequestBody AttrGroupRelationVo[] attrroupRelationVos){
        attrAttrgroupRelationService.saveAttrRelation(attrroupRelationVos);
        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        List<Long> categoryPath = categoryService.queryFullCategoryPath(attrGroup.getCatelogId());
        attrGroup.setCatelogIdPath(categoryPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
