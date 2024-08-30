package com.diploma;

import com.diploma.model.BaseResponse;
import com.diploma.model.CreateUserRequest;
import com.diploma.model.CreateUserResponse;
import com.diploma.model.LogoutRequest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.UUID;

import static com.diploma.TestUtils.post;
import static com.diploma.UserCreationTest.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginTest extends BaseTest {

    public static final String LOGIN_PATH = "/auth/login";
    public static final String LOGOUT_PATH = "/auth/logout";
    private static final String LOGIN_FAILED_MESSAGE = "email or password are incorrect";
    private static final String LOGOUT_MESSAGE = "Successful logout";

    @Test
    @DisplayName("Check that login works as expected when password is correct")
    public void shouldSuccessfullyLoginWhenPasswordIsCorrect() {
        logout(refreshToken);
        Response response = login(email, password);

        validateSuccessfulUserCreation(email, name, response);
    }

    @Test
    @DisplayName("Check that login will fail as expected when password is incorrect")
    public void shouldNotLoginWhenPasswordIsInCorrect() {
        logout(refreshToken);
        String incorrectPassword = UUID.randomUUID().toString();
        Response response = login(email, incorrectPassword);

        validateFailedUserCreation(response, SC_UNAUTHORIZED, LOGIN_FAILED_MESSAGE);
    }

    @Test
    @DisplayName("Check that login will fail as expected when password is missing")
    public void shouldNotLoginWhenPasswordIsMissing() {
        logout(refreshToken);
        String incorrectPassword = null;
        Response response = login(email, incorrectPassword);

        validateFailedUserCreation(response, SC_UNAUTHORIZED, LOGIN_FAILED_MESSAGE);
    }

    @Test
    @DisplayName("Check that login will fail as expected when email is incorrect")
    public void shouldNotLoginWhenEmailIsIncorrect() {
        logout(refreshToken);
        String incorrectEmail = UUID.randomUUID().toString();
        Response response = login(incorrectEmail, password);

        validateFailedUserCreation(response, SC_UNAUTHORIZED, LOGIN_FAILED_MESSAGE);
    }

    @Test
    @DisplayName("Check that login will fail as expected when email is missing")
    public void shouldNotLoginWhenEmailIsMissing() {
        logout(refreshToken);
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
