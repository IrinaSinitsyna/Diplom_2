package com.diploma;

import com.diploma.model.CreateUserResponse;
import com.diploma.model.GetOrdersResponse;
import com.diploma.model.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static com.diploma.LoginTest.logout;
import static com.diploma.OrderCreationTest.createOrder;
import static com.diploma.TestUtils.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class GetOrderTest {

    public static final String ORDER_PATH = "/orders";
    private static final String NOT_AUTHORISED_MESSAGE = "You should be authorised";

    @Before
    public void setUp() {
        RestAssured.baseURI = PRACTICUM;
    }

    @Test
    @DisplayName("Checks that orders are returned when user is authorised")
    public void shouldReturnOrdersWhenUserIsAuthorised() {
        String email = UUID.randomUUID() + "-test-data@yandex.ru";
        String password = "password";
        CreateUserResponse createUserResponse = UserCreationTest.createUserSuccessfully(email, password);

        Order order = createOrder(createUserResponse.getAccessToken());

        Response response = getWithAuth(ORDER_PATH, createUserResponse.getAccessToken());
        response.then().statusCode(SC_OK);
        GetOrdersResponse getOrdersResponse = response.as(GetOrdersResponse.class);

        assertTrue(getOrdersResponse.getSuccess());
        assertEquals(1, getOrdersResponse.getOrders().size());
        assertEquals(order.getNumber(), getOrdersResponse.getOrders().get(0).getNumber());
    }

    @Test
    @DisplayName("Checks that orders won't be returned when user is not authorised")
    public void shouldReturnErrorWhenUserIsNotAuthorised() {
        Response response = get(ORDER_PATH);
        response.then().statusCode(SC_UNAUTHORIZED);
        GetOrdersResponse getOrdersResponse = response.as(GetOrdersResponse.class);

        assertNull(getOrdersResponse.getOrders());
        assertFalse(getOrdersResponse.getSuccess());
        assertEquals(NOT_AUTHORISED_MESSAGE, getOrdersResponse.getMessage());
    }

    @Test
    @DisplayName("Checks that orders won't be returned when user is logged out")
    public void shouldReturnErrorWhenUserIsLoggedOut() {
        String email = UUID.randomUUID() + "-test-data@yandex.ru";
        String password = "password";
        CreateUserResponse createUserResponse = UserCreationTest.createUserSuccessfully(email, password);
        Order order = createOrder(createUserResponse.getAccessToken());

        logout(createUserResponse.getRefreshToken());

        Response response = getWithAuth(ORDER_PATH, createUserResponse.getAccessToken());
        GetOrdersResponse getOrdersResponse = response.as(GetOrdersResponse.class);
        response.then().statusCode(SC_UNAUTHORIZED);

        assertNull(getOrdersResponse.getOrders());
        assertFalse(getOrdersResponse.getSuccess());
        assertEquals(NOT_AUTHORISED_MESSAGE, getOrdersResponse.getMessage());
    }
}
