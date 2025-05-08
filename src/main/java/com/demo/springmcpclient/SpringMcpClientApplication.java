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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SpringMcpClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMcpClientApplication.class, args);
    }


    /** 测试功能
     * @param mcpSyncClients mcpClient，自动注入
     * @param context context
     * @return
     */
    @Bean
    public CommandLineRunner callTool(List<McpSyncClient> mcpSyncClients,
                                                 ConfigurableApplicationContext context) {

        return args -> {
            System.out.println("普通client调用方法------------------------------------");
            // 获取mcp-client
            McpSyncClient mcpSyncClient = mcpSyncClients.get(0);
            McpSchema.ListToolsResult listToolsResult = mcpSyncClient.listTools();
            System.out.println(listToolsResult);

            McpSchema.CallToolResult callToolResult = mcpSyncClient.callTool(new McpSchema.CallToolRequest("getWeather", Map.of("city", "北京")));
            System.out.println(callToolResult);
            // 结束会话
//            context.close();
        };
    }



    @Bean
    public CommandLineRunner callToolByLLM(ChatClient.Builder chatClientBuilder,
                                                 ToolCallbackProvider toolCallbackProvider,
                                                 ConfigurableApplicationContext context) {

        return args -> {
            System.out.println("基于spring-ai，llm调用方法--------------------------------");
            // 获取工具列表
            Gson gson = new Gson();
            for (FunctionCallback toolCallback : toolCallbackProvider.getToolCallbacks()) {
                System.out.println(toolCallback.getName() + " " + toolCallback.getDescription() + " " + toolCallback.getInputTypeSchema());
            }

            // 模拟用户输入的信息
            var chatClient = chatClientBuilder
                    .defaultTools(toolCallbackProvider)
                    .build();
            String userInput = "北京今天天气怎么样";
            System.out.println("用户问: " + userInput);

            // 包装请求LLM
            String content = chatClient.prompt(userInput).call().content();
            System.out.println("AI回答: " + gson.toJson(content));


            // 结束会话
            context.close();
        };
    }
}
