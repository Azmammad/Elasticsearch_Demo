package com.example.elasticsearch_demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "items_index")
@Getter
@Setter
@ToString
@Setting(settingPath = "static/es-settings.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

    @Id
    private String id;

    @Field(name = "name", type = FieldType.Text, analyzer = "custom_index", searchAnalyzer = "custom_search")
    private String name;

    @Field(name = "price", type = FieldType.Double)
    private Double price;

    @Field(name = "brand", type = FieldType.Text, analyzer = "custom_index", searchAnalyzer = "custom_search")
    private String brand;

    @Field(name = "category", type = FieldType.Keyword)
    private String category;
}