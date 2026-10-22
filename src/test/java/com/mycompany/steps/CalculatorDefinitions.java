package com.mycompany.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.util.CsvUtils;
import com.mycompany.util.SoapRequestUtil;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.serenitybdd.rest.SerenityRest.*;
import static org.hamcrest.Matchers.*;

public class CalculatorDefinitions {

    private EnvironmentVariables environmentVariables;
    private String BASE_PATH;

    @Before
    public void setUp() {
        environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
        BASE_PATH = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getOptionalProperty("environments.soap.base.uri").orElse("https://reqres.in/api");
        RestAssured.baseURI = BASE_PATH;
    }



    @Given("El cliente configura el recurso {string} para que los números {int} y {int} están listos para ser sumados")
    public void el_cliente_configura_el_recurso_para_que_los_números_y_están_listos_para_ser_sumados(String path, Integer intA, Integer intB) throws Exception {
        String requestBody = SoapRequestUtil.createSoapRequest(SoapRequestUtil.Operation.ADD, Map.of("intA", intA, "intB", intB));
        given()
                .header("Content-Type", "text/xml; charset=utf-8")
                .baseUri(BASE_PATH)
                .basePath(path)
                .body(requestBody);
    }
    @When("envío una solicitud SOAP")
    public void envío_una_solicitud_soap() {
        when().post().andReturn();
    }

    @Then("la respuesta debe contener el resultado {int}")
    public void la_respuesta_debe_contener_el_resultado(Integer resultado) {
        then().statusCode(200)
                .and().contentType(ContentType.XML)
                .and().body("Envelope.Body.AddResponse.AddResult.text()", is(String.valueOf(resultado)));
    }

}
