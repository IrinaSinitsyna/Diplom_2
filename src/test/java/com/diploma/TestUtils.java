package com.diploma;

import com.diploma.model.Ingredient;
import com.diploma.model.IngredientsResponse;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.diploma.EditUserTest.USER_PATH;
import static com.diploma.OrderCreationTest.INGREDIENTS_PATH;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_ACCEPTED;

public class TestUtils {

    public static final String PRACTICUM = "https://stellarburgers.nomoreparties.site/api";

    public static <T> Response post(String path, T body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(path);
    }

    public static <T> Response patch(String path, T body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .patch(path);
    }

    public static <T> Response patchWithAuth(String path, T body, String token) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .header(new Header("Authorization", token))
                .when()
                .patch(path);
    }

    public static <T> Response postWithAuth(String path, T body, String token) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .header(new Header("Authorization", token))
                .when()
                .post(path);
    }

    public static <T> Response get(String path) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(path);
    }

    public static <T> Response getWithAuth(String path, String token) {
        return given()
                .headers(
                        new Headers(
                                new Header("Content-type", "application/json"),
                                new Header("Authorization", token)
                        )
                )
                .and()
                .when()
                .get(path);
    }

    public static List<Ingredient> getRandomIngredients(int amount) {
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

    public static void deleteUser(String token) {
        given()
                .headers(
                        new Headers(
                                new Header("Content-type", "application/json"),
                                new Header("Authorization", token)
                        )
                )
                .and()
                .when()
                .delete(USER_PATH)
                .then().statusCode(SC_ACCEPTED);
    }
}
