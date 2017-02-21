package com.treelogic.proteus.websocket.echo;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.treelogic.proteus.formatting.JsonParser;
import com.treelogic.proteus.formatting.Parser;
import com.treelogic.proteus.kafka.KafkaMessageEvent;


@Component
public class EchoWebSocketHandler extends TextWebSocketHandler implements ApplicationListener<KafkaMessageEvent> {

	private final EchoService echoService;
	private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<WebSocketSession>());
	private final Parser<Object> parser;

	@Autowired
	public EchoWebSocketHandler(EchoService echoService) {
		this.echoService = echoService;
		this.parser = new JsonParser<>();
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
		this.sessions.remove(session);
	}
	@Override
	public void afterConnectionEstablished(WebSocketSession session){
		this.sessions.add(session);
	}
	
	private void sendAll(Object o){
		//String json = this.parser.parse(o);
		String json = (String) o;
		System.out.println(json);
		for(WebSocketSession s : this.sessions){
			try {
				s.sendMessage(new TextMessage(json));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String reply = this.echoService.getMessage(message.getPayload());
		session.sendMessage(new TextMessage(reply));
	}

	public void onApplicationEvent(KafkaMessageEvent messageEvent) {
		this.sendAll(messageEvent.getMessage());
		
	}

}
