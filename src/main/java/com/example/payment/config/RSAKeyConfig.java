package com.example.payment.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class RSAKeyConfig {

    private static String privateKey;

    @PostConstruct
    public void init() {
        try {
            // 读取 src/main/resources/rsapk.txt 文件
            privateKey = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("rsapk.txt").toURI())));
            System.out.println("Private key initialized successfully.");
        } catch (IOException | NullPointerException | URISyntaxException e) {
            throw new RuntimeException("Failed to load RSA private key from rsapk.txt", e);
        }
    }

    public static String getPrivateKey() {
        return privateKey;
    }
}
