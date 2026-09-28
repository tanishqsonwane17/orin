package com.jarvis.tools;

import static org.assertj.core.api.Assertions.assertThat;

import com.jarvis.security.PermissionLevel;
import com.jarvis.security.PermissionService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ToolRegistryTest {

    @Test
    void executesRegisteredSafeTool() {
        ToolRegistry registry = new ToolRegistry(List.of(new TestTool(PermissionLevel.SAFE)), new PermissionService());

        ToolExecutionResult result = registry.execute("test", Map.of());

        assertThat(result.success()).isTrue();
        assertThat(result.message()).isEqualTo("done");
    }

    @Test
    void blocksToolsThatNeedConfirmation() {
        ToolRegistry registry = new ToolRegistry(List.of(new TestTool(PermissionLevel.CONFIRM)), new PermissionService());

        ToolExecutionResult result = registry.execute("test", Map.of());

        assertThat(result.success()).isFalse();
        assertThat(result.message()).contains("requires confirmation");
    }

    private record TestTool(PermissionLevel permissionLevel) implements JarvisTool {

        @Override
        public String name() {
            return "test";
        }

        @Override
        public String description() {
            return "test tool";
        }

        @Override
        public ToolExecutionResult execute(Map<String, String> parameters) {
            return ToolExecutionResult.success("done");
        }
    }
}
