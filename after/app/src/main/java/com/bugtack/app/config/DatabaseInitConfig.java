package com.bugtack.app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Configuration
public class DatabaseInitConfig {

    @Bean
    public CommandLineRunner initDatabase(DataSource dataSource) {
        return args -> {
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {
                
                String[] tables = {
                    "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, username VARCHAR(50) UNIQUE NOT NULL, password VARCHAR(255) NOT NULL, name VARCHAR(100) NOT NULL, email VARCHAR(100) UNIQUE NOT NULL, phone VARCHAR(20), avatar VARCHAR(500), role VARCHAR(50) NOT NULL DEFAULT 'member', status VARCHAR(20) NOT NULL DEFAULT 'active', created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, last_login_at TIMESTAMP)",
                    "CREATE TABLE IF NOT EXISTS projects (id SERIAL PRIMARY KEY, name VARCHAR(200) NOT NULL, description TEXT, status VARCHAR(20) NOT NULL DEFAULT 'active', created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                    "CREATE TABLE IF NOT EXISTS project_members (id SERIAL PRIMARY KEY, project_id INTEGER NOT NULL, user_id INTEGER NOT NULL, role VARCHAR(50) NOT NULL DEFAULT 'member', joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                    "CREATE TABLE IF NOT EXISTS project_leaders (id SERIAL PRIMARY KEY, project_id INTEGER NOT NULL, user_id INTEGER NOT NULL, assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                    "CREATE TABLE IF NOT EXISTS requirements (id SERIAL PRIMARY KEY, project_id INTEGER NOT NULL, title VARCHAR(200) NOT NULL, description TEXT, priority VARCHAR(20) NOT NULL DEFAULT 'P1', status VARCHAR(20) NOT NULL DEFAULT 'active', assignee_id INTEGER, creator_id INTEGER, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                    "CREATE TABLE IF NOT EXISTS requirement_history (id SERIAL PRIMARY KEY, requirement_id INTEGER NOT NULL, action VARCHAR(50) NOT NULL, field_name VARCHAR(50), old_value TEXT, new_value TEXT, user_id INTEGER NOT NULL, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                    "CREATE TABLE IF NOT EXISTS meetings (id SERIAL PRIMARY KEY, title VARCHAR(200) NOT NULL, project_id INTEGER NOT NULL, meeting_date DATE, participants TEXT, status VARCHAR(20) DEFAULT 'pending', content TEXT, conclusion TEXT, meeting_link VARCHAR(500), attachment VARCHAR(500), related_requirement_id INTEGER, creator_id INTEGER, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                    "CREATE TABLE IF NOT EXISTS issues (id SERIAL PRIMARY KEY, project_id INTEGER NOT NULL, requirement_id INTEGER, title VARCHAR(500) NOT NULL, description TEXT, type VARCHAR(50) NOT NULL, severity VARCHAR(20) NOT NULL DEFAULT 'medium', status VARCHAR(50) NOT NULL DEFAULT 'pending', environment VARCHAR(50), version VARCHAR(50), assignee_id INTEGER, creator_id INTEGER NOT NULL, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, resolved_at TIMESTAMP, closed_at TIMESTAMP)",
                    "CREATE TABLE IF NOT EXISTS issue_history (id SERIAL PRIMARY KEY, issue_id INTEGER NOT NULL, action VARCHAR(50) NOT NULL, from_status VARCHAR(50), to_status VARCHAR(50), user_id INTEGER NOT NULL, description TEXT, environment VARCHAR(50), created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                    "CREATE TABLE IF NOT EXISTS todos (id SERIAL PRIMARY KEY, user_id INTEGER NOT NULL, issue_id INTEGER NOT NULL, priority VARCHAR(20) NOT NULL DEFAULT 'medium', deadline TIMESTAMP, status VARCHAR(20) NOT NULL DEFAULT 'pending', remark TEXT, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, completed_at TIMESTAMP)",
                    "CREATE TABLE IF NOT EXISTS testing_tasks (id SERIAL PRIMARY KEY, issue_id INTEGER NOT NULL, environment VARCHAR(50) NOT NULL, status VARCHAR(20) NOT NULL DEFAULT 'pending', queue_order INTEGER NOT NULL DEFAULT 0, submitter_id INTEGER NOT NULL, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, started_at TIMESTAMP, completed_at TIMESTAMP)",
                    "CREATE TABLE IF NOT EXISTS testing_records (id SERIAL PRIMARY KEY, task_id INTEGER NOT NULL, issue_id INTEGER NOT NULL, environment VARCHAR(50) NOT NULL, result VARCHAR(20), description TEXT, tester_id INTEGER NOT NULL, tested_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                    "CREATE TABLE IF NOT EXISTS attachments (id SERIAL PRIMARY KEY, filename VARCHAR(255) NOT NULL, original_name VARCHAR(500) NOT NULL, size BIGINT NOT NULL, type VARCHAR(50), mime_type VARCHAR(100), path VARCHAR(500) NOT NULL, url VARCHAR(500), uploader_id INTEGER NOT NULL, issue_id INTEGER, meeting_id INTEGER, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                    "CREATE TABLE IF NOT EXISTS issue_comments (id SERIAL PRIMARY KEY, issue_id INTEGER NOT NULL, user_id INTEGER NOT NULL, content TEXT NOT NULL, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                    "CREATE TABLE IF NOT EXISTS roles (id SERIAL PRIMARY KEY, name VARCHAR(100) UNIQUE NOT NULL, key VARCHAR(50) UNIQUE NOT NULL, description TEXT, permissions JSONB NOT NULL DEFAULT '[]', created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                    "CREATE TABLE IF NOT EXISTS environments (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, key VARCHAR(50) UNIQUE NOT NULL, description TEXT, sort_order INTEGER NOT NULL DEFAULT 0, is_active BOOLEAN NOT NULL DEFAULT true, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                    "CREATE TABLE IF NOT EXISTS issue_statuses (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, key VARCHAR(50) UNIQUE NOT NULL, type VARCHAR(20) NOT NULL, description TEXT, sort_order INTEGER NOT NULL DEFAULT 0, is_active BOOLEAN NOT NULL DEFAULT true, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)"
                };
                
                for (String sql : tables) {
                    try {
                        stmt.execute(sql);
                    } catch (Exception e) {
                        System.err.println("Error executing: " + sql.substring(0, Math.min(50, sql.length())) + "... " + e.getMessage());
                    }
                }
                
                System.out.println("Database tables initialized successfully");
            }
        };
    }
}
