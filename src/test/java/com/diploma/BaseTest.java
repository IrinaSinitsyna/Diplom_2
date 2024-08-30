package com.diploma;

import com.diploma.model.CreateUserResponse;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;

import java.util.UUID;

import static com.diploma.TestUtils.PRACTICUM;
import static com.diploma.TestUtils.deleteUser;
import static com.diploma.UserCreationTest.createUserSuccessfully;

public class BaseTest {
    static {
        RestAssured.baseURI = PRACTICUM;
    }

    protected String email;
    protected String password;
    protected String name;
    protected String accessToken;
    protected String refreshToken;

    @Before
    public void init() {
        email = UUID.randomUUID() + "-test-data@yandex.ru";
        password = "password";
        CreateUserResponse createUserResponse = createUserSuccessfully(email, password);
        accessToken = createUserResponse.getAccessToken();
        refreshToken = createUserResponse.getRefreshToken();
        name = createUserResponse.getUser().getName();
    }

    @After
    public void cleanUp() {
        deleteUser(accessToken);
    }
}
