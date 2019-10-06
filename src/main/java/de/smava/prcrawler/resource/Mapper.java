package de.smava.prcrawler.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Mapper {
    private ObjectMapper objectMapper;

    public Mapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<Activity> parseActivities(InputStream is) {
        Response<Activity> response = parse(is, new TypeReference<>() {});
        return response.getValues();
    }

    public <T> Response<T> parse(InputStream is, TypeReference<Response<T>> type) {
        try {
            return objectMapper.readValue(is, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
