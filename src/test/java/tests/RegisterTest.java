package tests;

import models.RegistrationBodyModel;
import models.RegistrationResponseError400Model;
import models.RegistrationResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

public class RegisterTest extends BaseTest {

    @Test
    @Tag("Register")
    @DisplayName("Register User successfully")
    public void registerUserSuccessfully() {
        RegistrationBodyModel regData = new RegistrationBodyModel();
        regData.setEmail("eve.holt@reqres.in");
        regData.setPassword("cityslicka");

        RegistrationResponseModel response = step("Sent request to registration.", () ->
                    given(requestSpec)
                        .body(regData)
                    .when()
                        .post("/register")
                    .then()
                        .spec(responseSpec(200))
                        .extract().as(RegistrationResponseModel.class));

        step("Check response.", () -> {
            assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
            assertEquals("4", response.getId());
        });

    }

    @Test
    @Tag("Register")
    @DisplayName("Register User Unsuccessfully")
    public void registerUserUnsuccessfully() {
        RegistrationBodyModel regData = new RegistrationBodyModel();
        regData.setEmail("paradigma@reqres.in");
        //regData.setPassword("paradigma");

        RegistrationResponseError400Model response = step("Sent request to registration.", () ->
                given(requestSpec)
                        .body(regData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(responseSpec(400))
                        .extract().as(RegistrationResponseError400Model.class));

        step("Check response.", () -> {
            assertEquals("Missing password", response.getError());
        });

    }

}
