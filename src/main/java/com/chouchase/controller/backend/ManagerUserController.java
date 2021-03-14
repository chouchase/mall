package com.chouchase.controller.backend;

import com.chouchase.common.Const;
import com.chouchase.common.ServerResponse;
import com.chouchase.domain.User;
import com.chouchase.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller()
@RequestMapping("/manage/user")
@ResponseBody
public class ManagerUserController {
    @Autowired
    UserService userService;
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ServerResponse<User> login(HttpSession session,String username, String password){
        if(StringUtils.isAnyBlank(username,password)){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        ServerResponse<User> response = userService.login(username,password);
        if(response.isSuccess() &&response.getData().getRole() == Const.Role.ADMIN){
            session.setAttribute(Const.CURRENT_USER,response.getData());
            return response;
        }else{
            return ServerResponse.createFailResponseByMsg("用户不存在");
        }
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ServerResponse<String> register(User user) {
        if(StringUtils.isAnyBlank(user.getUsername(),user.getPassword())){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        user.setRole(Const.Role.ADMIN);
        return userService.register(user);
    }
}
