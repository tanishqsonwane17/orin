package com.jarvis.memory;

import java.time.Instant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommandHistoryService {

    private final JdbcTemplate jdbcTemplate;

    public CommandHistoryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void record(String command, String response, String status) {
        jdbcTemplate.update("""
                insert into command_history(command, response, created_at, status)
                values(?, ?, ?, ?)
                """, command, response, Instant.now().toString(), status);
    }
}
