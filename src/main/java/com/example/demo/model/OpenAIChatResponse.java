package com.example.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class OpenAIChatResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private OpenAIMessage message;
    }
}
