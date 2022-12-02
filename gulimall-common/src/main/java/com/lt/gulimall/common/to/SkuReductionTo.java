package com.lt.gulimall.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName SkuReductionTo
 * @Description:
 * @Author lite
 * @Date 2022/11/13
 * @Version V1.0
 **/
@Data
public class SkuReductionTo {
    private Long skuId;
    //满几件
    private int fullCount;
    //打几折
    private BigDecimal discount;

    //是否叠加其他优惠[0-不可叠加，1-可叠加]
    private int countStatus;
    //满多少
    private BigDecimal fullPrice;
    //减多少
    private BigDecimal reducePrice;

    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
