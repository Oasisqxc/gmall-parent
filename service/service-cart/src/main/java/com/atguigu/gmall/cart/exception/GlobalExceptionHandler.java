package com.atguigu.gmall.cart.exception;

import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice
public class GlobalExceptionHandler {

//    业务期间出现异常都用gmallexception包装
    @ExceptionHandler(GmallException.class)
    public Result handleGmallException(GmallException exception){
//        业务状态的枚举类

      ResultCodeEnum codeEnum= exception.getCodeEnum();
        Result<String> result = Result.build("", codeEnum);
        return result;//给前端的返回
    }

    @ExceptionHandler(NullPointerException.class)
    public String handleGmallException(NullPointerException exception){
//        业务状态的枚举类

        return "hh";//给前端的返回
    }
}
