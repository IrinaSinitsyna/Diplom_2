package com.diploma;

import com.diploma.model.CreateUserRequest;
import com.diploma.model.CreateUserResponse;
import com.diploma.model.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.UUID;

import static com.diploma.TestUtils.deleteUser;
import static com.diploma.TestUtils.post;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

public class UserCreationTest extends BaseTest {

    public static final String USER_CREATION_PATH = "/auth/register";
    private static final String EXPECTED_VALIDATION_MESSAGE = "Email, password and name are required fields";
    private static final String EXPECTED_ALREADY_EXISTS_MESSAGE = "User already exists";

    @Test
    @DisplayName("Check that user creation returns expected response and code")
    public void shouldCreateUserSuccessfullyWhenCorrectRequestIsPassed() {
        String expectedEmail = UUID.randomUUID() + "-test-data@yandex.ru";
        String password = "password";
        CreateUserResponse createUserResponse = createUserSuccessfully(expectedEmail, password);
        deleteUser(createUserResponse.getAccessToken());
    }

    @Test
    @DisplayName("Check that user creation fails if password is not provided")
    public void shouldNotCreateUserWhenPasswordIsNotProvided() {
        String expectedEmail = UUID.randomUUID() + "-test-data@yandex.ru";
        String expectedName = "name";
        CreateUserRequest createUserRequest = new CreateUserRequest(
                expectedEmail,
                null,
                expectedName
        );

        Response response = post(USER_CREATION_PATH, createUserRequest);
        validateFailedUserCreation(response, SC_FORBIDDEN, EXPECTED_VALIDATION_MESSAGE);
    }

    @Test
    @DisplayName("Check that user creation fails if email is not provided")
    public void shouldNotCreateUserWhenEmailIsNotProvided() {
        String expectedName = "name";
        CreateUserRequest createUserRequest = new CreateUserRequest(
                null,
                "password",
                expectedName
        );

        Response response = post(USER_CREATION_PATH, createUserRequest);
        validateFailedUserCreation(response, SC_FORBIDDEN, EXPECTED_VALIDATION_MESSAGE);
    }

    @Test
    @DisplayName("Check that user creation fails if name is not provided")
    public void shouldNotCreateUserWhenNameIsNotProvided() {
        String expectedEmail = UUID.randomUUID() + "-test-data@yandex.ru";
        CreateUserRequest createUserRequest = new CreateUserRequest(
                expectedEmail,
                "password",
                null
        );

        Response response = post(USER_CREATION_PATH, createUserRequest);
        validateFailedUserCreation(response, SC_FORBIDDEN, EXPECTED_VALIDATION_MESSAGE);
    }

    @Test
    @DisplayName("Check that user creation fail if user already exists")
    public void shouldNotCreateUserWhenUserAlreadyExists() {
        String expectedEmail = UUID.randomUUID() + "-test-data@yandex.ru";
        String expectedName = "name";
        CreateUserRequest createUserRequest = new CreateUserRequest(
                expectedEmail,
                "password",
                expectedName
        );

        Response firstResponse = post(USER_CREATION_PATH, createUserRequest);
        validateSuccessfulUserCreation(expectedEmail, expectedName, firstResponse);

        Response secondResponse = post(USER_CREATION_PATH, createUserRequest);
        validateFailedUserCreation(secondResponse, SC_FORBIDDEN, EXPECTED_ALREADY_EXISTS_MESSAGE);
    }

    public static CreateUserResponse createUserSuccessfully(String email, String password) {
        String name = "name";
        CreateUserRequest createUserRequest = new CreateUserRequest(
                email,
                password,
                name
        );

        Response response = post(USER_CREATION_PATH, createUserRequest);

        return validateSuccessfulUserCreation(email, name, response);
    }

    public static CreateUserResponse validateSuccessfulUserCreation(String email,
                                                                    String name,
                                                                    Response response) {
        response.then().assertThat().statusCode(SC_OK);
        CreateUserResponse createUserResponse = response.as(CreateUserResponse.class);
        User user = createUserResponse.getUser();
        assertTrue(createUserResponse.getSuccess());
        assertFalse(createUserResponse.getAccessToken().isBlank());
        assertFalse(createUserResponse.getRefreshToken().isBlank());
        assertNull(createUserResponse.getMessage());
        assertEquals(user.getEmail(), email);
        assertEquals(user.getName(), name);
        return createUserResponse;
    }

    public static void validateFailedUserCreation(Response response, int expectedStatusCode, String expectedMessage) {
        response.then().assertThat().statusCode(expectedStatusCode);
        CreateUserResponse createUserResponse = response.as(CreateUserResponse.class);
        User user = createUserResponse.getUser();
        assertFalse(createUserResponse.getSuccess());
        assertEquals(expectedMessage, createUserResponse.getMessage());
        assertNull(createUserResponse.getAccessToken());
        assertNull(createUserResponse.getRefreshToken());
        assertNull(user);
    }
}
