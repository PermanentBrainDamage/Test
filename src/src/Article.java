package src;

import java.util.Objects;

// Article class to represent each research article
class Article implements Comparable<Article> {
    private int id;
    private String title;
    private String abstractText;
    private int computerScience;
    private int physics;
    private int mathematics;
    private int statistics;
    private int quantitativeFinance;

    public Article(int id, String title, String abstractText, int cs, int physics, int math, int stats, int finance) {
        this.id = id;
        this.title = title;
        this.abstractText = abstractText;
        this.computerScience = cs;
        this.physics = physics;
        this.mathematics = math;
        this.statistics = stats;
        this.quantitativeFinance = finance;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAbstractText() { return abstractText; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Article article = (Article) obj;
        return id == article.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Article other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Article{id=" + id + ", title='" + title + "'}";
    }
}