package com.example.payment.controller;

import com.example.payment.service.AuthService;
import com.example.payment.util.RSAUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    /**
     * 处理注册请求
     * @param encryptedFormData 用户注册信息
     * @return 注册结果
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> encryptedFormData) {
        Map<String, String> decryptedFormData = new HashMap<>();

        try {
            // 解密每个字段
            for (Map.Entry<String, String> entry : encryptedFormData.entrySet()) {
                String key = entry.getKey();
                String encryptedValue = entry.getValue();
                String decryptedValue = RSAUtil.decrypt(encryptedValue);
                decryptedFormData.put(key, decryptedValue);
            }

            int status = authService.register(decryptedFormData);
            switch (status) {
                case 0:
                    return ResponseEntity.ok("Registration successful!");
                case 1:
                    return ResponseEntity.status(202).body("Email already exists");
                case 2:
                    return ResponseEntity.status(202).body("Phone number already exists");
                case 3:
                    return ResponseEntity.status(202).body("SSN already exists");
                default:
                    return ResponseEntity.badRequest().body("Registration failed with unknown error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Decryption failed: " + e.getMessage());
        }
    }

    /**
     * 处理登录请求
     * @param encryptedFormData 用户注册信息
     * @return 注册结果
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> encryptedFormData) {
        Map<String, String> decryptedFormData = new HashMap<>();

        try {
            // 解密每个字段
            for (Map.Entry<String, String> entry : encryptedFormData.entrySet()) {
                String key = entry.getKey();
                String encryptedValue = entry.getValue();
                String decryptedValue = RSAUtil.decrypt(encryptedValue);
                decryptedFormData.put(key, decryptedValue);
            }

            int status = authService.login(decryptedFormData);
            switch (status) {
                case 0:
                    return ResponseEntity.ok("Login successful!");
                case 1:
                    return ResponseEntity.status(202).body("User doesn't exist");
                case 2:
                    return ResponseEntity.status(202).body("Wrong password");
                default:
                    return ResponseEntity.badRequest().body("Registration failed with unknown error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Decryption failed: " + e.getMessage());
        }
    }

    /**
     * 退出登陆
     * @return 200
     */
    @CrossOrigin(origins =  "*")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody String userName) {
        userName = userName.substring(1, userName.length() - 1);
        return ResponseEntity.ok("Logout successful!");
    }

    @CrossOrigin(origins =  "*")
    @PostMapping("/verifyCardStatus")
    public ResponseEntity<?> verifyCardStatus(@RequestBody String cardNumber) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> requestMap = objectMapper.readValue(cardNumber, Map.class);
            String Number = requestMap.get("cardNum");
            boolean status = authService.verifyAccount(cardNumber);
            if (status) {
                return ResponseEntity.ok("Card verified");
            }
            else {
                return ResponseEntity.status(201).body("No such card exists");
            }
        } catch (Exception e) {
            return ResponseEntity.status(201).body("No such card exists");
        }
    }
}
