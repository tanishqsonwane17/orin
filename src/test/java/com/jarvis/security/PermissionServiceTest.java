package com.jarvis.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PermissionServiceTest {

    private final PermissionService permissionService = new PermissionService();

    @Test
    void safeToolsCanExecute() {
        assertThat(permissionService.canExecute(PermissionLevel.SAFE)).isTrue();
    }

    @Test
    void confirmAndRestrictedToolsDoNotExecuteInMvp() {
        assertThat(permissionService.canExecute(PermissionLevel.CONFIRM)).isFalse();
        assertThat(permissionService.canExecute(PermissionLevel.RESTRICTED)).isFalse();
    }
}
