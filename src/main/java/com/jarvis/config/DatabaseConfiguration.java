package com.jarvis.config;

import java.nio.file.Files;
import java.nio.file.Path;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfiguration {

    @Bean
    public DataSource dataSource(@Value("${jarvis.database.path}") String databasePath) throws Exception {
        Path path = Path.of(databasePath.replace("\\", "/"));
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:" + path);
        return dataSource;
    }
}
