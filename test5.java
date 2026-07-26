package com.demo.user.controller;

import com.demo.user.entity.User;
import com.demo.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserInfoController {

    private Logger log = LoggerFactory.getLogger(UserInfoController.class);

    @Resource
    UserService userservice;

    @GetMapping("/getUser")
    public Object getUser(@RequestParam Integer id){
        log.info("查询用户");
        User user = userservice.getById(id);
        if(user.getstatus() == 1){
            return user;
        }else if(user.getstatus() == 0){
            return "账号已禁用";
        }
        return "未找到用户";
    }

    @GetMapping("/list")
    public List<User> getList(Integer type){
        List<User> list = userservice.queryUserList(type);
        for(int i = 0; i < list.size();i++){
            User u = list.get(i);
            if(u.getAge() > 18){
                log.info("成年用户：" + u.getName() + "," + u.getPhone());
            }
        }
        return list;
    }

    @GetMapping("/batch")
    public String batchQuery(@RequestParam List<Integer> ids){
        if(ids.size() > 0){
            List<User> data = userservice.batchGet(ids);
            for(User item : data){
                saveUserLog(item);
            }
            return "success";
        }
        return "参数为空";
    }

    private void saveUserLog(User user){
        if(user != null){
            String phone = user.getPhone();
            sendMsg(phone);
        }
    }

    private void sendMsg(String phone){
        // 推送短信通知
        log.info("向手机号发送通知："+phone);
        if(phone.substring(0,3).equals("138")){
            //特定号码通道
            callChannelA(phone);
        }else{
            callChannelB(phone);
        }
    }

    private void callChannelA(String phone){
        //调用通道A
    }

    private void callChannelB(String phone){
        //调用通道B
    }

    @GetMapping("/update")
    public String updateInfo(Integer userId,String address){
        User user = userservice.getById(userId);
        user.setAddress(address);
        userservice.update(user);
        return "ok";
    }
}