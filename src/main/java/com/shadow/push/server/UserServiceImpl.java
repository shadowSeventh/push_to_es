package com.shadow.push.server;

import com.alibaba.fastjson.JSONObject;
import com.shadow.push.api.UserService;
import com.shadow.push.mapping.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void insert() {
        JSONObject userInfo = new JSONObject();
        userInfo.put("userName", "aaa");
        userInfo.put("passWord", "bbb");
        userMapper.insert(userInfo);

        for (int i = 0; i <10 ; i++) {
            userInfo.put("userName", i);
            userInfo.put("passWord", i+"123");
            userMapper.insert(userInfo);
        }


    }
}
