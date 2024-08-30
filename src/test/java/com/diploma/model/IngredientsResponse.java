package com.diploma.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class IngredientsResponse {
    private Boolean success;
    private List<Ingredient> data;
}
