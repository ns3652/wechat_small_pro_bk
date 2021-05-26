package com.wyh.game_platform.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wyh.game_platform.config.SocketServer;
import com.wyh.game_platform.service.base.IBaseSocketService;
import com.wyh.game_platform.utils.MatchUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: game_platform
 * @description:
 * @author: wuyinhao
 * @create:2021-05-26-10:48
 **/
@Service
public class FiveInARowInviteService implements IBaseSocketService {
    static Logger logger = Logger.getLogger(FiveInARowInviteService.class);
    @Autowired
    private MatchUtil matchUtil;

    @Override
    public void pushAll(String message) {
        logger.info("pushAll: "+message);
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                String room = jsonObject.getString("room");
                String fromUserName = jsonObject.getString("fromUserName");
                //追加发送人(防止串改)

                List<String> users = matchUtil.getRoomUsers(room);
                jsonObject.put("roomUsersTotal", users.size());
                logger.info("pushAll时的房间人数:"+users.size());
                logger.info(users.toString());
                //房间内至少有两人以上，对除了自己以外的用户进行群发
                if (users.size() >= 2){
                    users.remove(fromUserName);
                    logger.info(users.toString());
                    for (int i = 0; i < users.size(); i++) {
                        String toUserName = users.get(i);
                        if(StringUtils.isNotBlank(toUserName)&& SocketServer.webSocketMap.containsKey(toUserName)){
                            SocketServer.webSocketMap.get(toUserName).sendMessage(jsonObject.toJSONString());
                        }else{
                            //否则不在这个服务器上，发送到mysql或者redis
                            logger.info("请求的toUserName:"+toUserName+"不在该服务器上");
                        }
                    }
                }
                logger.info(jsonObject);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void push(String message, String username) {

    }
}
