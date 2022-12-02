package com.lt.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName PurchaseDoneVo
 * @Description:
 * @Author lite
 * @Date 2022/11/30
 * @Version V1.0
 **/
@Data
public class PurchaseDoneVo {

    private Long id;

    private List<PurchaseItemDoneVo> items;
}
