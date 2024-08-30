package com.diploma.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
