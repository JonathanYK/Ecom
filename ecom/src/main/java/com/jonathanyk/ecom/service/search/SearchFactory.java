package com.jonathanyk.ecom.service.search;

public class SearchFactory {
    public SearchAlgorithm getSearch(SearchAlgoName algorithmName) {

        if (algorithmName == SearchAlgoName.BINARY_SEARCH) {
            return new BinarySearchAlgorithm();
        }
        else {
            //TODO: throw new Exception("");
            throw new IllegalArgumentException("Invalid algorithm type: " + algorithmName);
        }
    }
}
