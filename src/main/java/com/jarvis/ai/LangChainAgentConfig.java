package com.jarvis.ai;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AIModelConfig.class)
public class LangChainAgentConfig {
}
