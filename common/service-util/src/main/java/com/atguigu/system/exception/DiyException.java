package com.atguigu.system.exception;

import com.atguigu.common.result.ResultCodeEnum;
import lombok.Data;

@Data
public class DiyException extends RuntimeException{
    private Integer code;
    private String message;

    public DiyException(String message, Integer code) {
        this.code = code;
        this.message = message;
    }
    public DiyException(ResultCodeEnum resultCodeEnum){
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "DiyException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
