package com.chouchase.service;

import com.chouchase.common.ServerResponse;
import com.chouchase.domain.User;


public interface UserService {
    public ServerResponse<String> checkUsername(String username);

    public ServerResponse<String> checkEmail(String email);

    public ServerResponse<String> checkPhone(String phone);

    public ServerResponse<String> register(User user);

    public ServerResponse<User> login(String username, String password);

    public ServerResponse<String> getQuestion(String username);

    public ServerResponse<String> checkAnswer(String username, String question, String answer);

    public ServerResponse<String> resetPassword(String username, String newPassword, String resetToken);

    public ServerResponse<String> updatePassword(Integer userId, String oldPassword, String newPassword);

    public ServerResponse<User> updateUserInfo(User user);
}
