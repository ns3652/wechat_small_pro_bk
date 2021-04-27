package com.wyh.game_platform.config;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wyh.game_platform.utils.ApplicationContextUtils;
import com.wyh.game_platform.utils.MatchUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;



/**
 * @program: game_platform
 * @description:
 * @author: wuyinhao
 * @create:2021-04-15-17:01
 **/
@ServerEndpoint("/imserver")
@Component
public class WebSocketServer {

    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**userName*/
    private String userName="";

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
        userName = session.getUserPrincipal().getName();
        this.session = session;
        //this.userName=userName;
        if(webSocketMap.containsKey(userName)){
            webSocketMap.remove(userName);
            webSocketMap.put(userName,this);
            //加入set中
        }else{
            webSocketMap.put(userName,this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }
        MatchUtil matchUtil = (MatchUtil)ApplicationContextUtils.getBean("matchUtil");
        matchUtil.addCacheUser(userName);

        System.out.println("用户连接:"+userName+",当前在线人数为:" + getOnlineCount());
        try {
            //sendMessage("连接成功");匹配用户
            JSONObject jsonObject = new JSONObject();
            JSONObject message = new JSONObject();
            message.put("msg", "连接成功");

            jsonObject.put("userName", userName);

            String toUserName = matchUtil.matchToUser(userName);
            jsonObject.put("toUserName", toUserName==null?"":toUserName);
            //从两个用户中随机选择一个用户先走
            if (toUserName != null && !toUserName.equals("")){
                //String curStepUser = new Random().nextInt(10) % 2 == 0?userName:toUserName;
                String curStepUser = userName;
                message.put("curStepUser", curStepUser);
            }else{
                message.put("curStepUser", "");
            }
            jsonObject.put("message",message);
            webSocketMap.get(userName).sendMessage(jsonObject.toJSONString());
        } catch (IOException e) {
            System.out.println("用户:"+userName+",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userName)){
            webSocketMap.remove(userName);

            //从set中删除
            subOnlineCount();
            MatchUtil matchUtil = (MatchUtil)ApplicationContextUtils.getBean("matchUtil");
            matchUtil.deleteCacheUser(userName);

        }
        System.out.println("用户退出:"+userName+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        MatchUtil matchUtil = (MatchUtil)ApplicationContextUtils.getBean("matchUtil");

        System.out.println("用户消息:"+userName+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                System.out.println(jsonObject);
                //追加发送人(防止串改)
                jsonObject.put("userName",this.userName);
                String toUserName=jsonObject.getString("toUserName");
                //传送给对应toUserName用户的websocket
                if(StringUtils.isNotBlank(toUserName)&&webSocketMap.containsKey(toUserName)){
//                    jsonObject.put("type","2");
                    webSocketMap.get(toUserName).sendMessage(jsonObject.toJSONString());
                }else{
                    //System.out.println("请求的userName:"+toUserName+"不在该服务器上");
                    matchUtil.deleteCacheUser(toUserName);
                    //throw new RuntimeException("请求的userName:"+toUserName+"不在该服务器上");
                    System.out.println("请求的userName:"+toUserName+"不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("用户错误:"+this.userName+",原因:"+error.getMessage());
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message,@PathParam("userName") String userName) throws IOException {
        System.out.println("发送消息到:"+userName+"，报文:"+message);
        if(StringUtils.isNotBlank(userName)&&webSocketMap.containsKey(userName)){
            webSocketMap.get(userName).sendMessage(message);
        }else{
            System.out.println("用户"+userName+",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}

