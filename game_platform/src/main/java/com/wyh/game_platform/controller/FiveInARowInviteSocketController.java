package com.wyh.game_platform.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wyh.game_platform.service.FiveInARowInviteSocketService;
import com.wyh.game_platform.utils.ApplicationContextUtils;

import com.wyh.game_platform.utils.JsonUtil;
import com.wyh.game_platform.utils.MatchUtil;
import com.wyh.game_platform.utils.WebSocketUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;



/**
 * @program: game_platform
 * @description:
 * @author: wuyinhao
 * @create:2021-05-17-17:25
 **/
//@ServerEndpoint("/five/invite")
//@RestController
//@RequestMapping("/five")
//@Lazy
public class FiveInARowInviteSocketController {
//    FiveInARowInviteSocketService fiveInARowInviteSocketService = (FiveInARowInviteSocketService)ApplicationContextUtils.getBean("fiveInARowInviteSocketService");
//    MatchUtil matchUtil = (MatchUtil)ApplicationContextUtils.getBean("matchUtil");
//
//    static Logger logger = Logger.getLogger(FiveInARowInviteSocketController.class);
//
//    @PostMapping("/createRoom")
//    public JSONObject createRoom(String username){
//        String room = matchUtil.createRoom(username);
//        logger.info("create_room:" + room);
//        JSONObject data = new JSONObject();
//        data.put("room", room);
//        data.put("gameUser1", username);
//
//        //data.put("curStepUser", username);
//        return JsonUtil.toJSONObject(true, "200", "房间创建成功", data);
//    }
//
//    @PostMapping("/joinRoom")
//    public JSONObject joinRoom(String username, String room){
//        int num = matchUtil.addRoomUser(room, username);
//        List<String> list = matchUtil.getRoomUsers(room);
//
//
//        JSONObject message = new JSONObject();
//
//        message.put("room", room);
//        message.put("userNum", num);
//        if (num == 2){
//            message.put("gameUser1", list.get(list.size()-1));
//            message.put("gameUser2", username);
//            message.put("curStepUser", list.get(list.size()-1));
//        }
//        //message.put("message",message);
//        //广播并更新房间人数
//        fiveInARowInviteSocketService.pushAll(message.toString());
//
//        return JsonUtil.toJSONObject(true, "200", "房间加入成功", message);
//    }
//
//    @PostMapping("/pushAll")
//    public JSONObject pushAll(String message) throws IOException {
//
//        fiveInARowInviteSocketService.pushAll(message);
//        return JsonUtil.toJSONObject(true, "200", "消息已发送", "");
//    }
//
//    /**
//     * 连接建立成功调用的方法*/
//    @OnOpen
//    public void onOpen(Session session) {
//        System.out.println("====链接++++++++++++++++");
//        fiveInARowInviteSocketService.webSocketOpen(session);
//    }
//
//    /**
//     * 连接关闭调用的方法
//     */
//    @OnClose
//    public void onClose() {
//        fiveInARowInviteSocketService.webSocketClose();
//    }
//
//    /**
//     * 收到客户端消息后调用的方法
//     *
//     * @param message 客户端发送过来的消息*/
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        fiveInARowInviteSocketService.webSocketOnMessage(message);
//    }
//
//    /**
//     *
//     * @param session
//     * @param error
//     */
//    @OnError
//    public void onError(Session session, Throwable error) {
//        fiveInARowInviteSocketService.webSocketOnError(session, error);
//    }

}
