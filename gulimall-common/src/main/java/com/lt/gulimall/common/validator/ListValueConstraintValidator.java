package com.lt.gulimall.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;

/**
 * @ClassName ListValueConstraintValidator
 * @Description:
 * @Author lite
 * @Date 2022/11/4
 * @Version V1.0
 **/
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {
    private HashSet<Integer> set = new HashSet<>();


    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null){
            return true;
        }
        return set.contains(value);
    }

    @Override
    public void initialize(ListValue constraintAnnotation) {
        for (int val : constraintAnnotation.vals()) {
            set.add(val);
        }
    }
}
