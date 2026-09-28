package com.jarvis.workspace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WorkspaceRepository {

    private final JdbcTemplate jdbcTemplate;

    public WorkspaceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Workspace> findEnabledByName(String name) {
        String key = name.toLowerCase().trim();
        List<Workspace> workspaces = jdbcTemplate.query("""
                select id, name, display_name, project_path, enabled
                from workspaces
                where enabled = 1 and (lower(id) = ? or lower(name) = ?)
                """, this::mapWorkspace, key, key);
        return workspaces.stream().findFirst();
    }

    public List<WorkspaceUrl> findUrls(String workspaceId) {
        return jdbcTemplate.query("""
                select url, display_name
                from workspace_urls
                where workspace_id = ?
                order by sort_order
                """, (rs, rowNum) -> new WorkspaceUrl(rs.getString("url"), rs.getString("display_name")), workspaceId);
    }

    private Workspace mapWorkspace(ResultSet rs, int rowNum) throws SQLException {
        return new Workspace(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("display_name"),
                rs.getString("project_path"),
                rs.getInt("enabled") == 1
        );
    }
}
