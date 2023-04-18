package com.hrids.ctrbt.util;

import com.hrids.ctrbt.config.properties.ChatgptProperties;
import com.hrids.ctrbt.config.properties.ProxyProperties;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.ConsoleStreamListener;
import com.plexpt.chatgpt.listener.SseStreamListener;
import com.plexpt.chatgpt.util.Proxys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PostConstruct;
import java.net.Proxy;
import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
@Component
public class ChatGPTStrreamUtil {

    private final ChatgptProperties chatgptProperties;

    private final ProxyProperties proxyProperties;

    private ChatGPTStream chatGPTStream;

    @PostConstruct
    public void init() {
        //如果在国内访问，使用这个
        if (!StringUtils.isEmpty(proxyProperties)) {
            Proxy proxy = Proxys.http(proxyProperties.getIp(), proxyProperties.getPort());
            chatGPTStream = ChatGPTStream.builder()
                    .apiKeyList(chatgptProperties.getSecretKey())
                    .timeout(900)
                    .proxy(proxy)
                    .apiHost("https://api.openai.com/") //代理地址
                    .build()
                    .init();
        } else {
            chatGPTStream = ChatGPTStream.builder()
                    .apiKeyList(chatgptProperties.getSecretKey())
                    .timeout(900)
                    .apiHost("https://api.openai.com/") //代理地址
                    .build()
                    .init();
        }
    }

    public SseEmitter chat(String userMessage, String user) throws RuntimeException {

        SseEmitter sseEmitter = new SseEmitter(-1L);
        SseStreamListener listener = new SseStreamListener(sseEmitter);
        Message message = Message.of(userMessage);
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .user(user)
                .messages(Arrays.asList(message))
                .build();
        listener.setOnComplate(msg -> {
            //回答完成，可以做一些事情
        });
        chatGPTStream.streamChatCompletion(chatCompletion, listener);
        return sseEmitter;
    }
}
