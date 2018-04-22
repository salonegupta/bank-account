package com.banking.utils;

import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * To convert a message to a localized message based on users locale.
 * 
 * @author salonegupta
 * 
 */
public class MessageUtil {

	/**
	 * This method provides a localized message for given message code,
	 * parameters and locale.
	 * 
	 * @param messageSource
	 *            instance of message source for a resource bundle.
	 * @param code
	 *            message code.
	 * @param params
	 *            parameters required for message formatting.
	 * @return formatted message string based on locale.
	 */
	public static String getMessageResourceString(MessageSource messageSource, String code,
			Map<String, Object> params) {
		return messageSource.getMessage(code, new Object[] {}, LocaleContextHolder.getLocale());
	}

	/**
	 * This method provides a localised message for given message code,
	 * parameters and locale.
	 * 
	 * @param messageSource
	 *            instance of message source for a resource bundle.
	 * @param code
	 *            message code.
	 * @return formatted message string based on locale.
	 */
	public static String getMessageResourceString(MessageSource messageSource, String code) {
		return getMessageResourceString(messageSource, code, null);
	}

	public static String getJsonMessage(Object object) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			return "Not able to convert Object to JSON for logging. " + e.getMessage();
		}
	}
}
