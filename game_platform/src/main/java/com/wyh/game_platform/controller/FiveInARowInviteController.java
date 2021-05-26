package com.wyh.game_platform.controller;

import com.alibaba.fastjson.JSONObject;
import com.wyh.game_platform.service.FiveInARowInviteService;
import com.wyh.game_platform.service.FiveInARowInviteSocketService;
import com.wyh.game_platform.utils.JsonUtil;
import com.wyh.game_platform.utils.MatchUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;

/**
 * @program: game_platform
 * @description:
 * @author: wuyinhao
 * @create:2021-05-26-10:42
 **/
@RestController
@RequestMapping("/five")
public class FiveInARowInviteController {
    static Logger logger = Logger.getLogger(FiveInARowInviteController.class);
    @Autowired
    private MatchUtil matchUtil;

    @Autowired
    private FiveInARowInviteService fiveInARowInviteService;

    @PostMapping("/createRoom")
    public JSONObject createRoom(String username){
        String room = matchUtil.createRoom(username);
        logger.info("create_room:" + room);
        JSONObject data = new JSONObject();
        data.put("room", room);
        data.put("gameUser1", username);

        return JsonUtil.toJSONObject(true, "200", "房间创建成功", data);
    }

    @PostMapping("/joinRoom")
    public JSONObject joinRoom(String username, String room){
        List<String> list = matchUtil.getRoomUsers(room);
        if (list.size() >= 2){
            return JsonUtil.toJSONObject(false, "200", "房间加入失败","房间已满员");
        }
        int num = matchUtil.addRoomUser(room, username);
        list = matchUtil.getRoomUsers(room);


        JSONObject message = new JSONObject();

        message.put("room", room);
        message.put("userNum", num);
        if (num == 2){
            message.put("gameUser1", list.get(list.size()-1));
            message.put("gameUser2", list.get(list.size()-2));
            message.put("curStepUser", list.get(list.size()-1));

            //广播
            fiveInARowInviteService.pushAll(message.toString());

        }

        return JsonUtil.toJSONObject(true, "200", "房间加入成功", message);
    }

    @PostMapping("/pushAll")
    public JSONObject pushAll(String message) throws IOException {

        fiveInARowInviteService.pushAll(message);
        return JsonUtil.toJSONObject(true, "200", "消息已发送", "");
    }
}
