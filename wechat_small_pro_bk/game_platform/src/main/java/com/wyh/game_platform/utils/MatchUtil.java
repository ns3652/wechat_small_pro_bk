package com.wyh.game_platform.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: game_platform
 * @description:
 * @author: wuyinhao
 * @create:2021-04-19-23:13
 **/
@Component
public class MatchUtil {

    //匹配对手
    //待修改，List效率低，后续修改为Map
    public synchronized String matchToUser(String userName){
        //防止@Component修饰下的类通过@Autowired为null现象发生
        RedisUtil redisUtil = (RedisUtil)ApplicationContextUtils.getBean("redisUtil");
        //获得空闲用户列表
        List<String> freeUserList = redisUtil.getAllListData("gameFreeUsers");
        int userIndex = freeUserList.indexOf(userName);

        //userList有空闲用户（除自己外），取队列中的第一个对手进行匹配
        if (freeUserList.size() > 1){
            freeUserList.remove(userIndex);     //删除自己
            String toUserName = freeUserList.remove(0);       //删除队列中的首个空闲对手
            //当队列中没有空闲用户时，删除redis的key：gameFreeUsers
            if (freeUserList.size() == 0){
                redisUtil.delKey("gameFreeUsers");
            }else{
                redisUtil.delKey("gameFreeUsers");
                redisUtil.setListData("gameFreeUsers", (ArrayList<String>) freeUserList);
            }
            return toUserName;
        }else{
            return null;
        }
    }

    //退出登录需要删除空闲用户
    public synchronized void deleteCacheUser(String userName){

        RedisUtil redisUtil = (RedisUtil)ApplicationContextUtils.getBean("redisUtil");
        //获得空闲用户列表，并删除userName
        List<String> userList = redisUtil.getAllListData("gameFreeUsers");
        int removeIndex = userList.indexOf(userName);
        if (removeIndex != -1){
            userList.remove(removeIndex);
        }else {
            return;
        }

        //当队列中没有空闲用户时，删除redis的key：gameFreeUsers
        if (userList.size() == 0){
            redisUtil.delKey("gameFreeUsers");
        }else{
            redisUtil.delKey("gameFreeUsers");
            redisUtil.setListData("gameFreeUsers", (ArrayList<String>) userList);
        }
    }

    //增加空闲用户到缓存队列中
    public synchronized void addCacheUser(String userName){
        RedisUtil redisUtil = (RedisUtil)ApplicationContextUtils.getBean("redisUtil");
        //获得空闲用户列表
        List<String> userList = redisUtil.getAllListData("gameFreeUsers");
        if (!userList.contains(userName)){
            userList.add(userName);
        }
        redisUtil.delKey("gameFreeUsers");
        redisUtil.setListData("gameFreeUsers", (ArrayList<String>) userList);

    }
}
