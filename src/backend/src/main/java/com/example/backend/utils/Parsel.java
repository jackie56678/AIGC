package com.example.backend.utils;

import com.example.backend.CommonResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Parsel {
    public CommonResult<?> parsel(String body) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(body);
            JsonNode codeNode = rootNode.path("error_code");
            int code = codeNode.asInt();
            if(code == 0)
            {
                JsonNode contentNode = rootNode.path("result").path("words");
                StringBuilder contentBuilder = new StringBuilder();
                if (contentNode.isArray()) {
                    for (JsonNode node : contentNode) {
                        contentBuilder.append(node.path("words").asText()).append(" ");
                    }
                }

                String content = contentBuilder.toString().trim();
//                System.out.println("报告内容"+content);
                return CommonResult.success(content);
            } else {
                return CommonResult.error(code, "未知错误");
            }
        } catch (
                JsonProcessingException e) {
            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error parsing response: " + e.getMessage());
            return CommonResult.error(4001,e.getMessage());
        }
    }
}
