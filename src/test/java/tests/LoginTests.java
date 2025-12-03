package tests;

import io.restassured.RestAssured;
import models.LoginBodyModel;
import models.LoginResponseModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static specs.LoginSpec.loginRequestSpec;
import static specs.LoginSpec.loginResponseSpec;

public class LoginTests extends BaseTest {


    @Test
    void successfulLoginTests() {
        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginResponseModel response = step("Отправка запроса авторизации.", () ->
                given(loginRequestSpec)
                .body(authData)

            .when()
                .post( "/login")

            .then()
                .spec(loginResponseSpec)
                .extract().as(LoginResponseModel.class));

        step("Проверка ответа", () ->
        assertNotNull(response.getToken()));
    }
}
