package com.atguigu.system.handler;

import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.system.exception.DiyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理类
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result resolveException(Exception e){
        e.printStackTrace();
        return  Result.fail();
    }
    /**
     * 创建一个处理数学异常的方法
     * 执行特定异常
     */
    @ExceptionHandler(ArithmeticException.class)
    public Result resolveArithmeticException(ArithmeticException e){
        e.printStackTrace();
        return Result.fail().message(e.getMessage());
    }
    /**
     * 处理自定义异常的方法
     */
    @ExceptionHandler(DiyException.class)
    public Result resolveDiyException(DiyException e){
        e.printStackTrace();
        return Result.fail().message(e.getMessage()).code(e.getCode());
    }
    /**
     * spring security异常
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result error(AccessDeniedException e) throws AccessDeniedException {
        return Result.build(null, ResultCodeEnum.PERMISSION);
    }
}
