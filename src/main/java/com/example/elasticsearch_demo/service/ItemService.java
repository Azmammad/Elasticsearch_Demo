package com.example.elasticsearch_demo.service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.elasticsearch_demo.dto.SearchRequestDto;
import com.example.elasticsearch_demo.model.Item;

import java.util.List;
import java.util.Set;

public interface ItemService {

    Item createIndex(Item item);
    void addItemsFromJson();
    List<Item> getAllDataFromIndex(String indexName);
    List<Item> extractItemsFromResponse(SearchResponse<Item> response);
    List<Item> searchItemsByFiledAndValue(SearchRequestDto dto);
    List<Item> searchItemsByNameAndBrandWithQuery(String name, String brand);
    List<Item> boolQuery(SearchRequestDto dto);
    Set<String> findSuggestedItemNames(String name);
    List<String> autoSuggestItemByNameWithQuery(String name);
    void deleteIndex(String indexName);
    List<String> getIndexNames();

}
