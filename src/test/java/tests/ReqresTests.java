package tests;


import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

@Tag("reqres_tests")
@DisplayName("Тестирование сайта https://reqres.in/")

public class ReqresTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in/";
        RestAssured.basePath = "/api";
    }

    @DisplayName("Создание нового пользователя кода ответа 201")
    @Test
    void successfulCreatedUserTest() {
        String bodyData = "{\"name\": \"amongus_red\", \"job\": \"killer\"}";

        given()
                .filter(new AllureRestAssured())
                .body(bodyData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post("/users")

                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("amongus_red"))
                .body("job", is("killer"));
    }


    @DisplayName("Проверка пагинации страницы пользователей - выводит 6 пользователей (код 200)")
    @Test
    void checkUserListItemsCount() {
        given()
                .log().uri()
                .get( "/users?page=2")
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
                .get("/unknown/23")
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
                .post( "/login")

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
                .get( "/users/7")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.email", is("michael.lawson@reqres.in"));

    }


}


