package com.wyh.game_platform.service;

import com.alibaba.fastjson.JSONObject;

import com.wyh.game_platform.dao.SysRolePermissionDao;
import com.wyh.game_platform.dao.SysUserAuthsDao;
import com.wyh.game_platform.dao.SysUserDao;
import com.wyh.game_platform.dao.SysUserRoleDao;
import com.wyh.game_platform.enums.ThirdPartyTypeEnum;
import com.wyh.game_platform.exception.base.BaseDefineException;
import com.wyh.game_platform.exception.base.BaseErrorCode;
import com.wyh.game_platform.pojo.SysPermission;
import com.wyh.game_platform.pojo.SysRole;
import com.wyh.game_platform.pojo.SysUser;
import com.wyh.game_platform.pojo.SysUserAuths;
import com.wyh.game_platform.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
public class ThirdPartyService {
    @Value("${weChat.appId}")
    private String weChatAppId;

    @Value("${weChat.secret}")
    private String weChatSecret;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private SysUserAuthsDao sysUserAuthsDao;

    @Resource
    private SysUserRoleDao sysUserRoleDao;

    @Resource
    private SysRolePermissionDao sysRolePermissionDao;

    @Resource
    private SysUserDao sysUserDao;



    /*
    * 微信第三方登录
    * 首次登录会自动绑定生成一个平台账号（账号和密码随机生成）
    * 第二次之后登录可以直接登录
    * */
    public JSONObject weChatLogin(WeChatLoginRequest weChatLoginRequest){
        StringBuffer sb = new StringBuffer("https://api.weixin.qq.com/sns/jscode2session?");
        sb.append("appid=").append(weChatAppId)
                .append("&secret=").append(weChatSecret)
                .append("&js_code=").append(weChatLoginRequest.getJscode())
                .append("&grant_type=authorization_code");
        try {
            JSONObject res = HttpUtil.sendGet(sb.toString());
            String openid = (String) res.get("openid");
            if (!StringUtils.isEmpty(openid)){
                SysUserAuths userAuths = sysUserAuthsDao.findUserAuthsByIdentityTypeAndOpenIdCredential(ThirdPartyTypeEnum.WECHAT.getCode(), openid);
                Map<String, Object> resMap = new HashMap<>();

                if (userAuths != null){
                    resMap.put("token", "Bearer " + getJwtToken(userAuths));
                    weChatLoginRequest.setNickName(userAuths.getNickname());
                }else{
                    resMap.put("token", "Bearer " + getJwtTokenByAddWeChatUserAuths(openid, weChatLoginRequest));
                }

                resMap.put("userInfo", weChatLoginRequest);
                return JsonUtil.toJSONObject(true, "200", "微信登录成功", resMap);
            }else{
                throw new RuntimeException(res.getString("errmsg"));
            }
        }catch (IOException e){
            throw new BaseDefineException(BaseErrorCode.WECHAT_LOGIN_SERVER_EXCEPTION);
        }
    }

    private String getJwtToken(SysUserAuths userAuths){
        if (userAuths.getUserId() == null){
            throw new BaseDefineException(BaseErrorCode.THIRD_PARTY_NOT_BIND_USER);
        }
        SysUser sysUser = sysUserDao.findById(userAuths.getUserId());
        return getJwtToken(sysUser);
    }

    private String getJwtToken(SysUser sysUser){
        if (sysUser == null){
            throw new BaseDefineException(BaseErrorCode.THIRD_PARTY_NOT_BIND_USER);
        }
        if (sysUser != null){
            List<SysRole> userRoles = sysUserRoleDao.findRoleListByUserId(sysUser.getUserId());
            Set<GrantedAuthority> authorities = new HashSet<>();
            for (SysRole role:userRoles){
                List<SysPermission> permissions = sysRolePermissionDao.findPermissionListByRoleId(role.getRoleId());
                permissions.stream().forEach(auth->{
                    authorities.add(new SimpleGrantedAuthority(auth.getPermissionName()));
                });
            }
            UserDetails userDetails = new User(sysUser.getUsername(), sysUser.getPassword(), authorities);
            return jwtTokenUtil.generateToken(userDetails);
        }else{
            throw new BaseDefineException(BaseErrorCode.THIRD_PARTY_NOT_BIND_USER);
        }
    }

    /*
    * 1.新增一个平台用户
    * 2.将第三方微信信息与该用户自动绑定
    * */
    private String getJwtTokenByAddWeChatUserAuths(String openid, WeChatLoginRequest weChatLoginRequest){
        weChatLoginRequest.setNickName(weChatLoginRequest.getNickName() + CommonUtil.getRandomNum(10));
        SysUser sysUser = new SysUser(weChatLoginRequest.getNickName(), passwordEncoder.encode(CommonUtil.getRandomNum(15)));
        sysUserDao.insertNewUser(sysUser);
        sysUserAuthsDao.insertNewSysUserAuths(sysUser.getUserId(), ThirdPartyTypeEnum.WECHAT.getCode(), ThirdPartyTypeEnum.WECHAT.getDescription(),
                                            "openid", openid, weChatLoginRequest.getAvatarUrl(), weChatLoginRequest.getNickName(), weChatLoginRequest.getGender());
        sysUserRoleDao.insertDefaultUserRole(sysUser.getUserId());
        return getJwtToken(sysUser);
    }
}
