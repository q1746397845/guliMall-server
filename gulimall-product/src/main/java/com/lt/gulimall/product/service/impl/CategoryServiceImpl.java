package com.lt.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lt.gulimall.product.service.CategoryBrandRelationService;
import com.lt.gulimall.product.vo.Catalog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.gulimall.common.utils.PageUtils;
import com.lt.gulimall.common.utils.Query;

import com.lt.gulimall.product.dao.CategoryDao;
import com.lt.gulimall.product.entity.CategoryEntity;
import com.lt.gulimall.product.service.CategoryService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> treeList() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        List<CategoryEntity> level1Categorys = categoryEntities.stream()
                .filter(category -> category.getParentCid().equals(0L))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort)).collect(Collectors.toList());
        level1Categorys.stream().forEach(category -> category.setChildren(getChildrens(categoryEntities,category)));
        return level1Categorys;
    }

    @Override
    public void removeMenuByIds(List<Long> catIdList) {
        baseMapper.deleteBatchIds(catIdList);
    }

    @Override
    public List<Long> queryFullCategoryPath(Long catelogId) {
        List<Long> list = new ArrayList<>();
        queryParentPath(catelogId,list);
        Collections.reverse(list);
        return list;
    }

    @Override

    @Caching(evict ={
            @CacheEvict(value = {"category"},key = "'getLevel1Category'" ),
            @CacheEvict(value = {"category"},key = "'getCatalogJson'" )
    })
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
        if(!StringUtils.isEmpty(category.getName())){
            //修改关联表
            categoryBrandRelationService.updateCatNameByCatId(category.getCatId(),category.getName());
        }
        stringRedisTemplate.delete("catalogJson");
    }

    @Override
    @Cacheable(value = {"category"},key = "#root.method.name")
    public List<CategoryEntity> getLevel1Category() {
        System.out.println("getLevel1Category......");
        List<CategoryEntity> parent_cid = this.list(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return parent_cid;
    }

    @Override
    @Cacheable(value = {"category"},key = "#root.method.name")
    public Map<Long,List<Catalog2Vo>> getCatalogJson() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        List<CategoryEntity> level1Categorys = categoryEntities.stream()
                .filter(category -> category.getParentCid().equals(0L)).collect(Collectors.toList());

        Map<Long, List<Catalog2Vo>> collect = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId(),
                v -> getCategoryByParentId(categoryEntities,v.getCatId()).stream().map(item -> {
                    Catalog2Vo catalog2Vo = new Catalog2Vo();
                    catalog2Vo.setId(item.getCatId());
                    catalog2Vo.setCatalog1Id(v.getCatId());
                    catalog2Vo.setName(item.getName());
                    List<Catalog2Vo.Catalog3Vo> Catalog3VoList = getCategoryByParentId(categoryEntities,item.getCatId()).stream().map(category -> {
                        Catalog2Vo.Catalog3Vo catalog3Vo = new Catalog2Vo.Catalog3Vo();
                        catalog3Vo.setName(category.getName());
                        catalog3Vo.setCatalog2Id(item.getCatId());
                        catalog3Vo.setId(category.getCatId());
                        return catalog3Vo;
                    }).collect(Collectors.toList());
                    catalog2Vo.setCatalog3List(Catalog3VoList);
                    return catalog2Vo;
                }).collect(Collectors.toList())));
        return collect;
    }

    public Map<Long,List<Catalog2Vo>> getCatalogJson2() {
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        String s = stringStringValueOperations.get("catalogJson");
        if(StringUtils.isEmpty(s)){
            System.out.println("未命中缓存,执行查库操作");
            Map<Long, List<Catalog2Vo>> catalogJsonFromDb = getCatalogJsonFromDbWithRedissonLock();
            return catalogJsonFromDb;
        }
        return JSON.parseObject(s,new TypeReference<Map<Long,List<Catalog2Vo>>>() {});
    }

    public Map<Long,List<Catalog2Vo>> getCatalogJsonFromDbWithRedissonLock(){
        RLock lock = redissonClient.getLock("catalogJson-lock");;
        lock.lock();
        Map<Long, List<Catalog2Vo>> catalogJson = null;
        try {
            catalogJson = getCatalogJsonFromDb();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return catalogJson;
    }

    public Map<Long,List<Catalog2Vo>> getCatalogJsonFromDbWithSync(){
        synchronized (this){
            return getCatalogJsonFromDb();
        }
    }

    public Map<Long,List<Catalog2Vo>> getCatalogJsonFromDb(){
        //得到锁以后再去缓存查一次,确定没有采取查库
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        String s = stringStringValueOperations.get("catalogJson");
        if(StringUtils.isEmpty(s)){
            System.out.println("查询了数据库");
            List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
            List<CategoryEntity> level1Categorys = categoryEntities.stream()
                    .filter(category -> category.getParentCid().equals(0L)).collect(Collectors.toList());

            Map<Long, List<Catalog2Vo>> collect = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId(),
                    v -> getCategoryByParentId(categoryEntities,v.getCatId()).stream().map(item -> {
                        Catalog2Vo catalog2Vo = new Catalog2Vo();
                        catalog2Vo.setId(item.getCatId());
                        catalog2Vo.setCatalog1Id(v.getCatId());
                        catalog2Vo.setName(item.getName());
                        List<Catalog2Vo.Catalog3Vo> Catalog3VoList = getCategoryByParentId(categoryEntities,item.getCatId()).stream().map(category -> {
                            Catalog2Vo.Catalog3Vo catalog3Vo = new Catalog2Vo.Catalog3Vo();
                            catalog3Vo.setName(category.getName());
                            catalog3Vo.setCatalog2Id(item.getCatId());
                            catalog3Vo.setId(category.getCatId());
                            return catalog3Vo;
                        }).collect(Collectors.toList());
                        catalog2Vo.setCatalog3List(Catalog3VoList);
                        return catalog2Vo;
                    }).collect(Collectors.toList())));
            stringStringValueOperations.set("catalogJson",JSON.toJSONString(collect));
            return collect;
        }
        return JSON.parseObject(s,new TypeReference<Map<Long,List<Catalog2Vo>>>() {});
    }



    private List<CategoryEntity> getCategoryByParentId(List<CategoryEntity> categoryEntities,Long parentId){
        return categoryEntities.stream().filter(category -> category.getParentCid().equals(parentId)).collect(Collectors.toList());
    }

    private void queryParentPath(Long catelogId, List<Long> list){
        list.add(catelogId);
        CategoryEntity categoryEntity = baseMapper.selectById(catelogId);
        if(categoryEntity.getParentCid() != 0){
            queryParentPath(categoryEntity.getParentCid(),list);
        }
    }

    private List<CategoryEntity> getChildrens(List<CategoryEntity> allCategoryList, CategoryEntity parentCategory){
        List<CategoryEntity> collect = allCategoryList.stream()
                .filter(category -> category.getParentCid().equals(parentCategory.getCatId()))
                .map(category -> {
                    category.setChildren(getChildrens(allCategoryList, category));
                    return category;
                }).sorted(Comparator.comparingInt(CategoryEntity::getSort)).collect(Collectors.toList());
        return collect;
    }
}
