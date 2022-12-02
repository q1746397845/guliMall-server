package com.lt.gulimall.ware.vo;

import lombok.Data;

/**
 * @ClassName PurchaseItemDoneVo
 * @Description:
 * @Author lite
 * @Date 2022/11/30
 * @Version V1.0
 **/
@Data
public class PurchaseItemDoneVo {

    private Long itemId;

    private Integer status;

    private String reason;
}
