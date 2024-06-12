package com.demoqa.tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.demoqa.tests.TestData.login;
import static com.demoqa.tests.TestData.password;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.lang.String.format;

public class CollectionTests extends TestBase{

    @Test
    void addBookToCollectionApiTest() {

        step("Get authorization by api", () ->{
            String authData = "{\"userName\": \"test123456\", \"password\": \"Test123456@\"}";
            Response authResponse = given()
                    .log().uri()
                    .log().method()
                    .log().body()
                    .contentType(JSON)
                    .body(authData)
                    .when()
                    .post("/Account/v1/Login")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(200)
                    .extract().response();

            // delete all books from collection
            given()
                    .log().uri()
                    .log().method()
                    .log().body()
                    .contentType(JSON)
                    .header("Authorization","Bearer " + authResponse.path("token"))
                    .queryParams("UserId", authResponse.path("userId"))
                    .body(authData)
                    .when()
                    .delete("/BookStore/v1/Books")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(204);

                    String isbn = "9781449325862";

                    String bookData = format("",authResponse.path("userId"),isbn);

            given()
                    .log().uri()
                    .log().method()
                    .log().body()
                    .contentType(JSON)
                    .header("Authorization","Bearer " + authResponse.path("token"))
                    .body(bookData)
                    .when()
                    .post("/BookStore/v1/Books")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(201)
                    .extract().response();


            open("/gplaypattern.jpg");
            getWebDriver().manage().addCookie(new Cookie("userID",authResponse.path("userId")));
            getWebDriver().manage().addCookie(new Cookie("expires",authResponse.path("expires")));
            getWebDriver().manage().addCookie(new Cookie("token",authResponse.path("token")));

        });

        step("Open profile page", () ->{
                open("/profile");
        });

        step("Verify successful added book", () ->{
                $(".ReactTable").shouldHave(text("Git Pocket Guide"));
        });
    }


    @Test
    void addBookToCollectionWithDelete1BookApiTest() {

        step("Get authorization by api", () ->{
            String authData = "{\"userName\": \"test123456\", \"password\": \"Test123456@\"}";
            Response authResponse = given()
                    .log().uri()
                    .log().method()
                    .log().body()
                    .contentType(JSON)
                    .body(authData)
                    .when()
                    .post("/Account/v1/Login")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(200)
                    .extract().response();

            String isbn = "9781449325862";
            String deleteBookData = format("{\"isbn\": \"%s\", \"userId\": \"%s\"}",isbn,authResponse.path("userId"));

            // delete 1 book from collection
            given()
                    .log().uri()
                    .log().method()
                    .log().body()
                    .contentType(JSON)
                    .header("Authorization","Bearer " + authResponse.path("token"))
                    .body(deleteBookData)
                    .body(authData)
                    .when()
                    .delete("/BookStore/v1/Book")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(204);



            String bookData = format("{\"userId\":\"%s\",\"collectionOfIsbns\":[{\"isbn\":\"%s\"}]}",authResponse.path("userId"),isbn);

            given()
                    .log().uri()
                    .log().method()
                    .log().body()
                    .contentType(JSON)
                    .header("Authorization","Bearer " + authResponse.path("token"))
                    .body(bookData)
                    .when()
                    .post("/BookStore/v1/Books")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(201)
                    .extract().response();


            open("/gplaypattern.jpg");
            getWebDriver().manage().addCookie(new Cookie("userID",authResponse.path("userId")));
            getWebDriver().manage().addCookie(new Cookie("expires",authResponse.path("expires")));
            getWebDriver().manage().addCookie(new Cookie("token",authResponse.path("token")));

        });

        step("Open profile page", () ->{
            open("/profile");
        });

        step("Verify successful added book", () ->{
            $(".ReactTable").shouldHave(text("Git Pocket Guide"));
        });
    }


}

