package com.wyh.game_platform.redis;

import com.wyh.game_platform.GamePlatformApplicationTests;
import com.wyh.game_platform.utils.RedisUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: game_platform
 * @description:
 * @author: wuyinhao
 * @create:2021-05-17-21:19
 **/
public class RedisTest extends GamePlatformApplicationTests {
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void test(){
        Map<Object, Object> map = new HashMap<>();
        map.put("stu1", "压压");
        map.put("stu3", "网");
        redisUtil.addMapData("map", map);

        for (Object key:redisUtil.getMapValue("map").keySet()){
            System.out.println(redisUtil.getMapValue("map").get(key));
        }

    }

    @Test
    public void test2() {
        List<String> list = redisUtil.getAllListData("list");
        for (String s:list){
            System.out.println(s);
        }
    }

    @Test
    public void test3() {
        List<String> list = new ArrayList<>();
        list.add("wyh");
        redisUtil.addListData("list", (ArrayList<String>) list);

    }




}
