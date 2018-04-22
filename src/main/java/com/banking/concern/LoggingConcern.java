package com.banking.concern;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.banking.handlers.utils.LogMessage;
import com.banking.utils.MessageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Aspect
public class LoggingConcern {

	private static final Logger logger = LogManager.getLogger(LoggingConcern.class);

	@Before("execution(* com.banking.controller.AccountsController.*(..))")
	public void logRequest(JoinPoint joinPoint) throws JsonProcessingException {
		if (logger.isDebugEnabled()) {
			String klass = joinPoint.getTarget().getClass().getSimpleName();
			String method = joinPoint.getSignature().getName();

			Object[] objects = joinPoint.getArgs();
			Map<String, Object> params = new HashMap<>();
			for (Object object : objects) {
				params.put(object.getClass().getName(), object);
			}

			LogMessage message = new LogMessage();
			message.setMessage("Method " + method + " is being called on class " + klass);
			message.setParams(params);

			logger.debug(MessageUtil.getJsonMessage(message));
		}
	}

}
