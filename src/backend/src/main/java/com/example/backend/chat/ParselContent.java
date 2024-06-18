package com.example.backend.chat;

import com.example.backend.CommonResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ParselContent{
    public CommonResult<?> parsel(String body) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(body);
            JsonNode codeNode = rootNode.path("code");
            int code = codeNode.asInt();
            if(code == 0)
            {
                JsonNode contentNode = rootNode.path("data").path("content");
                String content = contentNode.asText();
                return CommonResult.success(content);
            } else if (code == 1001) {
                return CommonResult.error(1001, "参数异常，通常是缺少必填参数");
            } else if (code == 1007) {
                return CommonResult.error(1007, "触发审核后系统干预返回的内容");
            } else if (code == 2001) {
                return CommonResult.error(2001, "没有访问权限，或者权限到期，请联系官网客服");
            } else if (code == 2002) {
                return CommonResult.error(2002, "触发模型 QPS 限流，请降低请求频率");
            } else if (code == 2003) {
                return CommonResult.error(2003, "触发单日用量限制，请次日再重试");
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
