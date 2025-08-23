package src;

import java.util.List;

class SearchTask implements Runnable {
    private final SearchAlgorithm<Article> algorithm;
    private final List<Article> list;
    private final Article target;
    private SearchResult result;

    public SearchTask(SearchAlgorithm<Article> algorithm, List<Article> list, Article target) {
        this.algorithm = algorithm;
        this.list = list;
        this.target = target;
    }

    @Override
    public void run() {
        this.result = algorithm.search(list, target);
    }

    public SearchResult getResult() {
        return result;
    }
}