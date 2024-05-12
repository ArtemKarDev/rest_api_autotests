package specs;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;

public class LoginSpec {
     public static RequestSpecification loginRequestSpec = with()
             .filter(new AllureRestAssured())
             .log().uri()
             .log().body()
             .log().headers()
             .contentType(JSON);

     public static ResponseSpecification loginResponseSpec = new ResponseSpecBuilder()
             .expectStatusCode(200)
             .log(STATUS)
             .log(BODY)
             .build();

     public static ResponseSpecification loginResponse400Spec = new ResponseSpecBuilder()
             .expectStatusCode(400)
             .log(STATUS)
             .log(BODY)
             .build();

}
