package com.hrids.ctrbt.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * chatgpt配置
 *
 * @author ben
 * @date 2023/03/04
 */
@Data
@Component
@ConfigurationProperties("openai")
public class ChatgptProperties {
    private List<String> secretKey;
}
