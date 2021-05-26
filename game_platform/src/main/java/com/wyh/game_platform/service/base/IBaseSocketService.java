package com.wyh.game_platform.service.base;

import javax.websocket.Session;


public interface IBaseSocketService {
    void pushAll(String message);

    void push(String message, String username);
}
