package com.wyh.game_platform.controller;

import com.alibaba.fastjson.JSONObject;

import com.wyh.game_platform.service.JwtUserDetailsService;
import com.wyh.game_platform.service.ThirdPartyService;
import com.wyh.game_platform.utils.JsonUtil;
import com.wyh.game_platform.utils.JwtRequest;
import com.wyh.game_platform.utils.JwtTokenUtil;
import com.wyh.game_platform.utils.WeChatLoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;


@RestController
@CrossOrigin
@RequestMapping("/auth")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private ThirdPartyService thirdPartyService;

//    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
//    public JSONObject createAuthenticationToken(JwtRequest authenticationRequest) throws Exception {
//
//        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//
//        final UserDetails userDetails = userDetailsService
//                .loadUserByUsername(authenticationRequest.getUsername());
//
//        final String token = jwtTokenUtil.generateToken(userDetails);
//
//        return JsonUtil.toJSONObject(true, "200", "", ResponseEntity.ok(new JwtResponse(token)));
//    }

    /*
    *
    * 账号密码登录
    *
    * */
    @PostMapping("/login")
    public JSONObject createAuthenticationToken(JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        JSONObject data = new JSONObject();
        data.put("token", "Bearer " + token);
        data.put("userName", authenticationRequest.getUsername());

        return JsonUtil.toJSONObject(true, "200", "登录成功", data);
    }

    /*
    *
    * 微信登录
    *
    * */
    @PostMapping("/weChatLogin")
    public JSONObject weChatLogin(String jscode, String nickName, String avatarUrl, int gender, String language, String country, String province, String city) throws Exception {
        return thirdPartyService.weChatLogin(new WeChatLoginRequest(jscode, nickName, avatarUrl, gender, language, country, province, city));
    }


    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}