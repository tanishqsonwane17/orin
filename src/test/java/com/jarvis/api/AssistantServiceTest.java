package com.jarvis.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AssistantServiceTest {

    private final AssistantService assistantService = new AssistantService();

    @Test
    void returnsStructuredPlaceholderResponseWithoutExecutingActions() {
        AssistantMessageResponse response = assistantService.respondTo("Hello Orin");

        assertThat(response.id()).isNotBlank();
        assertThat(response.status()).isEqualTo("received");
        assertThat(response.message()).contains("Hello Orin");
        assertThat(response.timestamp()).isNotNull();
    }

    @Test
    void reportsHealthyService() {
        AssistantHealthResponse response = assistantService.health();

        assertThat(response.status()).isEqualTo("online");
        assertThat(response.service()).isEqualTo("orin-assistant");
    }
}
