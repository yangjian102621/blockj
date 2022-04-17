package org.rockyang.jblock.chain.service;

import org.rockyang.jblock.base.model.Message;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
public interface MessageService {

	void addMessage(Message message);

	// get message with the specified message Cid
	Message getMessage(String cid);

	boolean validateMessage(Message message);

	// send a message and return the message Cid
	String sendMessage(String from, String to, BigDecimal value, String param) throws Exception;
}
