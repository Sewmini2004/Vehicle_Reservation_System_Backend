package com.example.Vehicle_Reservation_System_Backend.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtils {

    // Converts a DTO object into a JSON string
    public static String convertDtoToJson(Object dto) {
        if (dto == null) {
            return "{}"; // Return empty JSON if object is null
        }

        StringBuilder json = new StringBuilder();

        // Check if DTO is a list or a single object
        if (dto instanceof List) {
            // If the object is a list, iterate through and convert each object in the list
            List<?> list = (List<?>) dto;
            json.append("[");  // Start of JSON array
            for (int i = 0; i < list.size(); i++) {
                json.append(convertDtoToJson(list.get(i))); // Convert each DTO in list to JSON
                if (i < list.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");  // End of JSON array
            return json.toString();
        }

        // If it is a single object
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
        // Regex pattern to match both string and numeric values
        String pattern = "\""+ key +"\"\\s*:\\s*(\"(.*?)\"|([\\d.]+)|true|false|null)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(json);

        if (matcher.find()) {
            return matcher.group(2) != null ? matcher.group(2) : matcher.group(3);
        }
        return null;
    }

}
