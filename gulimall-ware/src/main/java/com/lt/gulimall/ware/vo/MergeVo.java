package com.lt.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName MergeVo
 * @Description:
 * @Author lite
 * @Date 2022/11/29
 * @Version V1.0
 **/
@Data
public class MergeVo {

    private Long purchaseId;

    private List<Long> items;
}
