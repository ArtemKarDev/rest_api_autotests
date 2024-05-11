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

        assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
    }
}
//
//
//@DisplayName("Создание нового пользователя.")
//@Test
//void successfulCreatedUserTest() {
//    UserDataModel userData = new UserDataModel();
//    userData.setName("amongus_red");
//    userData.setJob("killer");
//
//    UserDataResModel response = step("Отправка запроса.", () ->
//            given(UserRequestSpec)
//                    .body(userData)
//                    .when()
//                    .post("/users")
//                    .then()
//                    .spec(UserResponseCreateSpec)
//                    .extract().as(UserDataResModel.class));
//
//    step("Проверка ответа.", () -> {
//        assertEquals(userData.getJob(), response.getJob());
//        assertEquals(userData.getName(), response.getName());
//    });
//}