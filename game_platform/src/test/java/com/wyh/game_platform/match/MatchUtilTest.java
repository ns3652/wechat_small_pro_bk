package com.wyh.game_platform.match;

import com.wyh.game_platform.GamePlatformApplicationTests;
import com.wyh.game_platform.pojo.SysUser;
import com.wyh.game_platform.utils.MatchUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @program: game_platform
 * @description:
 * @author: wuyinhao
 * @create:2021-05-18-21:33
 **/
public class MatchUtilTest extends GamePlatformApplicationTests {
    @Autowired
    private MatchUtil matchUtil;

    @Test
    public void addRoom(){
        String room = matchUtil.createRoom("root");
        System.out.println("room:"+room);
        List<String> users = matchUtil.getRoomUsers(room);

        System.out.println("user:"+users);

        matchUtil.addRoomUser(room, "wyh");
        matchUtil.addRoomUser(room, "lisi");
        users = matchUtil.getRoomUsers(room);
        System.out.println(users);

    }

    @Test
    public void delRoomUser(){
        List<String> users = matchUtil.getRoomUsers("69226836");
        System.out.println(users);

        matchUtil.addRoomUser("69226836", "wyh");
        matchUtil.addRoomUser("69226836", "wyh");
        users = matchUtil.getRoomUsers("69226836");
        System.out.println(users);

        matchUtil.delRoomUser("69226836", "wyh");
        users = matchUtil.getRoomUsers("69226836");
        System.out.println(users);
    }


}
