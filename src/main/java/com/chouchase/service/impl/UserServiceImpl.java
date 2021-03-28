package com.chouchase.service.impl;

import com.chouchase.common.ServerResponse;
import com.chouchase.common.TokenCache;
import com.chouchase.dao.UserDao;
import com.chouchase.domain.User;
import com.chouchase.service.UserService;
import com.chouchase.util.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service

public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    public ServerResponse<String> checkUsername(String username) {
        //查询用户名
        int count = userDao.checkUsername(username);

        if (count > 0) {
            return ServerResponse.createFailResponseByMsg("用户名已存在");
        } else {
            return ServerResponse.createSuccessResponseByMsg("校验成功");
        }
    }



    @Override
    public ServerResponse<String> register(User user) {
        //检查用户名是否存在
        if (userDao.checkUsername(user.getUsername()) > 0) {
            return ServerResponse.createFailResponseByMsg("用户名已存在");
        }

        user.setPassword(MD5Utils.encrypt(user.getPassword()));
        //加入数据库
        int cnt = userDao.insertUser(user);
        if (cnt > 0) {
            return ServerResponse.createSuccessResponseByMsg("注册成功");
        }
        return ServerResponse.createFailResponseByMsg("注册失败");
    }

    @Override
    public ServerResponse<User> login(String username, String password) {
        //校验用户名和加密后的密码
        User user = userDao.selectUserByUsernameAndPassword(username, MD5Utils.encrypt(password));
        //如果登录成功
        if (user != null) {
            return ServerResponse.createSuccessResponseByMsgAndData("登陆成功", user);
        }
        return ServerResponse.createFailResponseByMsg("用户名或密码错误");
    }

    @Override
    public ServerResponse<String> getQuestion(String username) {
        String question = userDao.selectQuestionByUsername(username);
        //如果该用户没有设置问题
        if (StringUtils.isBlank(question)) {
            return ServerResponse.createFailResponseByMsg("用户没有设置问题");
        }
        //成功
        return ServerResponse.createSuccessResponseByMsgAndData("获取成功", question);
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {

        //校验问题的答案
        if (userDao.checkQuestionAndAnswer(username, question, answer) == 0) {
            return ServerResponse.createFailResponseByMsg("问题或答案错误");
        }
        //生成修改密码的Token
        String uuid = UUID.randomUUID().toString();
        //把Token加入本地缓存
        TokenCache.put(username, uuid);

        //把Token放入响应消息对象,并返回
        return ServerResponse.createSuccessResponseByMsgAndData("校验成功", uuid);
    }

    @Override
    public ServerResponse<String> resetPassword(String username, String newPassword, String resetToken) {
        //获取本地缓存该用户的Token
        String token = TokenCache.get(username);
        //对比Token
        if (!StringUtils.equals(token, resetToken)) {
            return ServerResponse.createFailResponseByMsg("token无效");
        }
        //更新密码
        int cnt = userDao.updatePasswordByUsername(username, MD5Utils.encrypt(newPassword));

        if (cnt > 0) {
            return ServerResponse.createSuccessResponseByMsg("密码重置成功");
        }
        return ServerResponse.createFailResponseByMsg("密码重置失败");
    }

    @Override
    public ServerResponse<String> updatePassword(Integer userId, String oldPassword, String newPassword) {
        //查询原密码是否正确
        int cnt = userDao.checkPasswordByUserId(userId, MD5Utils.encrypt(oldPassword));
        //不正确
        if (cnt == 0) {
            return ServerResponse.createFailResponseByMsg("原密码错误");
        }
        //正确，加密密码并更新
        cnt = userDao.updatePasswordByUserId(userId, MD5Utils.encrypt(newPassword));
        if (cnt == 0) {
            return ServerResponse.createFailResponseByMsg("更新密码失败");
        }
        return ServerResponse.createSuccessResponseByMsg("更新密码成功");
    }

    @Override
    public ServerResponse<User> updateUserInfo(User user) {

        //更新用户信息
        int cnt = userDao.updateUserSelective(user);
        //如果更新成功，将新的用户信息放入响应消息对象并返回
        if (cnt > 0) {
            return ServerResponse.createSuccessResponseByMsgAndData("更新成功", userDao.selectUserByPrimaryKey(user.getId()));
        }
        return ServerResponse.createSuccessResponseByMsg("更新失败");
    }
}
