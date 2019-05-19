package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

@Service
public class WebSocketClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringWebsocketWithoutStompClient.class);

    @PostConstruct
    public void connect() {
        try {
            WebSocketClient webSocketClient = new StandardWebSocketClient();

            WebSocketSession webSocketSession = webSocketClient.doHandshake(new TextWebSocketHandler() {
                @Override
                public void handleTextMessage(WebSocketSession session, TextMessage message) {
                    LOGGER.info("received message - " + message.getPayload());
                }

                @Override
                public void afterConnectionEstablished(WebSocketSession session) {
                    LOGGER.info("established connection - " + session);
                }
            }, new WebSocketHttpHeaders(), URI.create("ws://echo.websocket.org")).get();

            newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                try {
                    TextMessage message = new TextMessage("Hello !!");
                    webSocketSession.sendMessage(message);
                    LOGGER.info("sent message - " + message.getPayload());
                } catch (Exception e) {
                    LOGGER.error("Exception while sending a message", e);
                }
            }, 1, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOGGER.error("Exception while accessing websockets", e);
        }
    }
}


