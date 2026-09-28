package com.jarvis.os;

import com.jarvis.workspace.WorkspaceApplication;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WindowsApplicationService {

    private final JdbcTemplate jdbcTemplate;

    public WindowsApplicationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Application> findEnabledByName(String name) {
        String key = name.toLowerCase().replace(" ", "").trim();
        List<Application> applications = jdbcTemplate.query("""
                select id, name, display_name, command, arguments, process_name, enabled
                from applications
                where enabled = 1 and (lower(name) = ? or lower(id) = ?)
                """, this::mapApplication, key, key);
        return applications.stream().findFirst();
    }

    public List<WorkspaceApplication> findByWorkspace(String workspaceId) {
        return jdbcTemplate.query("""
                select a.id, a.name, a.display_name, a.command, a.arguments, a.process_name, a.enabled
                from applications a
                join workspace_applications wa on wa.application_id = a.id
                where wa.workspace_id = ? and a.enabled = 1
                order by wa.sort_order
                """, (rs, rowNum) -> new WorkspaceApplication(mapApplication(rs, rowNum)), workspaceId);
    }

    private Application mapApplication(ResultSet rs, int rowNum) throws SQLException {
        return new Application(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("display_name"),
                rs.getString("command"),
                rs.getString("arguments"),
                rs.getString("process_name"),
                rs.getInt("enabled") == 1
        );
    }
}
