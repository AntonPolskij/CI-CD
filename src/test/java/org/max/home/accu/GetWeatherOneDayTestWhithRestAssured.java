package org.max.home.accu;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.max.home.accu.weather.DailyForecast;
import org.max.home.accu.weather.Headline;
import org.max.home.accu.weather.Weather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

public class GetWeatherOneDayTestWhithRestAssured extends AbstractTest{

    private static final Logger logger
            = LoggerFactory.getLogger(GetWeatherOneDayTestWhithRestAssured.class);
    @Test
    void get_shouldReturn200() throws JsonProcessingException {
        logger.info("200 rc test running");
        ObjectMapper mapper = new ObjectMapper();
        Weather weather = new Weather();
        Headline headline = new Headline();
        headline.setCategory("Category");
        headline.setText("Text");
        DailyForecast dailyForecast = new DailyForecast();
        List<DailyForecast> dailyForecastList = new ArrayList<>();
        dailyForecastList.add(dailyForecast);
        weather.setHeadline(headline);
        weather.setDailyForecasts(dailyForecastList);


        logger.debug("STUB building");

        stubFor(get(urlPathEqualTo("/forecasts/v1/daily/1day/1")).withQueryParam("apikey", equalTo(getApikey())).willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(weather))));

        String responseAsString = given()
                .queryParams("apikey", getApikey())
                .pathParam("q", 1)
                .when().get(getBaseUrl()+"/forecasts/v1/daily/1day/{q}")
                .then()
                .statusCode(200)
                .extract().response().body().asString();
        Weather response = mapper.readValue(responseAsString, Weather.class);

        Assertions.assertEquals("Category", response.getHeadline().getCategory());
        Assertions.assertEquals("Text",response.getHeadline().getText());
        Assertions.assertEquals(1,response.getDailyForecasts().size());
    }
}
