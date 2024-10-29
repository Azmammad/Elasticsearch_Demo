package com.example.elasticsearch_demo.service;

import com.example.elasticsearch_demo.model.Item;

import java.util.List;

public interface JsonDataService {
    List<Item> readItemsFromJson();
}
