package com.example.payment.config;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/payment.db";
    private static final String SCHEMA_PATH = "src/main/resources/schema.sql";

    @PostConstruct
    public void initializeDatabase() {
        try {
            // 检查数据库文件是否存在
            File dbFile = new File("src/main/resources/payment.db");
            if (!dbFile.exists()) {
                logger.info("Database file not found. Creating new database...");
                Files.createDirectories(Paths.get("src/main/resources"));
                dbFile.createNewFile();

                // 初始化表
                initializeSchema();
            } else {
                logger.info("Database file found. Skipping initialization.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create database file", e);
        }
    }

    private void initializeSchema() {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            logger.info("Initializing database schema...");
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.setLogWriter(null); // 关闭日志输出

            // 加载 schema.sql 脚本
            try (Reader reader = new BufferedReader(new FileReader(SCHEMA_PATH))) {
                scriptRunner.runScript(reader);
            }

            logger.info("Database schema initialized successfully.");
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }
}
