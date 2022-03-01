package evolution.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import evolution.dto.LobbyDto;
import evolution.dto.Message;
import evolution.dto.ResponseMessage;
import evolution.dto.UserDto;
import evolution.services.LobbyService;
import evolution.services.impl.NotificationService;
import lombok.AllArgsConstructor;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class MessageController {
    private final NotificationService notificationService;
    private final LobbyService        lobbyService;


    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ResponseMessage getMessage(final Message message) throws InterruptedException {
        Thread.sleep(1000);
        notificationService.sendGlobalNotification();
        return new ResponseMessage(HtmlUtils.htmlEscape(message.getMessageContent()));
    }

    @MessageMapping("/lobby")
    @SendTo("/topic/lobby")
    public LobbyDto getLobby(@AuthenticationPrincipal UserDto user) throws InterruptedException {
        Thread.sleep(1000);
        notificationService.sendGlobalNotification();
        return lobbyService.get(user);
    }

    @MessageMapping("/private-message")
    @SendToUser("/topic/private-messages")
    public ResponseMessage getPrivateMessage(final Message message,
                                             final Principal principal) throws InterruptedException {
        Thread.sleep(1000);
        notificationService.sendPrivateNotification(principal.getName());
        return new ResponseMessage(HtmlUtils.htmlEscape(
                "Sending private message to user " + principal.getName() + ": "
                        + message.getMessageContent())
        );
    }
}
