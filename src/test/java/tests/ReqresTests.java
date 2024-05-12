package tests;

import io.restassured.RestAssured;
import models.*;
import models.LoginBodyModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static specs.LoginSpec.*;
import static specs.UserSpec.*;

@Tag("reqres_tests")
@DisplayName("Тестирование сайта https://reqres.in/")

public class ReqresTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in/";
        RestAssured.basePath = "/api";
    }

    @DisplayName("Создание нового пользователя.")
    @Test
    void successfulCreatedUserTest() {
        UserDataCreatedModel userData = new UserDataCreatedModel();
        userData.setName("amongus_red");
        userData.setJob("killer");

        UserDataCreatedResModel response = step("Отправка запроса.", () ->
                given(userRequestSpec)
                        .body(userData)
                        .when()
                        .post("/users")
                        .then()
                        .spec(userResponseCreateSpec)
                        .extract().as(UserDataCreatedResModel.class));

        step("Проверка ответа.", () -> {
            assertEquals(userData.getJob(), response.getJob());
            assertEquals(userData.getName(), response.getName());
        });
    }


    @DisplayName("Проверка пагинации страницы пользователей - выводит 6 пользователей (код 200)")
    @Test
    void checkUserListItemsCount() {
        step("Отправка запроса.", () ->
                given(userRequestSpec)
                        .when()
                        .get("/users?page=2")
                        .then()
                        .spec(usersListResponseSpec)
                        .time(lessThan(1500L))
                        .body("data.findall.size()", equalTo(6)));
    }

    @DisplayName("Проверка кода ответа - NOT FOUND")
    @Test
    void checkCode404() {
        given(unknownRequestSpec)
                .get("/unknown/23")
                .then()
                .spec(unknownResponseSpec)
                .extract().as(UserDataResModel.class);
    }

    @DisplayName("Проверка авторизации несуществующего пользователя")
    @Test
    void loginNotUserUnsuccessfulTest() {

        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("amongus@killer");
        authData.setPassword("cityslicka");

        LoginErrorModel response = step("Отправка запроса.", () ->
                given(loginRequestSpec)
                        .body(authData)
                        .when()
                        .post("/login")
                        .then()
                        .spec(loginResponse400Spec)
                        .extract().as(LoginErrorModel.class));

        step("Проверка ответа.", () -> {
            assertEquals("user not found", response.getError());
        });
    }

    @DisplayName("Проверка авторизации пользователя с неверным паролем")
    @Test
    void loginUserUnsuccessfulTest() {

        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        //authData.setPassword("pass");

        LoginErrorModel response = step("Отправка запроса.", () ->
                given(loginRequestSpec)
                        .body(authData)
                        .when()
                        .post("/login")
                        .then()
                        .spec(loginResponse400Spec)
                        .extract().as(LoginErrorModel.class));

        step("Проверка ответа.", () -> {
            assertEquals("Missing password", response.getError());
        });

    }


    @DisplayName("Проверка email у пользователя")
    @Test
    void checkEmailTest() {
        UserDataModel userData = new UserDataModel();
        userData.setEmail("michael.lawson@reqres.in");

        UserDataResModel response = step("Отправка запроса.", () ->
                given(userRequestSpec)
                        .when()
                        .get("/users/7")
                        .then()
                        .spec(usersListResponseSpec)
                        .extract().as(UserDataResModel.class));

        step("Проверка ответа.", () -> {
            assertEquals(userData.getEmail(), response.getData().getEmail());
        });

    }


}


