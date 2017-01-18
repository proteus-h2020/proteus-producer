package com.treelogic.proteus.kafka;

import org.springframework.context.ApplicationEvent;

public class KafkaMessageEvent extends ApplicationEvent {

	private static final long serialVersionUID = 5743058377815147529L;

	private KafkaRecord<?> message;

	public KafkaMessageEvent(Object source, KafkaRecord<?> message) {
		super(source);
		this.message = message;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MessageEvent [message=").append(message).append(']');
		return builder.toString();
	}
	
	public KafkaRecord<?> getMessage(){
		return this.message;
	}

}