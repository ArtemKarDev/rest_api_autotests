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

public class LoginTests extends TestBase{

    @Test
    void loginWithUITest() {
        step("Open login page", () ->
                open("/login"));
        step("Fill login from", () -> {
            $("#userName").setValue(login);
            $("#password").setValue(password).pressEnter();
        });
        step("Verify successful authorization", () ->
                $("#userName-value").shouldHave(text(login)));
    }

    @Test
    void loginWithApiTest() {

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

            open("/gplaypattern.jpg");
            getWebDriver().manage().addCookie(new Cookie("userID",authResponse.path("userId")));
            getWebDriver().manage().addCookie(new Cookie("expires",authResponse.path("expires")));
            getWebDriver().manage().addCookie(new Cookie("token",authResponse.path("token")));
        });


        step("Open profile page", () ->
                open("/profile")
        );
        step("Verify successful authorization", () ->
                $("#userName-value").shouldHave(text(login)));
    }

}
