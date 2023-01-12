/*
Создаю отдельный компонент, так как на версии IntelliJ IDEA 2022.1.2 (Ultimate Edition)
у меня не инжектится jdbcTemplate
https://intellij-support.jetbrains.com/hc/en-us/community/posts/
6186052659474-Could-not-autowire-No-beans-of-JdbcTemplate-type-found-In-Intellij-IDEA-CE-works-in-Ultimate-doesn-t
 */

package ru.yandex.practicum.filmorate.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan("ru.yandex.practicum.filmorate")
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String JDBC_URL;

    @Value("${spring.datasource.username}")
    private String JDBC_USERNAME;

    @Value("${spring.datasource.password}")
    private String JDBC_PASSWORD;

    @Value("${spring.datasource.driverClassName}")
    private String JDBC_DRIVER;

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(JDBC_USERNAME);
        config.setPassword(JDBC_PASSWORD);
        config.setDriverClassName(JDBC_DRIVER);
        return config;
    }

    @Bean
    public HikariDataSource dataSource() {
        return new HikariDataSource(hikariConfig());
    }

    @Bean
    public JdbcTemplate getTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
