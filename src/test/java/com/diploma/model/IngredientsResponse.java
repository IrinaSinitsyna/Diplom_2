package com.diploma.model;

import java.util.List;

public class IngredientsResponse {
    private Boolean success;
    private List<Ingredient> data;

    public IngredientsResponse(Boolean success, List<Ingredient> data) {
        this.success = success;
        this.data = data;
    }

    public List<Ingredient> getData() {
        return data;
    }

    public void setData(List<Ingredient> data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
