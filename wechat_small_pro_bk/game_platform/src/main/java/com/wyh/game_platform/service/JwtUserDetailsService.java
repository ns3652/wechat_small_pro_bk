package com.wyh.game_platform.service;

import com.wyh.game_platform.dao.SysRolePermissionDao;
import com.wyh.game_platform.dao.SysUserDao;
import com.wyh.game_platform.dao.SysUserRoleDao;
import com.wyh.game_platform.pojo.SysPermission;
import com.wyh.game_platform.pojo.SysRole;
import com.wyh.game_platform.pojo.SysUser;
import com.wyh.game_platform.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Value("${redis.timeout}")
    private String timeout;

    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private SysUserRoleDao sysUserRoleDao;

    @Resource
    private SysRolePermissionDao sysRolePermissionDao;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = null;
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (redisUtil.getListDataLength(username) > 0){         //读取redis缓存
            //第一元素为密码，后面元素为权限
            List<String> dataList = redisUtil.getAllListData(username);
            String password = dataList.remove(dataList.size() - 1);
            sysUser = new SysUser(username, password);
            for (String auth:dataList){
                authorities.add(new SimpleGrantedAuthority(auth));
            }
        }else {
            sysUser = sysUserDao.findByUsername(username);
            ArrayList<String> dataList = new ArrayList<>();
            System.out.println("=======================");
            if (StringUtils.isEmpty(sysUser)){
                throw new UsernameNotFoundException("SysUser not found with username: " + username);
            }else{
                dataList.add(sysUser.getPassword());
                List<SysRole> userRoles = sysUserRoleDao.findRoleListByUserId(sysUser.getUserId());

                for (SysRole role:userRoles){
                    List<SysPermission> permissions = sysRolePermissionDao.findPermissionListByRoleId(role.getRoleId());
                    permissions.stream().forEach(auth->{
                        dataList.add(auth.getPermissionName());
                        authorities.add(new SimpleGrantedAuthority(auth.getPermissionName()));
                    });
                }
            }
            redisUtil.setListData(username, dataList);
            redisUtil.setExpireTime(username, Integer.valueOf(timeout));     //设置redis缓存过期时间
        }
        return new User(username, sysUser!=null?sysUser.getPassword():null, authorities);
    }
}