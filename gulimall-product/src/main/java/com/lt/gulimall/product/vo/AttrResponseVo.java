package com.lt.gulimall.product.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AttrResponseVo
 * @Description:
 * @Author lite
 * @Date 2022/11/8
 * @Version V1.0
 **/
@Data
public class AttrResponseVo extends AttrVo {

    /**
     * 分类名称
     */
    private String catelogName;

    //属性分组名称
    private String groupName;

    //分类完整路径
    private List<Long> catelogPath;

}
