package com.diploma;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.hamcrest.core.Is;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static io.restassured.RestAssured.given;

public class TestUtils {

    public static final String PRACTICUM = "https://stellarburgers.nomoreparties.site/api";

    public static String getStringFromPath(String path) throws IOException {
        File jsonFile = new File(path);
        return Files.readString(jsonFile.toPath());
    }


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

    public static void assertExpectedBodyAndCode(Response response, String body, int code) {
        response.then().assertThat().body(Is.is(body))
                .and()
                .statusCode(code);
    }
}
