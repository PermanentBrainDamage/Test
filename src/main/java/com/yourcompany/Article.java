package com.yourcompany;

public class Article implements Comparable<Article> {
    private int id;
    private String title;
    private String abstractText;
    private int cs;
    private int physics;
    private int math;
    private int stats;
    private int finance;

    // Constructs a new Article object
    public Article(int id, String title, String abstractText, int cs, int physics, int math, int stats, int finance) {
        this.id = id;
        this.title = title;
        this.abstractText = abstractText;
        this.cs = cs;
        this.physics = physics;
        this.math = math;
        this.stats = stats;
        this.finance = finance;
    }

    // Returns the ID of the article
    public int getId() {
        return id;
    }

    // Getter methods for all fields
    public String getTitle() { return title; }
    public String getAbstractText() { return abstractText; }
    public int getCs() { return cs; }
    public int getPhysics() { return physics; }
    public int getMath() { return math; }
    public int getStats() { return stats; }
    public int getFinance() { return finance; }

    // Compares this article to another based on its ID
    @Override
    public int compareTo(Article other) {
        return Integer.compare(this.id, other.id);
    }

    // Provides a string representation of the article
    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", abstractText='" + abstractText.substring(0, Math.min(20, abstractText.length())) + "...'" + // Truncating for cleaner output
                ", cs=" + cs +
                ", physics=" + physics +
                ", math=" + math +
                ", stats=" + stats +
                ", finance=" + finance +
                '}';
    }
}