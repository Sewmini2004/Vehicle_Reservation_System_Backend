package com.example.Vehicle_Reservation_System_Backend.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtils {

    public static String convertDtoToJson(Object dto) {
        if (dto == null) {
            return "{}"; // Return empty JSON if object is null
        }

        StringBuilder json = new StringBuilder();
        json.append("{");

        Field[] fields = dto.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true); // Access private fields
            try {
                String key = fields[i].getName();
                Object value = fields[i].get(dto);

                json.append("\"").append(key).append("\":");

                if (value instanceof String) {
                    json.append("\"").append(value).append("\"");
                } else {
                    json.append(value);
                }

                if (i < fields.length - 1) {
                    json.append(",");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        json.append("}");
        return json.toString();
    }
    public static String getJsonFromRequest(HttpServletRequest request) throws IOException {
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }
        return jsonBuffer.toString();
    }
    public static String extractJsonValue(String json, String key) {
        String pattern = "\""+ key +"\"\\s*:\\s*(\"(.*?)\"|(\\d+)|true|false)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(json);

        if (matcher.find()) {
            return matcher.group(2) != null ? matcher.group(2) : matcher.group(3); // Match string or number
        }
        return null;
    }
}
