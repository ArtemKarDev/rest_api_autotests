package tests;

import io.restassured.RestAssured;
import models.LoginBodyModel;
import models.LoginResponseModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

public class LoginTests extends BaseTest {


    @Test
    void successfulLoginTests() {
        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginResponseModel response = step("Отправка запроса авторизации.", () ->
                given(requestSpec)
                .body(authData)

            .when()
                .post( "/login")

            .then()
                .spec(responseSpec(200))
                .extract().as(LoginResponseModel.class));

        step("Проверка ответа", () ->
        assertNotNull(response.getToken()));
    }
}
