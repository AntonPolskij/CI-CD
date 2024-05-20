package org.max.seminar.accu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;


public class SeminarTest extends AbstractTest{
    public static final String LOCATION_V_1_CITIES_AUTOCOMPLETE =  "/location/v1/cities/autocomplete";

    @Test
    void getShouldReturn200WithRestAssured(){
        stubFor(get(urlPathEqualTo(LOCATION_V_1_CITIES_AUTOCOMPLETE)).withQueryParam("s",equalTo("string")).withQueryParam("w",containing("word")).willReturn(aResponse().withStatus(200).withBody("Hello")));

        stubFor(get(urlPathEqualTo(LOCATION_V_1_CITIES_AUTOCOMPLETE + "400")).willReturn(aResponse().withStatus(400)));

        given()
                .when().get(getBaseUrl() + LOCATION_V_1_CITIES_AUTOCOMPLETE)
                .then().statusCode(404);

        given()
                .when().get(getBaseUrl() + LOCATION_V_1_CITIES_AUTOCOMPLETE + "400")
                .then().statusCode(400);

        given()
                .queryParam("q", "Samara")
                .pathParam("yes", "yes")
                .when().get(getBaseUrl() + LOCATION_V_1_CITIES_AUTOCOMPLETE + "400" + "{yes}")
                .then().statusCode(400);

        String body = given().queryParams("s", "string", "w", "word2321").when().get(getBaseUrl() + LOCATION_V_1_CITIES_AUTOCOMPLETE).then().statusCode(200).extract().response().body().asString();

        Assertions.assertEquals("Hello", body);
    }
}
