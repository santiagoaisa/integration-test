package com.mycompany.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.*;
import static org.hamcrest.Matchers.*;


public class PetTypesDefinitions {

    private EnvironmentVariables enviromentVariables;
    private String BASE_PATH;

    @Before
    public void setup(){
        enviromentVariables= SystemEnvironmentVariables.createEnvironmentVariables();
        BASE_PATH = EnvironmentSpecificConfiguration.from(enviromentVariables).getProperty("base.uri");
        RestAssured.baseURI = BASE_PATH;
    }


    @Given("El cliente configura la URI base")
    public void el_cliente_configura_la_uri_base() {
        given().baseUri(BASE_PATH);
    }

    @When("El cliente realiza una consulta a {string}")
    public void el_cliente_realiza_una_consulta_a(String path) {
        when().get(path).andReturn();
    }



    @Then("El servidor debe responder con un status {int}")
    public void el_servidor_debe_responder_con_un_status(Integer status) {
        then().statusCode(status);
    }

    @Then("El cuerpo de la respuesta debe ser una lista de tipos de mascotas")
    public void el_cuerpo_de_la_respuesta_debe_ser_una_lista_de_tipos_de_mascotas() {
        //then().body("$",hasSize(6));
        //then().body("size()",is(6));
        then().body("$",not(empty()));
        then().body("[0].id",notNullValue());
        then().body("[0].name",notNullValue());
    }

    @Then("El cuerpo de la respuesta contiene la propiedad id con el valor {int}")
    public void el_cuerpo_de_la_respuesta_contiene_la_propiedad_id_con_el_valor(Integer id) {
        then().body("id", is(id));
    }
    @Then("El cuerpo de la respuesta contiene la propiedad name con el valor {string}")
    public void el_cuerpo_de_la_respuesta_contiene_la_propiedad_name_con_el_valor(String name) {
        then().body("name", is(name));
    }


    @Given("El cliente configura el recurso {string} con los datos de un nuevo tipo de mascota")
    public void el_cliente_configura_el_recurso_con_los_datos_de_un_nuevo_tipo_de_mascota(String path, String docString) {
        given().contentType(ContentType.JSON)
                .basePath(path)
                .body(docString);

        Serenity.setSessionVariable("petType").to(docString);
    }
    @When("El cliente registra el nuevo tipo de mascota")
    public void el_cliente_registra_el_nuevo_tipo_de_mascota() {
        when().post().andReturn();
    }

    @Then("El cuerpo de la respuesta debe contener los detalle del nuevo tipo de mascota registrado")
    public void el_cuerpo_de_la_respuesta_debe_contener_los_detalle_del_nuevo_tipo_de_mascota_registrado()  throws JsonProcessingException {
        String docString = Serenity.sessionVariableCalled("petType");
        Map<String, Object> jsonMap = new ObjectMapper().readValue(docString, new TypeReference<>() {
        });
        then().body(notNullValue());
        then().body("id", notNullValue());
        then().body("id", instanceOf(Integer.class));
        then().body("name", notNullValue());
        then().body("name", is(jsonMap.get("name")));
    }


    @Given("El cliente configura el recurso {string} con id {int} usando los datos")
    public void el_cliente_configura_el_recurso_con_id_usando_los_datos(String path, Integer id, String docString) {
        given().contentType(ContentType.JSON)
                .basePath(path)
                .pathParam("id", id)
                .body(docString);
    }
    @When("El cliente actualiza el nuevo tipo de mascota")
    public void el_cliente_actualiza_el_nuevo_tipo_de_mascota() {
        when().put().andReturn();
    }

    @Then("El cuerpo de la respuesta debe estar vacio")
    public void el_cuerpo_de_la_respuesta_debe_estar_vacio() {
        then().body(emptyOrNullString());
    }

    @Given("El cliente configura el recurso {string} con id {int}")
    public void el_cliente_configura_el_recurso_con_id(String path, Integer id) {
        given().contentType(ContentType.JSON)
                .basePath(path)
                .pathParam("id", id);
    }
    @When("El cliente elimina el tipo de mascota")
    public void el_cliente_elimina_el_tipo_de_mascota() {
        // Write code here that turns the phrase above into concrete actions
        when().delete().andReturn();
    }


}
