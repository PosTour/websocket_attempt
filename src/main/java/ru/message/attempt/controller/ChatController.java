package ru.message.attempt.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.message.attempt.model.Action;
import ru.message.attempt.model.Message;
import ru.message.attempt.model.User;
import ru.message.attempt.service.MemberStore;

@Controller
public class ChatController {

    private final MemberStore memberStore;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChatController(MemberStore memberStore, SimpMessagingTemplate simpMessagingTemplate) {
        this.memberStore = memberStore;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/user")
    public void getUsers(User user, SimpMessageHeaderAccessor headerAccessor) {
        User newUser = new User(user.id(), user.username());
        headerAccessor.getSessionAttributes().put("user", newUser);
        memberStore.addMember(newUser);
        if (!memberStore.getMembers().isEmpty()) {
            simpMessagingTemplate.convertAndSend("/topic/users", memberStore.getMembers());
        }
        Message newMessage = new Message(newUser, null, Action.JOINED, Instant.now());
        simpMessagingTemplate.convertAndSend("/topic/messages", newMessage);
    }

    @EventListener
    public void handleSessionConnectEvent(SessionConnectEvent event) {
        System.out.println("Session Connect Event");
    }

    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
        System.out.println("Session Disconnect Event");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes == null) {
            return;
        }
        User user = (User) sessionAttributes.get("user");
        if (user == null) {
            return;
        }
        memberStore.removeMember(user);
        simpMessagingTemplate.convertAndSend("/topic/users", memberStore.getMembers());

        Message message = new Message(user, "", Action.LEFT, Instant.now());
        simpMessagingTemplate.convertAndSend("/topic/messages", message);
    }
}