package com.lt.gulimall.common.constant;

/**
 * @ClassName WareConstant
 * @Description:
 * @Author lite
 * @Date 2022/11/29
 * @Version V1.0
 **/
public class WareConstant {
    public enum PurchaseStatus{
        CREATED(0,"新建"),
        ASSIGNED(1,"已分配"),
        RECEIVE(2,"已领取"),
        FINISH(3,"已完成"),
        HAS_ERROR(4,"有异常"),
        ;

        private Integer code;
        private String msg;

        PurchaseStatus(Integer code, String msg) {
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

    public enum PurchaseDetailStatus{
        CREATED(0,"新建"),
        ASSIGNED(1,"已分配"),
        BUYING(2,"正在采购"),
        FINISH(3,"已完成"),
        PURCHASE_ERROR(4,"采购失败"),
        ;

        private Integer code;
        private String msg;

        PurchaseDetailStatus(Integer code, String msg) {
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
