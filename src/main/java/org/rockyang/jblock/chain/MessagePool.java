package org.rockyang.jblock.chain;

import com.google.common.base.Objects;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * message pool
 * @author yangjian
 */
@Component
public class MessagePool {

	private final List<Message> messages = new ArrayList<>();

	public void pendingMessage(Message message) {

		// check if the message is already in the message pool
		for (Message msg : this.messages) {
			if (Objects.equal(msg.getCid(), message.getCid())) {
				return;
			}
		}
		this.messages.add(message);
	}

	public List<Message> getTransactions() {
		return messages;
	}

	// remove message from the message pool
	public void removeTransaction(String cid)
	{
		for (Iterator iterator = messages.iterator(); iterator.hasNext();) {
			Message msg = (Message) iterator.next();
			if (Objects.equal(msg.getCid(), cid)) {
				iterator.remove();
			}
		}
	}

}
