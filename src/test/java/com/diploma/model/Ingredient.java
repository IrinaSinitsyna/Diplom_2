package com.diploma.model;

import com.google.gson.annotations.SerializedName;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;

public class Ingredient {
    @SerializedName("_id")
    private String id;

    public Ingredient(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}