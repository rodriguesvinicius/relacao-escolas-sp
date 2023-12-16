package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiRequest {
    private String resource_id;
    private String q;
    private Filters filters;
    private int limit;
    private int offset;
    private String sort;

    @Getter
    @Setter
    public static class Filters {
        @JsonProperty("TIPOESC")
        private List<String> TIPOESC;
    }
}

