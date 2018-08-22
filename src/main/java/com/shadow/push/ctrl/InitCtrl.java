package com.shadow.push.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.shadow.push.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitCtrl {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/init", method = RequestMethod.GET)
    public JSONObject init() {
        userService.insert();
        return null;
    }
}
