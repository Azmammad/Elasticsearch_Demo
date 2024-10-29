package com.example.elasticsearch_demo.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import com.example.elasticsearch_demo.dto.SearchRequestDto;
import com.example.elasticsearch_demo.model.Item;
import com.example.elasticsearch_demo.repository.ItemRepository;
import com.example.elasticsearch_demo.service.JsonDataService;
import com.example.elasticsearch_demo.util.ESUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements com.example.elasticsearch_demo.service.ItemService {

    private final ItemRepository itemRepository;
    private final JsonDataService jsonDataService;
    private final ElasticsearchClient elasticsearchClient;
    private final List<String> indexNames;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, JsonDataServiceImpl jsonDataService, ElasticsearchClient elasticsearchClient) {
        this.itemRepository = itemRepository;
        this.jsonDataService = jsonDataService;
        this.elasticsearchClient = elasticsearchClient;
        this.indexNames = new ArrayList<>();
        loadIndexNames();
    }

    @Override
    public Item createIndex(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void addItemsFromJson() {
        log.info("Adding items From json");
        List<Item> itemList = jsonDataService.readItemsFromJson();
        itemRepository.saveAll(itemList);
    }

    @Override
    public List<Item> getAllDataFromIndex(String indexName) {
        var query = ESUtil.createMatchAllQuery();
        log.info("Elasticsearch query {}", query.toString());
        SearchResponse<Item> response = null;
        try {
            response = elasticsearchClient.search(
                    q -> q.index(indexName).query(query), Item.class);
        } catch (IOException e) {
            log.error("An error occurred in the Elasticsearch query: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }

        log.info("Elasticsearch response {}", response);
        return extractItemsFromResponse(response);
    }

    @Override
    public List<Item> extractItemsFromResponse(SearchResponse<Item> response) {
        return response
                .hits()
                .hits()
                .stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItemsByFiledAndValue(SearchRequestDto dto) {
        Supplier<Query> query = ESUtil.buildQueryForFieldAndValue(dto.getFieldName().get(0),
                dto.getSearchValue().get(0));
        log.info("Elasticsearch query {}", query.toString());
        SearchResponse<Item> response = null;

        try {
            response = elasticsearchClient.search(q -> q.index("items_index").
                    query(query.get()), Item.class);
        } catch (IOException e) {
            log.error("An error occurred in the Elasticsearch query: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        log.info("Elasticsearch response {}", response);
        return extractItemsFromResponse(response);
    }

    @Override
    public List<Item> searchItemsByNameAndBrandWithQuery(String name, String brand) {
        return itemRepository.searchByNameAndBrand(name, brand);
    }

    @Override
    public List<Item> boolQuery(SearchRequestDto dto) {
        var query = ESUtil.createBoolQuery(dto);
        log.info("Elasticsearch query {}", query.toString());
        SearchResponse<Item> response = null;

        try {
            response = elasticsearchClient.search(q -> q.index("items_index").query(query.get()), Item.class);
        } catch (IOException e) {
            log.error("An error occurred in the Elasticsearch query: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        log.info("Elasticsearch response {}", response.toString());
        return extractItemsFromResponse(response);
    }

    @Override
    public Set<String> findSuggestedItemNames(String name) {
        Query query = ESUtil.buildAutoSuggestQuery(name);
        log.info("Elasticsearch query {}", query.toString());

        try {
            return elasticsearchClient.search(q -> q.index("items_index").query(query), Item.class)
                    .hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .map(Item::getName)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            log.error("An error occurred in the Elasticsearch query: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> autoSuggestItemByNameWithQuery(String name) {
        List<Item> itemList = itemRepository.customAutoSuggest(name);
        log.info("Elasticsearch response {}", itemList.toString());
        return itemList.stream()
                .map(Item::getName)
                .collect(Collectors.toList());

    }

    @Override
    public void deleteIndex(String indexName) {
        try {
            DeleteIndexRequest deleteRequest = new DeleteIndexRequest.Builder()
                    .index(indexName)
                    .build();

            elasticsearchClient.indices().delete(deleteRequest);
            log.info("Index {} deleted successfully", indexName);
        } catch (IOException e) {
            log.error("Error deleting index {}: {}", indexName, e.getMessage(), e);
            throw new RuntimeException("Failed to delete index: " + indexName, e);
        }
    }

    private void loadIndexNames() {
        indexNames.add("items_index");
        indexNames.add("other_index");
    }

    @Override
    public List<String> getIndexNames() {
        return indexNames;
    }


}