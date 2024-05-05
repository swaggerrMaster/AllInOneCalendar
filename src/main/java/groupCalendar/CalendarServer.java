package groupCalendar;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class CalendarServer {

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("Added");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received message from client: " + message);
        broadcastMessage(message);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }
    
    private void broadcastMessage(String message) {
    	Queue<Session> sessionList = new LinkedList<>();
        for (Session s : sessions) {
        	sessionList.add(s);
        }
        while(!sessionList.isEmpty()) {
            try {
            	Session s = sessionList.poll();
                s.getBasicRemote().sendText(message);
                System.out.println("Sent message");
            } 
            catch (IOException e) {
                System.err.println("Error broadcasting message: " + e.getMessage());
            }
        }
        
    }

}