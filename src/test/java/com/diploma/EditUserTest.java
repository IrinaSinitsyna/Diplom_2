package com.diploma;

import com.diploma.model.CreateUserRequest;
import com.diploma.model.CreateUserResponse;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static com.diploma.LoginTest.logout;
import static com.diploma.TestUtils.*;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class EditUserTest extends BaseTest {

    public static final String USER_PATH = "/auth/user";

    @Test
    @DisplayName("Check that user name is changed when patch request is authorized")
    public void shouldChangeUserNameSuccessfullyWhenPatchRequestIsAuthorized() {
        // Edit user name
        String expectedName = "newName";
        Response patchResponse = patchWithAuth(
                USER_PATH,
                new CreateUserRequest(null, null, expectedName),
                accessToken
        );
        CreateUserResponse editUserResponse = patchResponse.as(CreateUserResponse.class);
        assertTrue(editUserResponse.getSuccess());
        assertEquals(expectedName, editUserResponse.getUser().getName());
        assertEquals(email, editUserResponse.getUser().getEmail());

        // Check that name actually changed
        Response response = getWithAuth(USER_PATH, accessToken);
        CreateUserResponse getUserResponse = response.as(CreateUserResponse.class);
        assertTrue(getUserResponse.getSuccess());
        assertEquals(expectedName, getUserResponse.getUser().getName());
        assertEquals(email, getUserResponse.getUser().getEmail());
    }

    @Test
    @DisplayName("Check that user email is changed when patch request is authorized")
    public void shouldChangeUserEmailSuccessfullyWhenPatchRequestIsAuthorized() {
        // Edit user email
        String expectedEmail = "new_" + email;
        Response patchResponse = patchWithAuth(
                USER_PATH,
                new CreateUserRequest(expectedEmail, null, null),
                accessToken
        );
        CreateUserResponse editUserResponse = patchResponse.as(CreateUserResponse.class);
        assertTrue(editUserResponse.getSuccess());
        assertEquals("name", editUserResponse.getUser().getName());
        assertEquals(expectedEmail, editUserResponse.getUser().getEmail());

        // Check that name actually changed
        Response response = getWithAuth(USER_PATH, accessToken);
        CreateUserResponse getUserResponse = response.as(CreateUserResponse.class);
        assertTrue(getUserResponse.getSuccess());
        assertEquals("name", getUserResponse.getUser().getName());
        assertEquals(expectedEmail, getUserResponse.getUser().getEmail());
    }

    @Test
    @DisplayName("Check that user email and name are changed when patch request is authorized")
    public void shouldChangeUserEmailAndNameSuccessfullyWhenPatchRequestIsAuthorized() {
        // Edit user email and name
        String expectedName = "newName";
        String expectedEmail = "new_" + email;
        Response patchResponse = patchWithAuth(
                USER_PATH,
                new CreateUserRequest(expectedEmail, null, expectedName),
                accessToken
        );
        CreateUserResponse editUserResponse = patchResponse.as(CreateUserResponse.class);
        assertTrue(editUserResponse.getSuccess());
        assertEquals(expectedName, editUserResponse.getUser().getName());
        assertEquals(expectedEmail, editUserResponse.getUser().getEmail());

        // Check that name and email are actually changed
        Response response = getWithAuth(USER_PATH, accessToken);
        CreateUserResponse getUserResponse = response.as(CreateUserResponse.class);
        assertTrue(getUserResponse.getSuccess());
        assertEquals(expectedName, getUserResponse.getUser().getName());
        assertEquals(expectedEmail, getUserResponse.getUser().getEmail());
    }

    @Test
    @DisplayName("Check that request fails when patch request is not authorized")
    public void shouldFailWhenPatchRequestIsNotAuthorized() {
        // Edit user name
        String expectedName = "newName";
        Response patchResponse = patch(
                USER_PATH,
                new CreateUserRequest(null, null, expectedName)
        );
        patchResponse.then().statusCode(SC_UNAUTHORIZED);
        CreateUserResponse editUserResponse = patchResponse.as(CreateUserResponse.class);

        assertFalse(editUserResponse.getSuccess());
        assertEquals("You should be authorised", editUserResponse.getMessage());
    }

    @Test
    @DisplayName("Check that edit request fails when user logged out")
    public void shouldFailEditRequestWhenUserLoggedOut() {
        logout(refreshToken);

        // Edit user name
        String expectedName = "newName";
        Response patchResponse = patchWithAuth(
                USER_PATH,
                new CreateUserRequest(null, null, expectedName),
                accessToken
        );
        CreateUserResponse editUserResponse = patchResponse.as(CreateUserResponse.class);
        patchResponse.then().statusCode(SC_UNAUTHORIZED);
        assertFalse(editUserResponse.getSuccess());
        assertEquals("You should be authorised", editUserResponse.getMessage());

        deleteUser(accessToken);
    }
}
