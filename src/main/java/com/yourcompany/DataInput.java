package com.yourcompany;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataInput {

    // Reads data from a CSV file and populates a list of Article objects.
    public static void readArticleFile(List<Article> articleList, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Skips the header row.
            reader.readLine();

            String line;
            StringBuilder multiLineRecord = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                multiLineRecord.append(line);

                if (line.matches(".*\"?,\\d+,\\d+,\\d+,\\d+,\\d+")) {
                    String fullRecord = multiLineRecord.toString();
                    String[] data = parseCSVLine(fullRecord);

                    if (data.length >= 8) {
                        try {
                            // Parses the data and creates a new Article object.
                            int id = Integer.parseInt(data[0].trim());
                            String title = cleanQuotedField(data[1]);
                            String abstractText = cleanQuotedField(data[2]);
                            int cs = Integer.parseInt(data[3].trim());
                            int physics = Integer.parseInt(data[4].trim());
                            int math = Integer.parseInt(data[5].trim());
                            int stats = Integer.parseInt(data[6].trim());
                            int finance = Integer.parseInt(data[7].trim());

                            articleList.add(new Article(id, title, abstractText, cs, physics, math, stats, finance));
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping invalid record due to NumberFormatException: " + fullRecord);
                        }
                    }
                    multiLineRecord.setLength(0); // Resets the buffer for the next record
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // A custom CSV parser to handle fields with commas inside quotes.
    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                // Handles escaped quotes (e.g., "").
                if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // Adds a completed field to the result list.
                result.add(field.toString());
                field = new StringBuilder();
            } else {
                field.append(c);
            }
        }
        result.add(field.toString());
        while (result.size() < 8) {
            result.add("0");
        }
        return result.toArray(new String[0]);
    }

    private static String cleanQuotedField(String field) {
        if (field == null) return "";
        field = field.trim();
        if (field.length() >= 2 && field.startsWith("\"") && field.endsWith("\"")) {
            field = field.substring(1, field.length() - 1);
            field = field.replace("\"\"", "\"");
        }
        return field;
    }
}