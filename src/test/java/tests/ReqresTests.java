package tests;

import models.*;
import models.LoginBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

@Tag("reqres_tests")
@DisplayName("API tests for https://reqres.in/")

public class ReqresTests extends BaseTest {



    @DisplayName("Create user (201)")
    @Test
    void successfulCreatedUserTest() {
        UserDataCreatedModel userData = new UserDataCreatedModel();
        userData.setName("amongus_red");
        userData.setJob("killer");

        UserDataCreatedResModel response = step("Send request.", () ->
                given(requestSpec)
                        .body(userData)
                        .when()
                        .post("/users")
                        .then()
                        .spec(responseSpec(201))
                        .extract().as(UserDataCreatedResModel.class));

        step("Проверка ответа.", () -> {
            assertEquals(userData.getJob(), response.getJob());
            assertEquals(userData.getName(), response.getName());
        });
    }


    @DisplayName("Check paging user list - output 6 items")
    @Test
    void checkUserListItemsCount() {
        step("Send request.", () ->
                given(requestSpec)
                        .when()
                        .get("/users?page=2")
                        .then()
                        .spec(responseSpec(200))
                        .time(lessThan(1500L))
                        .body("data.findall.size()", equalTo(6)));
    }

    @DisplayName("Check unknown endpoint - NOT FOUND")
    @Test
    void checkCode404() {
        given(requestSpec)
                .get("/unknown/23")
                .then()
                .spec(responseSpec(404))
                .extract().as(UserDataResModel.class);
    }

    @DisplayName("Check authorization of incorrect user")
    @Test
    void loginNotUserUnsuccessfulTest() {

        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("amongus@killer");
        authData.setPassword("cityslicka");

        LoginErrorModel response = step("Send request.", () ->
                given(requestSpec)
                        .body(authData)
                        .when()
                        .post("/login")
                        .then()
                        .spec(responseSpec(400))
                        .extract().as(LoginErrorModel.class));

        step("Check response..", () -> {
            assertEquals("user not found", response.getError());
        });
    }

    @DisplayName("Check authorization of user incorrect password")
    @Test
    void loginUserUnsuccessfulTest() {

        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        //authData.setPassword("pass");

        LoginErrorModel response = step("Send request.", () ->
                given(requestSpec)
                        .body(authData)
                        .when()
                        .post("/login")
                        .then()
                        .spec(responseSpec(400))
                        .extract().as(LoginErrorModel.class));

        step("Check response.", () -> {
            assertEquals("Missing password", response.getError());
        });

    }


    @DisplayName("Check email of user")
    @Test
    void checkEmailTest() {
        UserDataModel userData = new UserDataModel();
        userData.setEmail("michael.lawson@reqres.in");

        UserDataResModel response = step("Send request.", () ->
                given(requestSpec)
                        .when()
                        .get("/users/7")
                        .then()
                        .spec(responseSpec(200))
                        .extract().as(UserDataResModel.class));

        step("Check response.", () -> {
            assertEquals(userData.getEmail(), response.getData().getEmail());
        });

    }


}


