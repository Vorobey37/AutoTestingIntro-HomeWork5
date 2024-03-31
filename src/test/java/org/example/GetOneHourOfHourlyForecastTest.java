package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.oneHourOfHourlyForecasts.Temperature;
import org.example.oneHourOfHourlyForecasts.WeatherInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GetOneHourOfHourlyForecastTest extends AbstractTest{

    private static final Logger logger
            = LoggerFactory.getLogger(GetOneHourOfHourlyForecastTest.class);
    @Test
    void test401Error() throws URISyntaxException, IOException {
        logger.info("Тест 401 запущен");

        logger.debug("Формирование мока для сервиса GetOneHourOfHourlyForecast");

        stubFor(WireMock.get(urlPathEqualTo("/forecasts/v1/hourly/autocomplete"))
                .withQueryParam("apikey", notMatching("100000"))
                .willReturn(aResponse().withStatus(401).withBody("Error")));

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(getBaseUrl() + "/forecasts/v1/hourly/autocomplete");
        URI uri = new URIBuilder(get.getURI())
                .addParameter("apikey", "200000")
                .build();
        get.setURI(uri);

        logger.debug("http клиент создан");

        HttpResponse response = client.execute(get);

        verify(getRequestedFor(urlPathEqualTo("/forecasts/v1/hourly/autocomplete")));
        Assertions.assertEquals(401, response.getStatusLine().getStatusCode());
    }

    @Test
    void test200WeatherInfoResponseCode() throws IOException, URISyntaxException {
        logger.info("Тест 200 запущен");
        ObjectMapper objectMapper = new ObjectMapper();
        WeatherInfo bodyOk = new WeatherInfo();
        bodyOk.setIconPhrase("OK");

        WeatherInfo bodyError = new WeatherInfo();
        bodyError.setIconPhrase("ERROR");

        stubFor(WireMock.get(urlPathEqualTo("/forecasts/v1/hourly/autocomplete"))
                .withQueryParam("IconPhrase", equalTo("Clear"))
                .willReturn(aResponse().withStatus(200).withBody(objectMapper.writeValueAsString(bodyOk))));

        stubFor(WireMock.get(urlPathEqualTo("/forecasts/v1/hourly/autocomplete"))
                .withQueryParam("IconPhrase", equalTo("error"))
                .willReturn(aResponse().withStatus(200).withBody(objectMapper.writeValueAsString(bodyError))));

        logger.debug("Формирование мока для теста test200WeatherInfoResponseCode завершено");

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(getBaseUrl() + "/forecasts/v1/hourly/autocomplete");
        URI uriOk = new URIBuilder(get.getURI())
                .addParameter("IconPhrase", "Clear")
                .build();
        get.setURI(uriOk);
        HttpResponse responseOk = client.execute(get);

        URI uriError = new URIBuilder(get.getURI())
                .addParameter("IconPhrase", "error")
                .build();
        get.setURI(uriError);
        HttpResponse responseError = client.execute(get);

        verify(2, getRequestedFor(urlPathEqualTo("/forecasts/v1/hourly/autocomplete")));
        Assertions.assertEquals(200, responseOk.getStatusLine().getStatusCode());
        Assertions.assertEquals(200, responseError.getStatusLine().getStatusCode());

        WeatherInfo weatherInfoOk = objectMapper.readValue(responseOk.getEntity().getContent(), WeatherInfo.class);
        WeatherInfo weatherInfoError = objectMapper.readValue(responseError.getEntity().getContent(), WeatherInfo.class);
        Assertions.assertEquals("OK", weatherInfoOk.getIconPhrase());
        Assertions.assertEquals("ERROR", weatherInfoError.getIconPhrase());
    }

    @Test
    void test200TemperatureResponseCode() throws IOException, URISyntaxException {
        logger.info("Тест 200 запущен");
        ObjectMapper objectMapper = new ObjectMapper();
        Temperature bodyOk = new Temperature();
        bodyOk.setValue(25.0);

        Temperature bodyError = new Temperature();
        bodyError.setValue(-100.0);

        stubFor(WireMock.get(urlPathEqualTo("/forecasts/v1/hourly/autocomplete"))
                .withQueryParam("value", equalTo("25"))
                .willReturn(aResponse().withStatus(200).withBody(objectMapper.writeValueAsString(bodyOk))));

        stubFor(WireMock.get(urlPathEqualTo("/forecasts/v1/hourly/autocomplete"))
                .withQueryParam("value", equalTo("-100"))
                .willReturn(aResponse().withStatus(200).withBody(objectMapper.writeValueAsString(bodyError))));

        logger.debug("Формирование мока для теста test200TemperatureResponseCode завершено");

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(getBaseUrl() + "/forecasts/v1/hourly/autocomplete");
        URI uriOk = new URIBuilder(get.getURI())
                .addParameter("value", "25")
                .build();
        get.setURI(uriOk);
        HttpResponse responseOk = client.execute(get);

        URI uriError = new URIBuilder(get.getURI())
                .addParameter("value", "-100")
                .build();
        get.setURI(uriError);
        HttpResponse responseError = client.execute(get);

        verify(2, getRequestedFor(urlPathEqualTo("/forecasts/v1/hourly/autocomplete")));
        Assertions.assertEquals(200, responseOk.getStatusLine().getStatusCode());
        Assertions.assertEquals(200, responseError.getStatusLine().getStatusCode());

        Temperature temperatureOk = objectMapper.readValue(responseOk.getEntity().getContent(), Temperature.class);
        Temperature temperatureError = objectMapper.readValue(responseError.getEntity().getContent(), Temperature.class);
        Assertions.assertEquals(25.0, temperatureOk.getValue());
        Assertions.assertEquals(-100.0, temperatureError.getValue());
    }

}
