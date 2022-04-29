package org.rockyang.jblock.web.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * global exception catch handler
 *
 * @author yangjian
 */
@ControllerAdvice
public class AppExceptionHandler {

	private final static Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public void handle(Exception e)
	{
		// @TODO: use different handler with the different exception
		logger.error("Something is wrong, ", e);
	}
}
