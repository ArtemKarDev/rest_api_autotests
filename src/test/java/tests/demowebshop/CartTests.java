package tests.demowebshop;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class CartTests extends TestBase {
    @Test
    void addToCartAsAuthTest(){
//move to api class
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

//get actual cart size

        String data = "product_attribute_72_5_18=53"+
                "&product_attribute_72_6_19=54"+
                "&product_attribute_72_3_20=57"+
                "&addtocart_72.EnteredQuantity=1";

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie(authCookieKey, authCookieValue)
                .body(data)
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your \\u003ca href=\\\"/cart\\\"\\u003eshopping cart\\u003c/a\\u003e"));

        // todo check cart size
    }

    @Test
    void addToCartUnAuthTest(){

        String data = "product_attribute_72_5_18=53"+
                "&product_attribute_72_6_19=54"+
                "&product_attribute_72_3_20=57"+
                "&addtocart_72.EnteredQuantity=1";

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body(data)
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your \\u003ca href=\\\"/cart\\\"\\u003eshopping cart\\u003c/a\\u003e"))
                .body("updatetopcartsectionhtml", is("(2)"));
    }

}
