package com.xinnn.reggie.exceptionHandler;

import com.xinnn.reggie.base.ReggieException;
import com.xinnn.reggie.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 自定义异常处理类
 */
@Slf4j
@ControllerAdvice
public class MyExceptionHandler {
    //处理数据库字段异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseBody
    public Result<String> FieldReExceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        //如果异常消息中包含 Duplicate entry
        if (ex.getMessage().contains("Duplicate entry")){
            String[] sts = ex.getMessage().split(" ");
            //返回具体那个字段重复了
            return Result.error(sts[2] + "重复了！");
        }
        return Result.error("未知错误！");
    }
    //处理自定义异常
    @ExceptionHandler(ReggieException.class)
    @ResponseBody
    public Result<String> reggieExceptionHandler(ReggieException ex){
        //给客户端返回异常消息
        return Result.error(ex.getMessage());
    }
}
