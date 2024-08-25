package com.diploma.model;

public class CreateUserResponse extends BaseResponse {
    private User user;
    private String accessToken;
    private String refreshToken;

    public CreateUserResponse(Boolean success, User user, String accessToken, String refreshToken, String message) {
        super(success, message);
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
