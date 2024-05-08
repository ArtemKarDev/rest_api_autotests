package tests;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

import org.hamcrest.Matchers;

@DisplayName("Тестирование сайта https://reqres.in/")

public class ReqresTests {
    String BASE_URL = "https://reqres.in/";

    @DisplayName("Создание нового пользователя кода ответа 201")
    @Test
    void successfulCreatedUserTest() {
        String bodyData = "{\"name\": \"amongus_red\", \"job\": \"killer\"}";

        given()
                .body(bodyData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post(BASE_URL + "api/users")

                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("amongus_red"))
                .body("job", is("killer"))
        ;
    }


    @DisplayName("Проверка пагинации страницы пользователей - выводит 6 пользователей (код 200)")
    @Test
    void checkUserListItemsCount() {
        given()
                .log().uri()
                .get(BASE_URL + "api/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .time(lessThan(1500L))
                .body("data.findall.size()", equalTo(6));

    }

    @DisplayName("Проверка кода ответа - NOT FOUND (код 404)")
    @Test
    void checkCode404() {
        given()
                .log().uri()
                .get(BASE_URL + "api/unknown/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

    @DisplayName("Проверка не валюдной авторизации пользователя")
    @Test
    void loginUserUnsuccessfulTest() {
        String postLoginData = "{\"email\": \"amongus@killer\"}";

        given()
                .body(postLoginData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post(BASE_URL + "api/login")

                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }


    @DisplayName("Проверка email у пользователя")
    @Test
    void checkEmailTest() {
        given()
                .log().uri()
                .get(BASE_URL + "api/users/7")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.email", is("michael.lawson@reqres.in"));

    }


}


