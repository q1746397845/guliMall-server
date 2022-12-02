package com.lt.gulimall.product.exception;

/**
 * @ClassName GulimallExceptionControllerAdvice
 * @Description:
 * @Author lite
 * @Date 2022/11/3
 * @Version V1.0
 **/

import com.lt.gulimall.common.exception.BizExceptionCodeEnums;
import com.lt.gulimall.common.utils.R;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice("com.lt.gulimall.product")
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        HashMap<String, String> errMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return R.error(BizExceptionCodeEnums.VALID_EXCEPTION.getCode(),BizExceptionCodeEnums.VALID_EXCEPTION.getMessage(),errMap);
    }
}
