package com.example.payment.service;

import com.example.payment.entity.Account;
import com.example.payment.entity.User;
import com.example.payment.entity.Phone;
import com.example.payment.entity.Email;
import com.example.payment.mapper.AccountMapper;
import com.example.payment.mapper.EmailMapper;
import com.example.payment.mapper.PhoneMapper;
import com.example.payment.mapper.UserMapper;
import com.example.payment.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final PhoneMapper phoneMapper;
    private final EmailMapper emailMapper;
    private final AccountMapper accountMapper;

    public AuthService(UserMapper userMapper, PhoneMapper phoneMapper, EmailMapper emailMapper, AccountMapper accountMapper) {
        this.userMapper = userMapper;
        this.phoneMapper = phoneMapper;
        this.emailMapper = emailMapper;
        this.accountMapper = accountMapper;
    }

    /**
     * 处理用户登录逻辑
     *
     * @param userInfo 用户注册信息（包含email,password 字段）
     * @return 登录成功返回用户信息，失败返回 null
     */
    public int login(Map<String, String> userInfo) {
        String account = userInfo.get("account");
        String password = userInfo.get("password");

        if(account != null && account.matches("\\d{11}")){
            Phone phone = phoneMapper.getPhoneByNumber(account);
            if (phone == null) {
                return 1; // 用户不存在
            }else{
                boolean right=PasswordUtil.verifyPassword(password,(userMapper.getUserById(phone.getUserId())).getPassword());
                if(!right){
                    return 2; //密码错误
                }else{
                    return 0;
                }
            }
        } else {
            Email email = emailMapper.getEmailByEmail(account);
            if (email == null) {
                return 1; // 用户不存在
            }else{
                boolean right=PasswordUtil.verifyPassword(password,(userMapper.getUserById(email.getUserId())).getPassword());
                if(!right){
                    return 2; //密码错误
                }else{
                    return 0;
                }
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

        if (emailMapper.getEmailByEmail(email) != null) {
            return 1; // 邮箱已被注册
        }
        if (phoneMapper.getPhoneByNumber(phone) != null) {
            return 2; // 电话号码已被注册
        }
        if (userMapper.getUserBySsn(ssn) != null) {
            return 3; // ssn
        }

        // 插入用户数据到数据库
        User user = new User();
        user.setName(name);
        user.setSsn(ssn);
        String hashedPassword = PasswordUtil.hashPassword(password);
        user.setPassword(hashedPassword);
        userMapper.insertUser(user);

        user.setId(userMapper.getUserByName(name).getId());

        // Insert email into the email table
        Email emailEntity = new Email();
        emailEntity.setUserId(user.getId());
        emailEntity.setEmail(email);
        emailEntity.setIsVerified(false);
        emailMapper.insertEmail(emailEntity);

        // Insert phone into the phone table
        Phone phoneEntity = new Phone();
        phoneEntity.setUserId(user.getId());
        phoneEntity.setPhone(phone);
        phoneEntity.setIsVerified(false);
        phoneMapper.insertPhone(phoneEntity);

        return 0;
    }

    public boolean verifyAccount(String accountNumber) {
        Account account = accountMapper.getAccountByAccountNumber(accountNumber);
        if (account == null) return false;
        account.setVerified(true);
        return true;
    }

}
