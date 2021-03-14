package com.chouchase.controller.portal;

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

@Controller
@RequestMapping(value = "/user")
@ResponseBody
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ServerResponse<String> register(User user) {
        //参数校验,用户名和密码不能为空
        if(StringUtils.isAnyBlank(user.getUsername(),user.getPassword())){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        //防止恶意调用
        user.setRole(Const.Role.CUSTOMER);
        user.setUpdateTime(null);
        user.setCreateTime(null);
        user.setId(null);

        return userService.register(user);
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        //参数校验
        if(StringUtils.isAnyBlank(username,password)){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        //获取登录结果
        ServerResponse<User> response = userService.login(username, password);
        //登陆成功，用户信息加入Session
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ServerResponse<String> logout(HttpSession session) {
        //从Session对象中移除当前用户
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createSuccessResponseByMsg("退出成功");
    }

    @RequestMapping(value = "/check/username", method = RequestMethod.GET)
    public ServerResponse<String> checkUsername(String username) {
        //参数校验
        if(StringUtils.isBlank(username)){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }

        return userService.checkUsername(username);
    }

    @RequestMapping(value = "/check/phone", method = RequestMethod.GET)
    public ServerResponse<String> checkPhone(String phone) {
        //参数校验
        if(StringUtils.isBlank(phone)){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }

        return userService.checkPhone(phone);
    }

    @RequestMapping(value = "/check/email", method = RequestMethod.GET)
    public ServerResponse<String> checkEmail(String email) {
        //参数校验
        if(StringUtils.isBlank(email)){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }

        return userService.checkEmail(email);
    }


    @RequestMapping(value = "/forget/get_question", method = RequestMethod.GET)
    public ServerResponse<String> getQuestion(String username) {
        //参数校验
        if(StringUtils.isBlank(username)){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        return userService.getQuestion(username);
    }

    @RequestMapping(value = "/forget/check_answer", method = RequestMethod.POST)
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        //参数校验
        if(StringUtils.isAnyBlank(username,question,answer)){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        return userService.checkAnswer(username, question, answer);
    }

    @RequestMapping(value = "/forget/reset_password", method = RequestMethod.POST)
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String resetToken) {
        //参数校验
        if(StringUtils.isAnyBlank(username,newPassword,resetToken)){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }

        return userService.resetPassword(username, newPassword, resetToken);
    }

    @RequestMapping(value = "/reset_password", method = RequestMethod.POST)
    public ServerResponse<String> updatePassword(HttpSession session, String oldPassword, String newPassowrd) {
        //参数校验
        if(StringUtils.isAnyBlank(oldPassword,newPassowrd)){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        //获取当前登录的用户
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //通过用户Id修改密码
        return userService.updatePassword(user.getId(), oldPassword, newPassowrd);

    }

    @RequestMapping(value = "/get_user_info", method = RequestMethod.GET)
    public ServerResponse<User> getUserInfo(HttpSession session) {
        //从Session中获取当前登录的用户
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return ServerResponse.createSuccessResponseByMsgAndData("获取成功", user);
    }

    @RequestMapping(value = "/update_user_info", method = RequestMethod.POST)
    public ServerResponse<User> updateUserInfo(User user, HttpSession session) {
        //获取当前登录用户
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        //防止接口恶意调用
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        user.setPassword(null);
        user.setAnswer(null);
        user.setQuestion(null);
        user.setRole(null);
        //更新信息
        ServerResponse<User> response = userService.updateUserInfo(user);

        //如果更新成功，将更新后的用户加入Session
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

}
