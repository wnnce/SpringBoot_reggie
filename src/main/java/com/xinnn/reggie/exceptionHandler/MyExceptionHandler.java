package com.xinnn.reggie.exceptionHandler;

import com.xinnn.reggie.base.ReggieException;
import com.xinnn.reggie.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseBody
    public Result<String> FieldReExceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")){
            String[] sts = ex.getMessage().split(" ");
            return Result.error(sts[2] + "重复了！");
        }
        return Result.error("未知错误！");
    }
    @ExceptionHandler(ReggieException.class)
    @ResponseBody
    public Result<String> reggieExceptionHandler(ReggieException ex){
        return Result.error(ex.getMessage());
    }
}
