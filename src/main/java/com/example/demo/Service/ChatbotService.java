package com.example.demo.Service;

import com.example.demo.config.OpenAIConfig;
import com.example.demo.entity.Food;
import com.example.demo.entity.Ingredient;
import com.example.demo.entity.IngredientBill;
import com.example.demo.entity.RestaurantInfo;
import com.example.demo.model.*;
import com.example.demo.repository.FoodRepository;
import com.example.demo.repository.IngredientBillRepository;
import com.example.demo.repository.IngredientRepository;
import com.example.demo.repository.RestaurantInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final FoodRepository foodRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientBillRepository ingredientBillRepository;
    private final RestaurantInfoRepository restaurantInfoRepository;
    private final OpenAIConfig openAIConfig;

    @Value("${openai.api.url}")
    private String openAiApiUrl;

    private final Map<String, List<OpenAIMessage>> conversationMemory = new HashMap<>();

    public String chat(String sessionId, String message) {

        List<Food> foods = foodRepository.findAll();
        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<IngredientBill> bills = ingredientBillRepository.findAll();
        List<RestaurantInfo> storeInfos = restaurantInfoRepository.findAll();

        String foodData = foods.stream()
                .map(f -> String.format("ID: %d, Name: %s, Price: %s, Category: %s, Status: %s",
                        f.getId_food(), f.getName(), f.getPrice(), f.getCategory(), f.getStatus()))
                .collect(Collectors.joining("\n"));

        String ingredientData = ingredients.stream()
                .map(i -> String.format("ID: %d, Name: %s, Quantity: %s %s, UnitPrice: %s, Origin: %s, CreatedAt: %s",
                        i.getId_ingredient(), i.getName(), i.getQuantity(), i.getUnit(),
                        i.getUnit_price(), i.getOrigin(), i.getCreated_at()))
                .collect(Collectors.joining("\n"));

        String billData = bills.stream()
                .map(b -> String.format("BillID: %d, Ingredient: %s, Quantity: %s %s, UnitPrice: %s, ImportTime: %s, Note: %s",
                        b.getId_ingredient_bill(),
                        b.getIngredient() != null ? b.getIngredient().getName() : "N/A",
                        b.getQuantity(),
                        b.getIngredient() != null ? b.getIngredient().getUnit() : "",
                        b.getUnit_price(), b.getImport_time(), b.getNote()))
                .collect(Collectors.joining("\n"));

        String restaurantData = storeInfos.stream()
                .map(r -> String.format(
                        "Name: %s, Address: %s, Phone: %s, Email: %s, Facebook: %s, OpeningHours: %s, Location: (%s, %s)",
                        r.getName(), r.getAddress(), r.getPhone(), r.getEmail(), r.getFacebook(),
                        r.getOpeningHours(), r.getLatitude(), r.getLongitude()
                ))
                .collect(Collectors.joining("\n"));

        conversationMemory.putIfAbsent(sessionId, new ArrayList<>());
        List<OpenAIMessage> messages = conversationMemory.get(sessionId);

        if (messages.isEmpty()) {
            try {
                String storeInfoTxt = Files.readString(Path.of("src/main/resources/store-info.txt"));
                messages.add(new OpenAIMessage("system", storeInfoTxt));
            } catch (IOException e) {
                messages.add(new OpenAIMessage("system", "Bạn là trợ lý AI của cửa hàng."));
            }
        }

        String prompt =
                "Dưới đây là dữ liệu cửa hàng:\n\n" +
                        "=== RESTAURANT INFO ===\n" + restaurantData + "\n\n" +
                        "=== FOOD ===\n" + foodData + "\n\n" +
                        "=== INGREDIENT ===\n" + ingredientData + "\n\n" +
                        "=== INGREDIENT BILL ===\n" + billData + "\n\n" +
                        "Người dùng hỏi: " + message + "\n" +
                        "Hãy trả lời dựa trên dữ liệu trên.";

        messages.add(new OpenAIMessage("user", prompt));

        int maxMemoryMessages = 1 + 2 * 2;
        if (messages.size() > maxMemoryMessages) {
            List<OpenAIMessage> trimmed = new ArrayList<>();
            trimmed.add(messages.get(0));
            trimmed.addAll(messages.subList(messages.size() - (maxMemoryMessages - 1), messages.size()));
            messages.clear();
            messages.addAll(trimmed);
        }

        OpenAIChatRequest chatRequest = new OpenAIChatRequest();
        chatRequest.setModel("gpt-3.5-turbo");
        chatRequest.setMessages(messages);

        HttpEntity<OpenAIChatRequest> entity =
                new HttpEntity<>(chatRequest, openAIConfig.openAIHeaders());

        RestTemplate restTemplate = new RestTemplate();
        OpenAIChatResponse chatResponse = restTemplate.postForObject(
                openAiApiUrl,
                entity,
                OpenAIChatResponse.class
        );

        if (chatResponse != null &&
                chatResponse.getChoices() != null &&
                !chatResponse.getChoices().isEmpty()) {

            String reply = chatResponse.getChoices().get(0).getMessage().getContent();
            messages.add(new OpenAIMessage("assistant", reply));
            return reply;
        }
        return "Không nhận được phản hồi từ AI.";
    }

    private Map<String, Object> getIntentFromRasa(String text) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> request = Map.of("text", text);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://localhost:5005/model/parse",
                request,
                Map.class
        );
        return response.getBody();
    }

    private String cleanText(String input) {
        return input.replaceAll("[^\\p{L}\\p{N}\\s]", "").trim().toLowerCase();
    }
}
