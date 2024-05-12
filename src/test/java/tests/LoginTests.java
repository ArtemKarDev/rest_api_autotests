package tests;

import io.restassured.RestAssured;
import models.LoginBodyModel;
import models.LoginResponseModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static specs.LoginSpec.loginRequestSpec;
import static specs.LoginSpec.loginResponseSpec;

public class LoginTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in/";
        RestAssured.basePath = "/api";
    }
    @Test
    void successfulLoginTests() {
        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginResponseModel response = given(loginRequestSpec)
                .body(authData)

            .when()
                .post( "/login")

            .then()
                .spec(loginResponseSpec)
                .extract().as(LoginResponseModel.class);

        assertNotNull(response.getToken());
    }
}
