package org.rockyang.jblock.chain;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * message pool
 * @author yangjian
 */
@Component
public class MessagePool {

	private final List<Message> messages = new CopyOnWriteArrayList<>();

	public void pendingMessage(Message message) {

		// check if the message is already in the message pool
		for (Message msg : this.messages) {
			if (Objects.equal(msg.getCid(), message.getCid())) {
				return;
			}
		}
		this.messages.add(message);
	}

	public List<Message> getMessages() {
		return messages;
	}

	// remove message from the message pool
	public void removeMessage(String cid)
	{
		messages.removeIf(message -> Objects.equal(message.getCid(), cid));
	}

	public boolean hasMessage(Message message)
	{
		for (Message msg : this.messages) {
			if (StringUtils.equals(msg.getCid(), message.getCid())) {
				return true;
			}
		}
		return false;
	}

}
