package com.example.payment.controller;

import com.example.payment.service.AuthService;
import com.example.payment.util.RSAUtil;
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

}
