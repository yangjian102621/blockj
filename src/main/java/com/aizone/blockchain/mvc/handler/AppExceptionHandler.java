package com.aizone.blockchain.mvc.handler;

import com.aizone.blockchain.utils.JsonVo;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 全局异常捕获处理类
 * @author yangjian
 */
@ControllerAdvice
public class AppExceptionHandler {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonVo handle(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {

        logger.error("ERROR ======> {}", e);
        return JsonVo.instance(JsonVo.CODE_SUCCESS, e.getMessage());
    }
}
