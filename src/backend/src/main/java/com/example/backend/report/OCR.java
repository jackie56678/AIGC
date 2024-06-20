package com.example.backend.report;
import com.example.backend.chat.VivoAuth;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
@Service
public class OCR {
    public String ocrTest(MultipartFile file) {
        String APP_ID= "3033936351";
        String APP_KEY = "JCuXgJuLbtINqlYy";
        final String DOMAIN = "api-ai.vivo.com.cn";
        final String URI = "/ocr/general_recognition";
        final String METHOD = "POST";
        final String PIC_FILE = "./test.jpg";
        byte[] fileContent;
        try {
            // 加载资源目录下的文件
            InputStream inputStream = file.getInputStream();
//                    getClass().getResourceAsStream("/test.jpg");
            // 将输入流转换为字节数组
            fileContent = inputStream.readAllBytes();
            String encodedImage = new String(Base64.getEncoder().encode(fileContent), "UTF-8");
            //构建请求体
            Map<String, String> postParams = new HashMap<>();
            postParams.put("image", encodedImage);
            postParams.put("pos", "0");
            postParams.put("businessid", "1990173156ceb8a09eee80c293135279");
            //请求参数
            Map<String, String> map = new HashMap<>();
            String queryStr = ToQueryString(map);
            //构建请求头
            HttpHeaders headers = VivoAuth.generateAuthHeaders(APP_ID, APP_KEY, METHOD, URI, queryStr);
            headers.add("Content-Type", "application/x-www-form-urlencoded");
            String url = "http://" + DOMAIN + URI;
            String jsonString = ToQueryString(postParams);
            RestTemplate template = new RestTemplate();
            HttpEntity<String> httpEntity = new HttpEntity<>(jsonString, headers);
            ResponseEntity<String> response1 = template.exchange(url, HttpMethod.POST, httpEntity, String.class);
            String responseBody = response1.getBody();
            if (responseBody != null) {
//                Object StringEscapeUtils = null;
                String unescapeJava = StringEscapeUtils.unescapeJava(responseBody);
                return unescapeJava;
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static String ToQueryString(Map<String, String> map) throws UnsupportedEncodingException {
        if (map == null || map.isEmpty()) {
            return ""; // 返回空字符串
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return sb.toString();
    }
}
