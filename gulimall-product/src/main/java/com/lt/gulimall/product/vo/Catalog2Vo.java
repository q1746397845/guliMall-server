package com.lt.gulimall.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName Catalog2Vo
 * @Description:
 * @Author lite
 * @Date 2022/12/5
 * @Version V1.0
 **/
@Data
public class Catalog2Vo {
    private Long catalog1Id;

    private List<Catalog3Vo> catalog3List;

    private Long id;

    private String name;

    @Data
    public static class Catalog3Vo{
        private Long catalog2Id;

        private Long id;

        private String name;
    }
}
