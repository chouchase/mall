package com.chouchase.dao;

import com.chouchase.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface UserDao {
    //查询用户名是否存在
    public int checkUsername(String username);
    //查询邮箱是否存在
    public int checkEmail(String Email);
    //查询手机号码是否存在
    public int checkPhone(String Phone);
    //查询邮箱是否被其他用户占用
    public int checkEmailByUserId(@Param("email") String Email, @Param("id") Integer id);
    //查询手机号码是否被其他用户占用
    public int checkPhoneByUserId(@Param("phone") String Phone, @Param("id") Integer id);
    //插入新用户
    public int insertUser(User user);
    //根据用户名和密码获取用户
    public User selectUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
    //根据用户名获取问题
    public String selectQuestionByUsername(String username);
    //根据用户Id获取用户
    public User selectUserByPrimaryKey(Integer id);
    //校验用户名、密码和答案
    public int checkQuestionAndAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);
    //通过用户名更新密码
    public int updatePasswordByUsername(@Param("username") String username, @Param("password") String password);
    //校验用户原密码
    public int checkPasswordByUserId(@Param("id") Integer id, @Param("password") String password);
    //通过用户Id更新密码
    public int updatePasswordByUserId(@Param("id") Integer id, @Param("password") String password);
    //选择性更新用户信息
    public int updateUserSelective(User user);
}
