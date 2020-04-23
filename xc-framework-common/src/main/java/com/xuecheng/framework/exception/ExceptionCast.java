package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
    这个类主要是用来封装错误代码的,用静态方法是为了能直接在其他地方能直接调用他
 */
public class ExceptionCast {

    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}
