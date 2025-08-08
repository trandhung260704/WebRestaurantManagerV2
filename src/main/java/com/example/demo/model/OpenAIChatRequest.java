package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIChatRequest {
    private String model = "gpt-3.5-turbo";
    private List<OpenAIMessage> messages;
}
