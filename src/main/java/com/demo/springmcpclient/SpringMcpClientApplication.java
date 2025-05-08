package com.demo.springmcpclient;

import com.google.gson.Gson;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.mcp.SyncMcpToolCallback;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.openai.OpenAiChatOptions;
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


//    @Bean
//    public CommandLineRunner predefinedQuestions(List<McpSyncClient> mcpSyncClients,
//                                                 ConfigurableApplicationContext context) {
//
//        return args -> {
//            McpSyncClient mcpSyncClient = mcpSyncClients.get(0);
//            McpSchema.ListToolsResult listToolsResult = mcpSyncClient.listTools();
//            System.out.println(listToolsResult);
//
//            McpSchema.CallToolResult callToolResult = mcpSyncClient.callTool(new McpSchema.CallToolRequest("getWeather", Map.of("city", "北京")));
//            System.out.println(callToolResult);
//            context.close();
//        };
//    }



    @Bean
    public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder,
                                                 ToolCallbackProvider toolCallbackProvider,
                                                 ConfigurableApplicationContext context) {

        return args -> {
            // 获取工具列表
            Gson gson = new Gson();
            System.out.println(toolCallbackProvider);
            // 用户输出
            var chatClient = chatClientBuilder
                    .defaultTools(toolCallbackProvider)
                    .build();
            String userInput = "北京今天天气怎么样";
            System.out.println("用户问: " + userInput);
            String content = chatClient.prompt(userInput).call().content();
            System.out.println("AI回答: " + gson.toJson(content));


            context.close();
        };
    }
}
