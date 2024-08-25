package com.diploma;

import com.diploma.model.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.diploma.TestUtils.*;
import static java.util.stream.Collectors.toList;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class OrderCreationTest {

    public static final String ORDER_PATH = "/orders";
    public static final String INGREDIENTS_PATH = "/ingredients";

    @Before
    public void setUp() {
        RestAssured.baseURI = PRACTICUM;
    }

    @Test
    @DisplayName("Check that order is created when ingredients are passed")
    public void shouldCreateOrderWhenIngredientsArePassed() {
        String email = UUID.randomUUID() + "-test-data@yandex.ru";
        String password = "password";
        CreateUserResponse createUserResponse = UserCreationTest.createUserSuccessfully(email, password);

        createOrder(createUserResponse.getAccessToken());
    }

    public static Order createOrder(String accessToken) {
        List<Ingredient> randomIngredients = getRandomIngredients(3);
        Response response = postWithAuth(
                ORDER_PATH,
                new CreateOrderRequest(
                        randomIngredients.stream().map(Ingredient::getId).collect(toList())
                ),
                accessToken
        );
        response.then().statusCode(SC_OK);
        CreateOrderResponse createOrderResponse = response.as(CreateOrderResponse.class);

        assertFalse(createOrderResponse.getName().isBlank());
        assertTrue(createOrderResponse.getSuccess());
        assertNotNull(createOrderResponse.getOrder().getNumber());

        return createOrderResponse.getOrder();
    }

    @Test
    @DisplayName("Check that order is not created when ingredients are not passed")
    public void shouldNotCreateOrderWhenIngredientsAreNotPassed() {
        Response response = post(
                ORDER_PATH,
                new CreateOrderRequest(
                        List.of()
                )
        );
        response.then().statusCode(SC_BAD_REQUEST);
        CreateOrderResponse createOrderResponse = response.as(CreateOrderResponse.class);

        assertNull(createOrderResponse.getName());
        assertFalse(createOrderResponse.getSuccess());
        assertNull(createOrderResponse.getOrder());
    }

    @Test
    @DisplayName("Check that order is not created when ingredients are incorrect")
    public void shouldNotCreateOrderWhenIngredientsAreNotCorrect() {
        String expectedResponse = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>Error</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<pre>Internal Server Error</pre>\n" +
                "</body>\n" +
                "</html>\n";
        Response response = post(
                ORDER_PATH,
                new CreateOrderRequest(
                        List.of("incorrect", "ingredients")
                )
        );
        response.then().statusCode(SC_INTERNAL_SERVER_ERROR);
        String createOrderResponse = response.body().asString();

        assertEquals(expectedResponse, createOrderResponse);
    }

    @Test
    @DisplayName("Check that order creation fails when request is not authorized")
    public void shouldNotCreateOrderWhenRequestIsNotAuthorized() {
        List<Ingredient> randomIngredients = getRandomIngredients(3);
        Response response = post(
                ORDER_PATH,
                new CreateOrderRequest(
                        randomIngredients.stream().map(Ingredient::getId).collect(toList())
                )
        );
        response.then().statusCode(SC_UNAUTHORIZED);
        CreateOrderResponse createOrderResponse = response.as(CreateOrderResponse.class);

        assertNull(createOrderResponse.getName());
        assertFalse(createOrderResponse.getSuccess());
        assertNull(createOrderResponse.getOrder());
    }

    private static List<Ingredient> getRandomIngredients(int amount) {
        IngredientsResponse ingredientsResponse = get(INGREDIENTS_PATH).as(IngredientsResponse.class);
        Random random = new Random();
        List<Ingredient> ingredients = ingredientsResponse.getData();
        int size = ingredients.size();
        List<Ingredient> requestedIngredients = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            requestedIngredients.add(ingredients.get(random.nextInt(size)));
        }

        return requestedIngredients;
    }
}
