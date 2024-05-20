package org.max.home.accu;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

public class AbstractTest {

    private static Properties prop = new Properties();
    private static WireMockServer wireMockServer = new WireMockServer();
    private static final int port = 8080;
    private static InputStream configFile;
    private static String baseUrl;

    private static String apikey;

    private static final Logger logger
            = LoggerFactory.getLogger(AbstractTest.class);

    @BeforeAll
    static void startServer() throws IOException {
        configFile = new FileInputStream("src/main/resources/accuweather.properties");
        prop.load(configFile);
        apikey = prop.getProperty("apikey");
        baseUrl = "http://localhost:" + port;
        wireMockServer.start();
        configureFor("localhost", port);
        logger.info("Start WireMockServer on port {}",port);
    }

    @AfterAll
    static void stopServer() {
        wireMockServer.stop();
        logger.info("Stop WireMockServer");
    }

    public static String getApikey() {
        return apikey;
    }

    //Вспомогательный метод - конвертор body to string
    public String convertResponseToString(HttpResponse response) throws IOException {
        logger.debug("convertResponseToString method call");
        try(InputStream responseStream = response.getEntity().getContent();
            Scanner scanner = new Scanner(responseStream, "UTF-8");) {
            String responseString = scanner.useDelimiter("\\Z").next();
            return responseString;
        }

    }

    public static String getBaseUrl() {
        return baseUrl;
    }
}
