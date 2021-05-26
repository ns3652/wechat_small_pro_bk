package com.wyh.game_platform.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wyh.game_platform.service.base.IBaseSocketService;
import com.wyh.game_platform.utils.MatchUtil;
import com.wyh.game_platform.utils.WebSocketUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: game_platform
 * @description:
 * @author: wuyinhao
 * @create:2021-05-18-20:48
 **/
//@Service
public class FiveInARowInviteSocketService {
//    @Autowired
//    private WebSocketUtils webSocketUtils;
//
//    @Autowired
//    private MatchUtil matchUtil;
//
//    static Logger logger = Logger.getLogger(FiveInARowInviteSocketService.class);
//
//    @Override
//    public void webSocketOpen(Session session) {
//        logger.info("webSocketOpen_webSocketUtils:" + webSocketUtils);
//        webSocketUtils.setUserName(session.getUserPrincipal().getName());
//        webSocketUtils.setSession(session);
//        ConcurrentHashMap<String, WebSocketUtils> webSocketMap = WebSocketUtils.getWebSocketMap();
//        String userName = webSocketUtils.getUserName();
//
//        if(webSocketMap.containsKey(userName)){
//            webSocketMap.remove(userName);
//            //加入set中
//            webSocketMap.put(userName,webSocketUtils);
//        }else{
//            //加入set中
//            webSocketMap.put(userName,webSocketUtils);
//            //在线数加1
//            webSocketUtils.addOnlineCount();
//        }
//        logger.info("用户连接:"+userName+",当前在线人数为:" + webSocketUtils.getOnlineCount());
//
//
//        try {
//            JSONObject message = new JSONObject();
//            message.put("socketStatus", "Connected");
//
//            message.put("userName", userName);
//
//            webSocketMap.get(userName).sendMessage(message.toJSONString());
//        } catch (IOException e) {
//            logger.error("用户:"+userName+",网络异常!!!!!!");
//        }
//    }
//
//    @Override
//    public void webSocketClose() {
//        //this.session = session;
//        ConcurrentHashMap<String, WebSocketUtils> webSocketMap = WebSocketUtils.getWebSocketMap();
//        String userName = webSocketUtils.getUserName();
//
//        if(webSocketMap.containsKey(userName)){
//            webSocketMap.remove(userName);
//            //从set中删除
//            webSocketUtils.subOnlineCount();
//        }
//        logger.info("用户退出:"+userName+",当前在线人数为:" + webSocketUtils.getOnlineCount());
//    }
//
//    @Override
//    public void webSocketOnMessage(String message) {
//        pushAll(message);
//    }
//
//    @Override
//    public void webSocketOnError(Session session, Throwable error) {
//        String userName = webSocketUtils.getUserName();
//        logger.error("用户错误:" + userName + ",原因:"+error.getMessage());
//
//        ConcurrentHashMap<String, WebSocketUtils> webSocketMap = WebSocketUtils.getWebSocketMap();
//
//        if(webSocketMap.containsKey(userName)){
//            webSocketMap.remove(userName);
//            //从set中删除
//            webSocketUtils.subOnlineCount();
//        }
//        logger.info("用户退出:"+userName+",当前在线人数为:" + webSocketUtils.getOnlineCount());
//
//        error.printStackTrace();
//    }
//
//    //主动群发
//    @Override
//    public void pushAll(String message) {
//        ConcurrentHashMap<String, WebSocketUtils> webSocketMap = WebSocketUtils.getWebSocketMap();
//        //可以群发消息
//        //消息保存到数据库、redis
//        if(StringUtils.isNotBlank(message)){
//            try {
//                //解析发送的报文
//                JSONObject jsonObject = JSON.parseObject(message);
//                String room = jsonObject.getString("room");
//                //追加发送人(防止串改)
//                jsonObject.put("fromUserName",webSocketUtils.getUserName());
//                List<String> users = matchUtil.getRoomUsers(room);
//                jsonObject.put("roomUsersTotal", users.size());
//                logger.info("pushAll时的房间人数:"+users.size());
//                logger.info(users.toString());
//                //房间内至少有两人以上，对除了自己以外的用户进行群发
//                if (users.size() >= 2){
//                    users.remove(webSocketUtils.getUserName());
//                    logger.info(users.toString());
//                    for (int i = 0; i < users.size(); i++) {
//                        String toUserName = users.get(i);
//                        if(StringUtils.isNotBlank(toUserName)&&webSocketMap.containsKey(toUserName)){
//                            webSocketMap.get(toUserName).sendMessage(jsonObject.toJSONString());
//                        }else{
//                            //否则不在这个服务器上，发送到mysql或者redis
//                            logger.info("请求的userName:"+toUserName+"不在该服务器上");
//                        }
//                    }
//                }
//                logger.info(jsonObject);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void push(String message, String username) {
//
//    }
}
