package com.hrids.ctrbt.config;

import cn.zhouyafeng.itchat4j.Wechat;
import com.hrids.ctrbt.config.properties.ChatgptProperties;
import com.hrids.ctrbt.config.properties.ProxyProperties;
import com.hrids.ctrbt.config.properties.QqProperties;
import com.hrids.ctrbt.config.properties.WechatProperties;
import com.hrids.ctrbt.handler.WechatMessageHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * bot配置
 *
 * @author ben
 * @date 2023/02/13
 */
@Slf4j
@Data
@Component
public class BotConfig {
    @Resource
    WechatProperties wechatConfig;

    @Resource
    private WechatMessageHandler wechatMessageHandler;

    @PostConstruct
    public void init() {
        //微信
        if (wechatConfig.getEnable()) {
            log.info("正在登录微信,请按提示操作：");
            Wechat wechatBot = new Wechat(wechatMessageHandler, wechatConfig.getQrPath());
            wechatBot.start();
        }
    }
}
