package src;

import java.util.List;

//Generic search interface
interface SearchAlgorithm<T> {
    SearchResult search(List<T> list, T target);
}