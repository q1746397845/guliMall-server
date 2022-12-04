package com.lt.gulimall.common.constant;

/**
 * @ClassName ProductConstant
 * @Description:
 * @Author lite
 * @Date 2022/11/8
 * @Version V1.0
 **/
public class ProductConstant {

    public enum AttrTypeEnum{
        ATTR_TYPE_BASE(1,"基本属性","base"),
        ATTR_TYPE_SALE(0,"销售属性","sale"),
        ;

        private Integer code;
        private String msg;
        private String type;

        AttrTypeEnum(Integer code, String msg, String type) {
            this.code = code;
            this.msg = msg;
            this.type = type;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static Integer getCode(String type) {
            for (AttrTypeEnum value : AttrTypeEnum.values()) {
                if(value.getType().equals(type)){
                    return value.getCode();
                }
            }
            return null;
        }
    }

    public enum SpuPublishStatus{
        NEW_SPU(0,"新建"),
        SPU_UP(1,"上架"),
        SPU_DOWN(2,"下架"),
        ;

        private Integer code;
        private String msg;

        SpuPublishStatus(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
