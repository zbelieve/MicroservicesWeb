package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 统一异常捕获类，先要通过CustomException抛出异常，然后在这边捕获异常
 *
 */

@ControllerAdvice//控制器增强,他怎么增强的呢，就是里面能加入@ExceptionHandler使得可以捕获异常
@ResponseBody//这个可以将返回的信息转为josn
public class ExceptionCatch {
    //定于map,配置异常类型所对应的错误,Class<? extends Throwable>代表继承这个的类，所有的捕获异常都是基于他
    //所有的返回的code都是基于ResultCode
    //ImmutableMap这是谷歌用的一个方法，一旦构建不可更改
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> Exceptions;
    //上面那个map需要定义map的builder对象，去构建ImmutableMap
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

    //定义日志
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);
    //捕获CustomException此类异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException customException){
        //记录日志
        LOGGER.error("catch exception:{}",customException.getMessage());
        ResultCode resultCode = customException.getResultCode();
        return new ResponseResult(resultCode);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception exception){
        //记录日志
        LOGGER.error("catch exception:{}",exception.getMessage());
        if(Exceptions== null){
            Exceptions = builder.build();//builder将Exceptions构建成功
        }
        //从Exceptions错误类型中找对应的错误代码，找到返回响应给用户，找不到，响应99999异常
        ResultCode resultCode = Exceptions.get(exception.getClass());
        if(resultCode!=null){
            //这儿是返回自定义的不可预知异常
            return new ResponseResult(resultCode);
        }else{
            //下面就是返回99999异常
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }

    }
    //静态方法块
    static {
        //定义异常类型对应的错误代码,就是定义一个错误类型和错误代码的buildermap
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALID_PARAM);
    }
}
