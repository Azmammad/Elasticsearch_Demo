package com.example.elasticsearch_demo.repository;

import com.example.elasticsearch_demo.model.Item;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ItemRepository extends ElasticsearchRepository<Item, String> {

    @Query("{\"bool\":{\"must\":[{\"match\":{\"name\": \"?0\"}}, {\"match\": {\"brand\": \"?1\"}}]}}")
    List<Item> searchByNameAndBrand(String name, String brand);

    @Query("{\"bool\":{\"must\":{\"match_phrase_prefix\":{\"name\": \"?0\"}}}}")
    List<Item> customAutoSuggest(String name);
}
