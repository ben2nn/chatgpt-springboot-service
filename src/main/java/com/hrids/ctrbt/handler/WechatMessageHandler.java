package com.hrids.ctrbt.handler;

import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
import com.hrids.ctrbt.util.ChatGPTStrreamUtil;
import com.hrids.ctrbt.util.ChatGPTUtil;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.exception.ChatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;

/**
 * 微信消息处理程序
 *
 * @author ben
 * @date 2023/03/19
 */
@Slf4j
@Component
public class WechatMessageHandler implements IMsgHandlerFace {

    @Resource
    private ChatGPTUtil chatGPTUtil;

    private static final String FROM_USER = "Wechat";

    @Override
    public String textMsgHandle(BaseMsg baseMsg) {
        //如果是在群聊
        if (baseMsg.isGroupMsg()) {
            //存在@机器人的消息就向ChatGPT提问
            if (baseMsg.getText().contains("@" + Core.getInstance().getNickName())) {
                //去除@再提问
                String prompt = baseMsg.getText().replace("@" + Core.getInstance().getNickName() + " ", "").trim();
                log.info("接收到的群聊消息是:{}", prompt);
                return sendGPTMsg(prompt);
            }
        } else {
            log.info("接收到的消息是:{}", baseMsg.getText());
            //不是在群聊 则直接回复
            return sendGPTMsg(baseMsg.getText());
        }
        return null;
    }

    public String sendGPTMsg(String prompt) {
        String response;
        try {
            Message message = chatGPTUtil.chat(prompt, FROM_USER);
            response = message.getContent();
        } catch (ChatException e) {
            response = "出错了，请重试";
        }
        return response;
    }


    @Override
    public String picMsgHandle(BaseMsg baseMsg) {
        return null;
    }

    @Override
    public String voiceMsgHandle(BaseMsg baseMsg) {
        return null;
    }

    @Override
    public String viedoMsgHandle(BaseMsg baseMsg) {
        return null;
    }

    @Override
    public String nameCardMsgHandle(BaseMsg baseMsg) {
        return null;
    }

    @Override
    public void sysMsgHandle(BaseMsg baseMsg) {

    }

    @Override
    public String verifyAddFriendMsgHandle(BaseMsg baseMsg) {
        return null;
    }

    @Override
    public String mediaMsgHandle(BaseMsg baseMsg) {
        return null;
    }
}
