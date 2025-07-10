package edu.upc.gessi.glidegamificationengine.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import edu.upc.gessi.glidegamificationengine.dto.SimpleRuleDTO;
import edu.upc.gessi.glidegamificationengine.exception.ConstraintViolationException;
import edu.upc.gessi.glidegamificationengine.service.OpenAPIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAPIServiceImpl implements OpenAPIService {

    @Value("${open.api.url}")
    private String openaiApiUrl;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private final WebClient webClient = WebClient.builder().baseUrl(openaiApiUrl).build();

    @Override
    public SimpleRuleDTO getSuggestedRuleTitleMessage(String evaluableActionDescription, String condition, List<Float> conditionParameters){
        Map<String,Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");

        Map<String,String> userMessage1 = new HashMap<>();
        userMessage1.put("role", "user");
        userMessage1.put("content", "Generate a title and message for the rule with the following inputs, passing the rule is positive");

        Map<String, String> userMessage2 = new HashMap<>();
        userMessage2.put("role", "user");
        userMessage2.put("content", String.format("Inputs: {%s, %s, %s}", evaluableActionDescription, condition, conditionParameters));

        requestBody.put("messages", new Map[] {userMessage1, userMessage2});

        Map<String, Object> responseFormat = new HashMap<>();
        responseFormat.put("type", "json_schema");

        Map<String, Object> jsonSchema = new HashMap<>();
        jsonSchema.put("name", "rule_response");

        Map<String, Object> schema = new HashMap<>();
        schema.put("type", "object");

        Map<String, Object> properties = new HashMap<>();
        properties.put("title", Map.of("type", "string"));
        properties.put("message", Map.of("type", "string"));

        schema.put("properties", properties);
        schema.put("required", new String[] {"title", "message"});
        schema.put("additionalProperties", false);

        jsonSchema.put("schema", schema);
        jsonSchema.put("strict", true);

        responseFormat.put("json_schema", jsonSchema);
        requestBody.put("response_format", responseFormat);

        ObjectMapper objectMapper = new ObjectMapper();

        String response;
        try {
            response = webClient.post()
                    .uri(openaiApiUrl)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + openaiApiKey)
                    .bodyValue(objectMapper.writeValueAsString(requestBody))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }
        catch (Exception e){
            throw new ConstraintViolationException("Error while requesting information.");
        }

        String content = JsonPath.read(response, "$.choices[0].message.content");

        JacksonJsonParser jacksonParser = new JacksonJsonParser();
        Map<String,Object> jsonList = jacksonParser.parseMap(content);

        SimpleRuleDTO simpleRuleDTO = new SimpleRuleDTO();
        simpleRuleDTO.setName(jsonList.get("title").toString());
        simpleRuleDTO.setAchievementAssignmentMessage(jsonList.get("message").toString());

        return simpleRuleDTO;
    }
}
