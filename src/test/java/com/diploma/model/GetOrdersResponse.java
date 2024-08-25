package com.diploma.model;

import java.util.List;

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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalToday() {
        return totalToday;
    }

    public void setTotalToday(Integer totalToday) {
        this.totalToday = totalToday;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
