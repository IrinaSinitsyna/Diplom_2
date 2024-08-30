package com.diploma.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateOrderResponse {

    private String name;
    private Boolean success;
    private Order order;
}
