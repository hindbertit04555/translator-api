package com.example;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class GeminiService {

    private static final String API_KEY;
    private static final String GEMINI_URL;

    static {
        // First try environment variable (for cloud deployment)
        String key = System.getenv("GEMINI_API_KEY");

        // If not found, fall back to config.properties (for local)
        if (key == null || key.isBlank()) {
            try {
                Properties props = new Properties();
                InputStream is = GeminiService.class
                        .getClassLoader()
                        .getResourceAsStream("config.properties");
                if (is != null) {
                    props.load(is);
                    key = props.getProperty("gemini.api.key", "");
                }
            } catch (Exception e) {
                key = "";
            }
        }

        API_KEY = key != null ? key : "";
        GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + API_KEY;
    }

    public static String translate(String text) {
        try {
            String prompt = "Translate the following text to Moroccan Arabic Darija. " +
                    "Return ONLY the translation, nothing else: " + text;

            String requestBody = "{"
                    + "\"contents\": [{"
                    + "\"parts\": [{\"text\": \"" + prompt.replace("\"", "\\\"") + "\"}]"
                    + "}]}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GEMINI_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );

            String body = response.body();

            int start = body.indexOf("\"text\": \"");
            if (start == -1) {
                start = body.indexOf("\"text\":\"");
                if (start == -1) return "Error: unexpected response from Gemini";
                start += 8;
            } else {
                start += 9;
            }
            int end = start;
            while (end < body.length()) {
                char c = body.charAt(end);
                if (c == '\\') { end += 2; continue; }
                if (c == '"') break;
                end++;
            }
            return body.substring(start, end).trim();

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}