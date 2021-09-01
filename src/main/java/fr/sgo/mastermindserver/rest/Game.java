package fr.sgo.mastermindserver.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@JsonSerialize
public class Game implements Serializable {
    @JsonProperty
    private final UUID id;
    @JsonProperty
    private final int size;
    @JsonProperty
    private final List<String> colors;

    public Game(UUID id, int size, List<String> colors) {
        this.id = id;
        this.size = size;
        this.colors = colors;
    }
}
