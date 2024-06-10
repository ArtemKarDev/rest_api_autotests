package tests.demowebshop;

import io.restassured.RestAssured;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

public class LoginTests extends TestBase {



    @Test
    void loginWithUITest() {
        step("Open login page", () ->
                open("/login"));
        step("Fill login from", () -> {
            $("#Email").setValue(login);
            $("#Password").setValue(password).pressEnter();

        });
        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }


    @Test
    void loginWithApiTest() {
        step("Get authorization cookie by api and set it to browser", () ->{
            String authCookieKey = "NOPCOMMERCE.AUTH";
            String authCookieValue = given()
                    // .headers("Content-type","application/x-www-form-urlencoded")
                    .contentType("application/x-www-form-urlencoded")
                    //.body(login, password)
                    .formParam("Email", login)
                    .formParam("Password", password)
                    .when()
                    .post("/login")
                    .then()
                    .statusCode(302)
                    .extract()
                    .cookie(authCookieKey);

            open("/favicon.png");
            Cookie authCookie = new Cookie(authCookieKey, authCookieValue);
            getWebDriver().manage().addCookie(authCookie);
        });
        step("Open main page", () ->
                open(""));
        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }
}
