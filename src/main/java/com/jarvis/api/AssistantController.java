package com.jarvis.api;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistant")
public class AssistantController {

    private final AssistantService assistantService;

    public AssistantController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @GetMapping("/health")
    public AssistantHealthResponse health() {
        return assistantService.health();
    }

    @PostMapping("/message")
    public ResponseEntity<?> message(@RequestBody(required = false) AssistantMessageRequest request) {
        if (request == null || request.message() == null || request.message().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "A non-empty message is required."));
        }

        return ResponseEntity.ok(assistantService.respondTo(request.message().trim()));
    }
}
