package com.demo.springmcpclient;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SpringMcpClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMcpClientApplication.class, args);
    }


    @Bean
    public CommandLineRunner predefinedQuestions(List<McpSyncClient> mcpSyncClients,
                                                 ConfigurableApplicationContext context) {

        return args -> {
            McpSyncClient mcpSyncClient = mcpSyncClients.get(0);
            McpSchema.ListToolsResult listToolsResult = mcpSyncClient.listTools();
            System.out.println(listToolsResult);

            McpSchema.CallToolResult callToolResult = mcpSyncClient.callTool(new McpSchema.CallToolRequest("getWeather", Map.of("city", "北京")));
            System.out.println(callToolResult);
            context.close();
        };
    }
}
