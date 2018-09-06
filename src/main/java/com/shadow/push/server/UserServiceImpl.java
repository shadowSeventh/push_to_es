package com.shadow.push.server;

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

//        JSONObject userInfo = new JSONObject();
//        for (int i = 0; i < 10; i++) {
//            userInfo.put("userName", i);
//            userInfo.put("passWord", i + "123");
//            userMapper.insert(userInfo);
//        }

//        userMapper.updateName("qwertyt", 12);
//        userMapper.updatePassWordByName("aaqwertyta", "aaa", 12);
        userMapper.updatePassWordByNameAndPassword("aaqwertyta", "aaa", "bbb", 12);
//        userMapper.delete(11);


//        userInfo.put("userName", "aaa");
//        userInfo.put("passWord", "bbb");
//        userInfo.put("id", 11);
//        userMapper.insert(userInfo);

    }
}
