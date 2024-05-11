package tests;


import io.restassured.RestAssured;
import models.UserDataCreatedModel;
import models.UserDataModel;
import models.UserDataResModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static specs.UserSpec.*;

@Tag("reqres_tests")
@DisplayName("Тестирование сайта https://reqres.in/")

public class ReqresTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in/";
        RestAssured.basePath = "/api";
    }

//    @DisplayName("Создание нового пользователя.")
//    @Test
//    void successfulCreatedUserTest() {
//        UserDataCreatedModel userData = new UserDataCreatedModel();
//        userData.setName("amongus_red");
//        userData.setJob("killer");
//
//        UserDataResModel response = step("Отправка запроса.", () ->
//                given(UserRequestSpec)
//                    .body(userData)
//                .when()
//                    .post("/users")
//                .then()
//                    .spec(UserResponseCreateSpec)
//                    .extract().as(UserDataResModel.class));
//
//        step("Проверка ответа.", () -> {
//            assertEquals(userData.getJob(), response.getJob());
//            assertEquals(userData.getName(), response.getName());
//        });
//    }


    @DisplayName("Проверка пагинации страницы пользователей - выводит 6 пользователей (код 200)")
    @Test
    void checkUserListItemsCount() {
        step("Отправка запроса.", () ->
        given(UserRequestSpec)
        .when()
                .get("/users?page=2")
        .then()
                .spec(UsersListResponseSpec)
                .time(lessThan(1500L))
                .body("data.findall.size()", equalTo(6)));


    }

    @DisplayName("Проверка кода ответа - NOT FOUND (код 404)")
    @Test
    void checkCode404() {
        given(UnknownRequestSpec)
                .get("/unknown/23")
            .then()
                .spec(UnknownResponseSpec)
                .extract().as(UserDataResModel.class);
    }

    @DisplayName("Проверка не валидной авторизации пользователя")
    @Test
    void loginUserUnsuccessfulTest() {
        String postLoginData = "{\"email\": \"amongus@killer\"}";

        given()
                .body(postLoginData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post("/login")

                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }


    @DisplayName("Проверка email у пользователя")
    @Test
    void checkEmailTest() {
        UserDataModel userData = new UserDataModel();
        userData.setEmail("michael.lawson@reqres.in");

        UserDataResModel response = step("Отправка запроса.", () ->
        given()
                .log().uri()
            .when()
                .get("/users/7")
            .then()
                .spec(UsersListResponseSpec)
                .extract().as(UserDataResModel.class));

        step("Проверка ответа.", () -> {
            assertEquals(userData.getEmail(), response.getData().getEmail());
        });

    }


}


