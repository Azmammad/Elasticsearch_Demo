package com.example.elasticsearch_demo.service.impl;

import com.example.elasticsearch_demo.model.Item;
import com.example.elasticsearch_demo.service.JsonDataService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JsonDataServiceImpl implements JsonDataService {

    private final ObjectMapper objectMapper;

    @Override
    public List<Item> readItemsFromJson(){
        try{
            ClassPathResource resource = new ClassPathResource("data/items.json");
            InputStream inputStream = resource.getInputStream();
            return objectMapper.readValue(inputStream, new TypeReference<List<Item>>() {
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
