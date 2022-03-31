package org.rockyang.jblock.web.handler;

import org.rockyang.jblock.web.vo.CodeEnum;
import org.rockyang.jblock.web.vo.JsonVo;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * global exception catch handler
 * @author yangjian
 */
@ControllerAdvice
public class AppExceptionHandler {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonVo handle(HttpServletRequest request, HttpServletResponse response, Exception e)
    {
        logger.error("Something is wrong, ", e);
        return new JsonVo(CodeEnum.FAIL, e.getMessage());
    }
}
