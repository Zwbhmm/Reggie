package com.itheima.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 异常统一处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
     public R<String> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex){
         if(ex.getMessage().contains("Duplicate entry")){
             String[] strings = ex.getMessage().split(" ");
             String[] str2= strings[2].split("'");
             return R.error("账号"+str2[1]+"已存在");
         }
         return R.error("未知异常");
     }
    @ExceptionHandler(value = RuntimeException.class)
    public R<String> CategoryException(CategoryException ex){
        return R.error(ex.getMessage());
    }
}
