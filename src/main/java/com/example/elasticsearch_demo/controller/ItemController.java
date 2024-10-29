package com.example.elasticsearch_demo.controller;


import com.example.elasticsearch_demo.dto.SearchRequestDto;
import com.example.elasticsearch_demo.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.elasticsearch_demo.model.Item;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    // TODO createIndex
    @PostMapping
    public Item createIndex(@RequestBody Item item){
        return itemService.createIndex(item);
    }

    // TODO addItemsFromJson
    @PostMapping("/init-index")
    public void addItemsFromJson(){
        itemService.addItemsFromJson();
    }

    // TODO getAllDataFromIndex
    @GetMapping("/getAllDataFromIndex/{indexName}")
    public List<Item> getAllDataFromIndex(@PathVariable String indexName){
        return itemService.getAllDataFromIndex(indexName);
    }

    // TODO searchItemsByFiledAndValue
    @GetMapping("/search")
    public List<Item> searchItemsByFiledAndValue(@RequestBody SearchRequestDto dto){
        return itemService.searchItemsByFiledAndValue(dto);
    }

    // TODO searchItemsByNameAndBrandWithQuery
    @GetMapping("/search/{name}/{brand}")
    public List<Item> searchItemsByNameAndBrandWithQuery(@PathVariable String name,
                                                         @PathVariable String brand){
        return itemService.searchItemsByNameAndBrandWithQuery(name,brand);
    }

    // TODO boolQuery
    @GetMapping("/boolQuery")
    public List<Item> boolQuery(@RequestBody SearchRequestDto dto){
        return itemService.boolQuery(dto);
    }


    // TODO autoSuggestItemsByName
    @GetMapping("/autoSuggest/{name}")
    public Set<String> autoSuggestItemsByName(@PathVariable String name){
        return itemService.findSuggestedItemNames(name);
    }

    // TODO autoSuggestItemsByNameWithQuery
    @GetMapping("/suggestionsQuery/{name}")
    public List<String> autoSuggestItemsByNameWithQuery(@PathVariable String name){
        return itemService.autoSuggestItemByNameWithQuery(name);
    }

    // TODO deleteIndexName
    @DeleteMapping("/delete-index/{indexName}")
    public void deleteIndex(@PathVariable String indexName) {
        itemService.deleteIndex(indexName);
    }

    // TODO foundIndexName
    @GetMapping("/foundIndexName")
    public ResponseEntity<List<String>> getIndexNames() {
        List<String> indexNames = itemService.getIndexNames();
        return ResponseEntity.ok(indexNames);
    }





}
