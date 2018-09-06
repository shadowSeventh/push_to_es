package com.shadow.push.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.shadow.push.api.UserService;
import com.shadow.push.readFiles.TableManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitCtrl {

    @Autowired
    private UserService userService;

    @Autowired
    private TableManager tableManager;

    @RequestMapping(path = "/init", method = RequestMethod.GET)
    public JSONObject init() {
//        http://ozqzyzixv.bkt.clouddn.com/Untitled-1

        tableManager.getMapping("http://ozqzyzixv.bkt.clouddn.com/Untitled-1", true);
        userService.insert();
        return null;
    }
}
