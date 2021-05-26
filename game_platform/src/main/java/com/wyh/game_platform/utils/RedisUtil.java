package com.wyh.game_platform.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    //写入数据,不设置过期时间
    public void setData(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    //写入数据,并设置过期时间,单位：秒
    public void setDataAndExpireTime(String key, String value, Integer time){
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    public void setExpireTime(String key, Integer time){
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    //获取数据
    public Object getData(String key){
        return redisTemplate.opsForValue().get(key);
    }

    //写入集合数据(list)
    public void addListData(String key, ArrayList<String> datas){
        redisTemplate.opsForList().leftPushAll(key,datas);
    }

    //写入集合数据(单个)
    public void addListDataByOne(String key, String value){
        redisTemplate.opsForList().leftPush(key, value);
    }

    //count > 0 从表头开始向表尾搜索，移除与value相等的元素，数量为count
    //count < 0 从表尾开始向表头搜索，移除与value相等的元素，数量为count的绝对值
    //count = 0 移除表中所有与value相等的值
    public void delListDataByVal(String key, String value, int count){
        redisTemplate.opsForList().remove(key, count, value);
    }


    @Deprecated
    public void setListData(String key, ArrayList<String> datas){
        redisTemplate.opsForList().leftPushAll(key,datas);
    }

    /*
     *
     * 集合长度
     *
     *  */
    public long getListDataLength(String key){
        return redisTemplate.opsForList().size(key);
    }

    /*
     *
     * 删除并弹出左元素
     *
     * */
    public String leftPopData(String key){
        return  redisTemplate.opsForList().leftPop(key);
    }

    public List<String> getAllListData(String key){
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    //删除key
    public void delKey(String key){
        redisTemplate.delete(key);
    }

    //写入map数据
    public void addMapData(String key, Map<Object, Object> map){
        redisTemplate.opsForHash().putAll(key, map);
    }

    public Map<Object, Object> getMapValue(String key) {
        return redisTemplate.opsForHash().entries(key);
    }
}


