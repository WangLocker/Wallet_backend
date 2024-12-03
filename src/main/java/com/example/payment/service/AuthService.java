package com.example.payment.service;

import com.example.payment.entity.User;
import com.example.payment.mapper.UserMapper;
import com.example.payment.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UserMapper userMapper;

    public AuthService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 处理用户登录逻辑
     *
     * @param userInfo 用户注册信息（包含email,password 字段）
     * @return 登录成功返回用户信息，失败返回 null
     */
    public int login(Map<String, String> userInfo) {
        String email = userInfo.get("account");
        String password = userInfo.get("password");

        // 检查邮箱是否已存在
        User user = userMapper.getUserByEmail(email);
        if (user == null) {
            return 1; // 用户不存在
        }else{
            boolean right=PasswordUtil.verifyPassword(password,user.getPassword());
            if(!right){
                return 2; //密码错误
            }else{
                return 0;
            }
        }
    }

    /**
     * 处理用户注册逻辑
     *
     * @param userInfo 用户注册信息（包含 name, email, phone, password 等字段）
     * @return 注册成功返回 true，失败返回 false
     */
    public int register(Map<String, String> userInfo) {
        // 从 Map 中提取字段
        String name = userInfo.get("name");
        String email = userInfo.get("email");
        String phone = userInfo.get("phone");
        String password = userInfo.get("password");
        String ssn = userInfo.get("ssn");

        // 检查邮箱是否已存在
        if (userMapper.getUserByEmail(email) != null) {
            return 1; // 邮箱已被注册
        }
        // 检查电话号码是否已存在
        if (userMapper.getUserByPhone(phone) != null) {
            return 2; // 电话号码已被注册
        }
        if (userMapper.getUserBySsn(ssn) != null) {
            return 3; // ssn
        }

        // 插入用户数据到数据库
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setSsn(ssn);

        // 加密密码（确保安全）
        String hashedPassword = PasswordUtil.hashPassword(password);
        user.setPassword(hashedPassword);

        // 插入用户到数据库
        userMapper.insertUser(user);

        return 0;
    }

}
