package com.zzy.webcut.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {Controller.class, RestController.class})
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 处理数据库字段重复异常
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)   //处理的异常类型
    public R<String> sqlExceptionHandler(SQLIntegrityConstraintViolationException ex){
        if(ex.getMessage().contains("Duplicate entry")){
            String[] sp = ex.getMessage().split(" ");
            return R.error( Code.CUT_ALREADY_EXISTS, String.format("剪切板%s已存在", sp[2]));
        }
        return R.error(Code.UNKNOW_ERROR,"数据库未知错误");
    }

    /**
     * 处理自定义异常
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> customExceptionHandler(CustomException ex){

        return R.error(ex.getMessage());
    }

    /**
     * 剪切板找不到
     * @param ex
     * @return
     */
    @ExceptionHandler(ClipboardNotFoundException.class)
    public R<String> clipboardNotFoundExceptionHandler(ClipboardNotFoundException ex){
        return R.error(Code.CUT_NOT_FOUND, ex.getMessage());
    }







    /**
     * 处理其他异常
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public R<String> exceptionHandler(Exception ex){

        return R.error(Code.UNKNOW_ERROR,ex.getMessage());
    }

}
