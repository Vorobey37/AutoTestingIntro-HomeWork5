package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.iPAddressSearch.AdministrativeArea;

import org.example.iPAddressSearch.Country;
import org.example.oneHourOfHourlyForecasts.Temperature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class IpAddressSearchTest extends AbstractTest{

    private static final Logger logger
            = LoggerFactory.getLogger(IpAddressSearchTest.class);

    @Test
    void test401Error() throws URISyntaxException, IOException {
        logger.info("Тест 401 запущен");

        logger.debug("Формирование мока для сервиса IpAddressSearch");

        stubFor(WireMock.get(urlPathEqualTo("/locations/v1/cities/autocomplete"))
                .withQueryParam("apikey", notMatching("25"))
                .willReturn(aResponse().withStatus(401).withBody("Error")));

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(getBaseUrl() + "/locations/v1/cities/autocomplete");
        URI uri = new URIBuilder(get.getURI())
                .addParameter("apikey", "35")
                .build();
        get.setURI(uri);

        logger.debug("http клиент создан");

        HttpResponse response = client.execute(get);

        verify(getRequestedFor(urlPathEqualTo("/locations/v1/cities/autocomplete")));
        Assertions.assertEquals(401, response.getStatusLine().getStatusCode());
    }

    @Test
    void test200AdministrativeAreaResponseCode() throws IOException, URISyntaxException {
        logger.info("Тест 200 запущен");
        ObjectMapper objectMapper = new ObjectMapper();

        AdministrativeArea bodyOk = new AdministrativeArea();
        bodyOk.setEnglishName("Narvskiy");

        AdministrativeArea bodyError = new AdministrativeArea();
        bodyError.setEnglishName("Error");

        stubFor(WireMock.get(urlPathEqualTo("/locations/v1/cities/autocomplete"))
                .withQueryParam("EnglishName", equalTo("Narvskiy"))
                .willReturn(aResponse().withStatus(200).withBody(objectMapper.writeValueAsString(bodyOk))));

        //у Вас тут адрес "/forecasts/v1/hourly/autocomplete", но в http клменте на строке 78 Вы определяете адрес "/locations/v1/cities/autocomplete"
        //в таком случае Вам надо делать два HttpGet объекта
        stubFor(WireMock.get(urlPathEqualTo("/forecasts/v1/hourly/autocomplete"))
                .withQueryParam("EnglishName", equalTo("Not Narvskiy"))
                .willReturn(aResponse().withStatus(200).withBody(objectMapper.writeValueAsString(bodyError))));

        logger.debug("Формирование мока для теста test200AdministrativeAreaResponseCode завершено");

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(getBaseUrl() + "/locations/v1/cities/autocomplete");
        URI uriOk = new URIBuilder(get.getURI())
                .addParameter("EnglishName", "Narvskiy")
                .build();
        get.setURI(uriOk);
        HttpResponse responseOk = client.execute(get);

        URI uriError = new URIBuilder(get.getURI())
                .addParameter("EnglishName", "Not Narvskiy")
                .build();
        get.setURI(uriError);
        HttpResponse responseError = client.execute(get);

        verify(2, getRequestedFor(urlPathEqualTo("/locations/v1/cities/autocomplete")));
        Assertions.assertEquals(200, responseOk.getStatusLine().getStatusCode());
        Assertions.assertEquals(200, responseError.getStatusLine().getStatusCode());

        AdministrativeArea administrativeAreaOk = objectMapper.readValue(responseOk.getEntity().getContent(), AdministrativeArea.class);
        AdministrativeArea administrativeAreaError = objectMapper.readValue(responseError.getEntity().getContent(), AdministrativeArea.class);
        Assertions.assertEquals("Narvskiy", administrativeAreaOk.getEnglishName());
        Assertions.assertEquals("Error", administrativeAreaError.getEnglishName());
    }

    @Test
    void test200CountryResponseCode() throws IOException, URISyntaxException {
        logger.info("Тест 200 запущен");
        ObjectMapper objectMapper = new ObjectMapper();
        Country bodyOk = new Country();
        bodyOk.setId("RU");

        Country bodyError = new Country();
        bodyError.setId("Error");

        stubFor(WireMock.get(urlPathEqualTo("/locations/v1/cities/autocomplete"))
                .withQueryParam("value", equalTo("RU"))
                .willReturn(aResponse().withStatus(200).withBody(objectMapper.writeValueAsString(bodyOk))));

        //у Вас тут адрес "/forecasts/v1/hourly/autocomplete", но в http клменте на строке 78 Вы определяете адрес "/locations/v1/cities/autocomplete"
        //в таком случае Вам надо делать два HttpGet объекта
        stubFor(WireMock.get(urlPathEqualTo("/forecasts/v1/hourly/autocomplete"))
                .withQueryParam("value", equalTo("Error"))
                .willReturn(aResponse().withStatus(200).withBody(objectMapper.writeValueAsString(bodyError))));

        logger.debug("Формирование мока для теста test200CountryResponseCode завершено");

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(getBaseUrl() + "/locations/v1/cities/autocomplete");
        URI uriOk = new URIBuilder(get.getURI())
                .addParameter("value", "RU")
                .build();
        get.setURI(uriOk);
        HttpResponse responseOk = client.execute(get);

        URI uriError = new URIBuilder(get.getURI())
                .addParameter("value", "Error")
                .build();
        get.setURI(uriError);
        HttpResponse responseError = client.execute(get);

        verify(2, getRequestedFor(urlPathEqualTo("/locations/v1/cities/autocomplete")));
        Assertions.assertEquals(200, responseOk.getStatusLine().getStatusCode());
        Assertions.assertEquals(200, responseError.getStatusLine().getStatusCode());

        Country countryOk = objectMapper.readValue(responseOk.getEntity().getContent(), Country.class);
        Country countryError = objectMapper.readValue(responseError.getEntity().getContent(), Country.class);
        Assertions.assertEquals("RU", countryOk.getId());
        Assertions.assertEquals("Error", countryError.getId());
    }
}
