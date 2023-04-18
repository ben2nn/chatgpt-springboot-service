package com.hrids.ctrbt.util;

import com.hrids.ctrbt.config.properties.ChatgptProperties;
import com.hrids.ctrbt.config.properties.ProxyProperties;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.util.Proxys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.net.Proxy;
import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
@Component
public class ChatGPTUtil {

    private final ChatgptProperties chatgptProperties;

    private final ProxyProperties proxyProperties;

    private ChatGPT chatGPT;


    @PostConstruct
    public void init() {
        if (!StringUtils.isEmpty(proxyProperties.getIp())) {
            //如果在国内访问，使用这个,在application.yml里面配置
            Proxy proxy = Proxys.http(proxyProperties.getIp(), proxyProperties.getPort());
            chatGPT = ChatGPT.builder()
                    .apiKeyList(chatgptProperties.getSecretKey())
                    .timeout(600)
                    .proxy(proxy)
                    .apiHost("https://api.openai.com/") //代理地址
                    .build()
                    .init();
        } else {
            chatGPT = ChatGPT.builder()
                    .apiKeyList(chatgptProperties.getSecretKey())
                    .timeout(600)
                    .apiHost("https://api.openai.com/") //代理地址
                    .build()
                    .init();
        }

    }

    public Message chat(String userMessage, String user) throws RuntimeException {
        Message message = Message.of(userMessage);

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .user(user)
                .messages(Arrays.asList(message))
                .maxTokens(3000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        return response.getChoices().get(0).getMessage();
    }
}
