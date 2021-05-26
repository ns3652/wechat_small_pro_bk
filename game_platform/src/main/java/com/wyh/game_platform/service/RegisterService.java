package com.wyh.game_platform.service;


import com.wyh.game_platform.dao.SysUserDao;
import com.wyh.game_platform.dao.SysUserRoleDao;
import com.wyh.game_platform.exception.base.BaseDefineException;
import com.wyh.game_platform.exception.base.BaseErrorCode;
import com.wyh.game_platform.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class RegisterService {
    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private SysUserRoleDao sysUserRoleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerNewUser(String username, String password){
        SysUser sysUser = sysUserDao.findByUsername(username);
        if (StringUtils.isEmpty(sysUser)){
            String encodePassword = passwordEncoder.encode(password);
            SysUser newUser = new SysUser(username, encodePassword);
            //sysUserDao.insertNewUser(username, encodePassword);
            sysUserDao.insertNewUser(newUser);
            //sysUser = sysUserDao.findByUsername(username);
            sysUserRoleDao.insertDefaultUserRole(newUser.getUserId());
        }else{
            throw new BaseDefineException(BaseErrorCode.USER_IS_EXIST_EXCEPTION);
        }
    }
}
