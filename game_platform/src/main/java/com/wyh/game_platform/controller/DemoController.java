package com.wyh.game_platform.controller;



import com.wyh.game_platform.config.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @program: game_platform
 * @description:
 * @author: wuyinhao
 * @create:2021-04-15-20:48
 **/
@RestController
@CrossOrigin
@RequestMapping("/demo")
public class DemoController {



    @GetMapping("/index")
    public ResponseEntity<String> index(){
        return ResponseEntity.ok("请求成功");
    }

//    @RequestMapping("/open")
//    public void open(){
//        WebSocketServer.addOnlineCount();
//    }



//    @RequestMapping("/push")
//    public ResponseEntity<String> pushToWeb(String message, @PathVariable String toUserId) throws IOException {
//        WebSocketServer.sendInfo(message,toUserId);
//        return ResponseEntity.ok("MSG SEND SUCCESS");
//    }
}

