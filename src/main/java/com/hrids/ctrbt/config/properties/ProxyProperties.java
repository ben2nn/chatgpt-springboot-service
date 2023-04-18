package com.hrids.ctrbt.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 代理配置
 *
 * @author ashinnotfound
 * @date 2023/03/04
 */
@Data
@Component
@ConfigurationProperties("proxy")
public class ProxyProperties {
    private String ip;
    private int port;
}
