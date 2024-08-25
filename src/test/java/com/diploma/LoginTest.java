package com.diploma;

import com.diploma.model.BaseResponse;
import com.diploma.model.CreateUserRequest;
import com.diploma.model.CreateUserResponse;
import com.diploma.model.LogoutRequest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static com.diploma.TestUtils.PRACTICUM;
import static com.diploma.TestUtils.post;
import static com.diploma.UserCreationTest.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginTest {

    public static final String LOGIN_PATH = "/auth/login";
    public static final String LOGOUT_PATH = "/auth/logout";
    private static final String LOGIN_FAILED_MESSAGE = "email or password are incorrect";
    private static final String LOGOUT_MESSAGE = "Successful logout";

    @Before
    public void setUp() {
        RestAssured.baseURI = PRACTICUM;
    }

    @Test
    @DisplayName("Check that login works as expected when password is correct")
    public void shouldSuccessfullyLoginWhenPasswordIsCorrect() {
        String email = UUID.randomUUID() + "-test-data@yandex.ru";
        String password = "password";
        CreateUserResponse createUserResponse = createUserSuccessfully(email, password);
        String name = createUserResponse.getUser().getName();

        logout(createUserResponse.getRefreshToken());
        Response response = login(email, password);

        validateSuccessfulUserCreation(email, name, response);
    }

    @Test
    @DisplayName("Check that login will fail as expected when password is incorrect")
    public void shouldNotLoginWhenPasswordIsInCorrect() {
        String email = UUID.randomUUID() + "-test-data@yandex.ru";
        String password = "password";
        CreateUserResponse createUserResponse = createUserSuccessfully(email, password);

        logout(createUserResponse.getRefreshToken());
        String incorrectPassword = UUID.randomUUID().toString();
        Response response = login(email, incorrectPassword);

        validateFailedUserCreation(response, SC_UNAUTHORIZED, LOGIN_FAILED_MESSAGE);
    }

    @Test
    @DisplayName("Check that login will fail as expected when password is missing")
    public void shouldNotLoginWhenPasswordIsMissing() {
        String email = UUID.randomUUID() + "-test-data@yandex.ru";
        String password = "password";
        CreateUserResponse createUserResponse = createUserSuccessfully(email, password);

        logout(createUserResponse.getRefreshToken());
        String incorrectPassword = null;
        Response response = login(email, incorrectPassword);

        validateFailedUserCreation(response, SC_UNAUTHORIZED, LOGIN_FAILED_MESSAGE);
    }

    @Test
    @DisplayName("Check that login will fail as expected when email is incorrect")
    public void shouldNotLoginWhenEmailIsIncorrect() {
        String email = UUID.randomUUID() + "-test-data@yandex.ru";
        String password = "password";
        CreateUserResponse createUserResponse = createUserSuccessfully(email, password);

        logout(createUserResponse.getRefreshToken());
        String incorrectEmail = UUID.randomUUID().toString();
        Response response = login(incorrectEmail, password);

        validateFailedUserCreation(response, SC_UNAUTHORIZED, LOGIN_FAILED_MESSAGE);
    }

    @Test
    @DisplayName("Check that login will fail as expected when email is missing")
    public void shouldNotLoginWhenEmailIsMissing() {
        String email = UUID.randomUUID() + "-test-data@yandex.ru";
        String password = "password";
        CreateUserResponse createUserResponse = createUserSuccessfully(email, password);

        logout(createUserResponse.getRefreshToken());
        String incorrectEmail = null;
        Response response = login(incorrectEmail, password);

        validateFailedUserCreation(response, SC_UNAUTHORIZED, LOGIN_FAILED_MESSAGE);
    }

    public static Response login(String email, String password) {
        CreateUserRequest createUserRequest = new CreateUserRequest(email, password, null);
        Response response = post(LOGIN_PATH, createUserRequest);
        return response;
    }

    public static void logout(String refreshToken) {
        Response response = post(LOGOUT_PATH, new LogoutRequest(refreshToken));

        response.then().statusCode(SC_OK);
        BaseResponse logoutResponse = response.as(BaseResponse.class);
        assertTrue(logoutResponse.getSuccess());
        assertEquals(LOGOUT_MESSAGE, logoutResponse.getMessage());
    }
}
