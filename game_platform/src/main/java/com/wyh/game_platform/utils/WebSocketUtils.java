package com.wyh.game_platform.utils;

import com.wyh.game_platform.service.FiveInARowInviteSocketService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: game_platform
 * @description:
 * @author: wuyinhao
 * @create:2021-05-17-17:32
 **/
@Component
@Scope("prototype")
public class WebSocketUtils {
    static Logger logger = Logger.getLogger(WebSocketUtils.class);
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String, WebSocketUtils> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**userName*/
    private String userName="";

    public void setSession(Session session) {
        this.session = session;
    }

    private WebSocketUtils(){}

    public String getUserName(){
        return userName;
    }

    public static synchronized ConcurrentHashMap<String, WebSocketUtils> getWebSocketMap(){
        return webSocketMap;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketUtils.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketUtils.onlineCount--;
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
    public static void sendInfo(String message,String userName) throws IOException {

        logger.info("发送消息到:"+userName+"，报文:"+message);
        if(StringUtils.isNotBlank(userName)&&webSocketMap.containsKey(userName)){
            webSocketMap.get(userName).sendMessage(message);
        }else{
            logger.info("用户"+userName+",不在线！");
        }
    }

}
