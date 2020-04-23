package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

import javax.xml.transform.Result;

/**这个类是自定义异常的类（这儿一定要记得要在引导类里面扫描了才能用的上）
 *
 *为啥用RuntimeException，这是因为他没有可侵入性，在其他代码调用的时候只需要把这个类调用就好了
 * 如果是exception则需要将改的代码的类抛出异常才行，需要改变代码
 */
public class CustomException extends RuntimeException {
    //错误代码
    ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode(){
        return resultCode;
    }
}
