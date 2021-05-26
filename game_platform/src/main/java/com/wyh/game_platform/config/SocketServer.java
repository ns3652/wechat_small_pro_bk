package com.wyh.game_platform.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: game_platform
 * @description:
 * @author: wuyinhao
 * @create:2021-05-26-10:17
 **/

@ServerEndpoint("/five/invite")
@Component
public class SocketServer {
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    public static ConcurrentHashMap<String,SocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**userName*/
    private String userName="";

    static Logger logger = Logger.getLogger(SocketServer.class);
//
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
        userName = session.getUserPrincipal().getName();
        this.session = session;

        this.userName=userName;
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
        logger.info("WebSocketServer open:" + this);

        //System.out.println("用户连接:"+userName+",当前在线人数为:" + getOnlineCount());
        logger.info("用户连接:"+userName+",当前在线人数为:" + getOnlineCount());

        try {
            JSONObject message = new JSONObject();
            message.put("userName", userName);
            message.put("socketStatus", "Connected");

            webSocketMap.get(userName).sendMessage(message.toJSONString());
        } catch (IOException e) {
            logger.error("用户:"+userName+",网络异常!!!!!!");
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
        }
        logger.info("用户退出:"+userName+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("用户消息:"+userName+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);

                //心跳包检测
                String alive = jsonObject.getString("alive");
                String userName = jsonObject.getString("userName");
                if (StringUtils.isNotBlank(alive)){
                    JSONObject obj = new JSONObject();
                    obj.put("socketStatus", "Connected");
                    obj.put("userName", userName);
                    webSocketMap.get(userName).sendMessage(obj.toJSONString());
                    return;
                }

                //追加发送人(防止串改)
                jsonObject.put("fromUserName",this.userName);
                String toUserName=jsonObject.getString("toUserName");
                //传送给对应toUserName用户的websocket
                if(StringUtils.isNotBlank(toUserName)&&webSocketMap.containsKey(toUserName)){
                    webSocketMap.get(toUserName).sendMessage(jsonObject.toJSONString());
                }else{
                    logger.info("请求的toUserName:"+toUserName+"不在该服务器上");
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
        logger.info("用户错误:"+this.userName+",原因:"+error.getMessage());
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
    public static void sendInfo(String message, String userName) throws IOException {
        logger.info("发送消息到:"+userName+"，报文:"+message);
        if(StringUtils.isNotBlank(userName)&&webSocketMap.containsKey(userName)){
            webSocketMap.get(userName).sendMessage(message);
        }else{
            logger.info("用户:"+userName+",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        SocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        SocketServer.onlineCount--;
    }
}
