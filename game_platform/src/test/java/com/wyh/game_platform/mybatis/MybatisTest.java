package com.wyh.game_platform.mybatis;

import com.wyh.game_platform.GamePlatformApplicationTests;
import com.wyh.game_platform.dao.SysUserDao;
import com.wyh.game_platform.pojo.SysUser;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: game_platform
 * @description:
 * @author: wuyinhao
 * @create:2021-05-18-17:16
 **/
public class MybatisTest extends GamePlatformApplicationTests {
    @Resource
    SysUserDao sysUserDao;


    @Test
    public void test(){
        List<SysUser> list = sysUserDao.findAll();
        for (SysUser user:list){
            System.out.println(user);
        }
    }
}
