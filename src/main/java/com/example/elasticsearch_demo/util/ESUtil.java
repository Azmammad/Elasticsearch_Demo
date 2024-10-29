package com.example.elasticsearch_demo.util;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.termvectors.Term;
import co.elastic.clients.util.ObjectBuilder;
import com.example.elasticsearch_demo.dto.SearchRequestDto;
import lombok.experimental.UtilityClass;


import java.util.function.Function;
import java.util.function.Supplier;

@UtilityClass
public class ESUtil {

    // TODO bos query this index all get
    public static Query createMatchAllQuery(){
        return Query.of(q-> q.matchAll(new MatchAllQuery.Builder().build()));
    }

    public static Supplier<Query> buildQueryForFieldAndValue(String fieldName,String searchValue) {
        return ()-> Query.of(q-> q.match(buildMatchQueryForFieldAndValue(fieldName,searchValue)));
    }

    private static MatchQuery buildMatchQueryForFieldAndValue(String fieldName, String searchValue) {
        return new MatchQuery.Builder()
                .field(fieldName)
                .query(searchValue)
                .build();
    }


    public static Supplier<Query> createBoolQuery(SearchRequestDto dto) {
        return ()-> Query.of(q-> q.bool(boolQuery(dto.getFieldName().get(0),dto.getSearchValue().get(0),
                dto.getFieldName().get(1),dto.getSearchValue().get(1))));
        
    }

    private static BoolQuery boolQuery(String key1, String value1, String key2, String value2) {
        return new BoolQuery.Builder()
                .filter(termQuery(key1,value1))
                .must(matchQuery(key2,value2))
                .build();
    }

    private static Query termQuery(String field, String value) {
        return Query.of(q-> q.term(new TermQuery.Builder()
                .field(field)
                .value(value)
                .build()));
    }

    private static Query matchQuery(String field, String value) {
        return Query.of(q-> q.match(new MatchQuery.Builder()
                .field(field)
                .query(value)
                .build()));
    }

    public static Query buildAutoSuggestQuery(String name) {
        return Query.of(q-> q.match(new MatchQuery.Builder()
                .field("name")
                .query(name)
                .build()));
    }
}
