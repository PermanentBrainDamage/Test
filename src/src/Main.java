package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String csvFile = "src/Article.csv";

        ArrayList<Article> articleArray = new ArrayList<>();
        LinkedList<Article> articleLinked = new LinkedList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.out.println("CSV file is empty.");
                return;
            }

            System.out.println("CSV Header: " + headerLine);
            String line;
            int lineNumber = 1;
            int skippedRows = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] data = parseCSVLine(line);

                if (data.length >= 8) {
                    try {
                        int id = parseIntSafely(data[0], -1);
                        if (id < 0) {
                            skippedRows++;
                            continue;
                        }

                        String title = cleanQuotedField(data[1]);
                        String abstractText = cleanQuotedField(data[2]);
                        
                        int cs = parseIntSafely(data[3], 0);
                        int physics = parseIntSafely(data[4], 0);
                        int math = parseIntSafely(data[5], 0);
                        int stats = parseIntSafely(data[6], 0);
                        int finance = parseIntSafely(data[7], 0);

                        Article article = new Article(id, title, abstractText, cs, physics, math, stats, finance);
                        articleArray.add(article);
                        articleLinked.add(article);

                    } catch (Exception e) {
                        skippedRows++;
                    }
                } else {
                    skippedRows++;
                }
            }
            System.out.println("Total rows processed: " + (lineNumber - 1));
            System.out.println("Rows skipped: " + skippedRows);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        System.out.println("Loaded " + articleArray.size() + " articles successfully!");
        System.out.println("Sample articles:");
        for (int i = 0; i < Math.min(5, articleArray.size()); i++) {
            System.out.println((i + 1) + ". " + articleArray.get(i));
        }

        if (articleArray.size() > 0) {
            // Sort lists once
            Collections.sort(articleArray);
            Collections.sort(articleLinked);
            
            System.out.println("\n=== PERFORMANCE TEST (ArrayList) ===");
            runConcurrentTestsWithStats(articleArray);

            System.out.println("\n=== PERFORMANCE TEST (LinkedList) ===");
            runConcurrentTestsWithStats(articleLinked);
        } else {
            System.out.println("\nNo valid articles loaded.");
        }
    }

    private static void runConcurrentTestsWithStats(List<Article> list) {
        
        SearchAlgorithm<Article> linearSearch = new LinearSearch<>();
        SearchAlgorithm<Article> binarySearch = new BinarySearch<>();
        SearchAlgorithm<Article> jumpSearch = new JumpSearch<>();
        SearchAlgorithm<Article> interpolationSearch = new InterpolationSearch<>();
        
        // Corrected header formatting to ensure alignment
        System.out.printf("%-20s | %13s | %10s | %10s | %18s | %8s | %8s%n", 
                          "Algorithm", "Avg Time (ns)", "Best (ns)", "Worst (ns)", "Avg Comparisons", "Best", "Worst");
        System.out.println("----------------------------------------------------------------------------------------------------");
        
        testAlgorithm("Linear Search", linearSearch, list);
        testAlgorithm("Binary Search", binarySearch, list);
        testAlgorithm("Jump Search", jumpSearch, list);
        testAlgorithm("Interpolation Search", interpolationSearch, list);
    }

    private static void testAlgorithm(String algorithmName, SearchAlgorithm<Article> algorithm, List<Article> list) {
        Random random = new Random();
        int numTests = 30;
        List<SearchTask> tasks = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        
        for (int i = 0; i < numTests; i++) {
            Article target;
            if (random.nextDouble() < 0.7 && !list.isEmpty()) {
                target = list.get(random.nextInt(list.size()));
            } else {
                target = new Article(-999, "Non-existing", "Abstract", 0, 0, 0, 0, 0);
            }
            SearchTask task = new SearchTask(algorithm, list, target);
            tasks.add(task);
            threads.add(new Thread(task));
        }
        
        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }
        
        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted, stopping.");
                return;
            }
        }
        
        // Collect and calculate statistics
        long totalTime = 0;
        int totalComparisons = 0;
        long bestTime = Long.MAX_VALUE;
        long worstTime = Long.MIN_VALUE;
        int bestComparisons = Integer.MAX_VALUE;
        int worstComparisons = Integer.MIN_VALUE;
        
        for (SearchTask task : tasks) {
            SearchResult result = task.getResult();
            totalTime += result.getTimeNanos();
            totalComparisons += result.getComparisons();
            bestTime = Math.min(bestTime, result.getTimeNanos());
            worstTime = Math.max(worstTime, result.getTimeNanos());
            bestComparisons = Math.min(bestComparisons, result.getComparisons());
            worstComparisons = Math.max(worstComparisons, result.getComparisons());
        }
        
        double avgTimeNs = (double) totalTime / numTests;
        double avgComparisons = (double) totalComparisons / numTests;

        // Corrected output formatting to ensure alignment
        System.out.printf("%-20s | %13.2f | %10d | %10d | %18.2f | %8d | %8d%n",
                          algorithmName,
                          avgTimeNs,
                          bestTime,
                          worstTime,
                          avgComparisons,
                          bestComparisons,
                          worstComparisons);
    }
    
    private static int parseIntSafely(String str, int defaultValue) {
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
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

    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
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
}