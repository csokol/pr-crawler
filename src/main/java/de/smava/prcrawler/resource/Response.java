package de.smava.prcrawler.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private List<T> values;
    private Integer size;
    private Integer limit;
    private Integer start;
    @JsonProperty("isLastPage")
    private boolean isLastPage;
}
