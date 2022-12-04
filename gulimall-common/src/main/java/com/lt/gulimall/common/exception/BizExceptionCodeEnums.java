package com.lt.gulimall.common.exception;

public enum BizExceptionCodeEnums {

    UNKNOWN_EXCEPTION("未知异常",10000),
    VALID_EXCEPTION("参数格式校验失败",10001),
    PRODUCT_UP_EXCEPTION("商品上架异常",11000);
    ;

    private String message;

    private Integer code;

    BizExceptionCodeEnums(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
