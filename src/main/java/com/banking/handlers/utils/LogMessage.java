package com.banking.handlers.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Captures log messages
 * 
 * @author salonegupta
 *
 */
public class LogMessage implements Serializable {

	private static final long serialVersionUID = 2594168610488002977L;

	private String message;

	@JsonInclude(value = Include.NON_EMPTY)
	private Map<String, Object> params;

	public LogMessage() {
		this.params = new HashMap<>();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

}
