package com.diploma.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetOrdersResponse extends BaseResponse {

    private Integer total;
    private Integer totalToday;
    private List<Order> orders;

    public GetOrdersResponse(Boolean success, String message, Integer total, Integer totalToday, List<Order> orders) {
        super(success, message);
        this.total = total;
        this.totalToday = totalToday;
        this.orders = orders;
    }
}
