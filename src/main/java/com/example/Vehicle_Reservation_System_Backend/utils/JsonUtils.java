package com.example.Vehicle_Reservation_System_Backend.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtils {

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
        String pattern = "\""+ key +"\"\\s*:\\s*\"(.*?)\"";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(json);
        return matcher.find() ? matcher.group(1) : null;
    }
}
