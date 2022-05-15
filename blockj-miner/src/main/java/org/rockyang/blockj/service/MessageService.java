package org.rockyang.blockj.service;

import org.rockyang.blockj.base.model.Message;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
public interface MessageService {
	String MESSAGE_PREFIX = "/messages/";

	boolean addMessage(Message message);

	// get message with the specified message Cid
	Message getMessage(String cid);

	boolean validateMessage(Message message);

	// send a message and return the message Cid
	String sendMessage(String from, String to, BigDecimal value, String param) throws Exception;
}
