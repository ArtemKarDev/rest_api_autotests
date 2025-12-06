package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;

public class BaseSpec {

    //Header header = new Header("x-api-key", "reqres_2c1b8f0f932a4959a9abedf74535de66");

    public static RequestSpecification requestSpec = with()
            .filter(withCustomTemplates())
            .header("x-api-key", "reqres_2c1b8f0f932a4959a9abedf74535de66")
            .contentType(JSON)
            .log().uri()
            .log().body()
            .log().headers();


    public static ResponseSpecification responseSpec(int expStatusCode) {
        return  new ResponseSpecBuilder()
                .expectStatusCode(expStatusCode)
                .log(STATUS)
                .log(ALL)
                .build();
    }
}
