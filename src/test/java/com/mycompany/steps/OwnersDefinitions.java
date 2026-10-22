package com.mycompany.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.util.CsvUtils;
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

public class OwnersDefinitions {

    private EnvironmentVariables enviromentVariables;
    private String BASE_PATH;
    private String filePath = "src/test/resources/owners-data.csv";

    @Before
    public void setup(){
        enviromentVariables= SystemEnvironmentVariables.createEnvironmentVariables();
        BASE_PATH = EnvironmentSpecificConfiguration.from(enviromentVariables).getProperty("environments.default.base.uri");
        RestAssured.baseURI = BASE_PATH;
    }

    @Then("El cuerpo de la respuesta debe ser una lista propietarios valida")
    public void el_cuerpo_de_la_respuesta_debe_ser_una_lista_propietarios_valida() {
        then().body(not(empty()));
        then().body("$", instanceOf(List.class));
    }

    @Then("El cuerpo de la respuesta contiene con las siguientes propiedades")
    public void el_cuerpo_de_la_respuesta_contiene_con_las_siguientes_propiedades(io.cucumber.datatable.DataTable dataTable) {
        List<List<String>> rows=dataTable.asLists(String.class);
        rows.stream().flatMap(List::stream).forEach(campo->System.out.println("campo1 "+campo) );


    }

    @Then("El cuerpo de la respuesta contiene con las siguientes propiedades2")
    public void el_cuerpo_de_la_respuesta_contiene_con_las_siguientes_propiedades2(io.cucumber.datatable.DataTable dataTable) {
        List<List<String>> rows=dataTable.asLists(String.class);
        rows.stream().flatMap(List::stream).forEach(campo->System.out.println("campo2 "+campo) );
    }

    @Then("El cuerpo de la respuesta contiene con las siguientes propiedades y valores")
    public void el_cuerpo_de_la_respuesta_contiene_con_las_siguientes_propiedades_y_valores(io.cucumber.datatable.DataTable dataTable) {
        Map<String,String> data=dataTable.asMap(String.class,String.class);
        data.forEach((campo,valor) -> {
            then().body(campo,is(parseValue(valor)));
        });


    }


    @Then("El cliente comprueba el valor del campo {string} es igual a {string}")
    public void el_cliente_comprueba_el_valor_del_campo_es_igual_a(String campo, String valor) {
        then().body(campo,is(valor));
    }
    @Then("El cliente comprueba el valor del campo {string} es igual a {int}")
    public void el_cliente_comprueba_el_valor_del_campo_es_igual_a(String campo, Integer valor) {
        then().body(campo,is(valor));
    }


    @Given("El cliente configura el recurso {string} con los datos de un nuevo propietario")
    public void el_cliente_configura_el_recurso_con_los_datos_de_un_nuevo_propietario(String path, String docString) {
        given().contentType(ContentType.JSON)
                .basePath(path)
                .body(docString);

    }
    @When("El cliente registra el nuevo propietario")
    public void el_cliente_registra_el_nuevo_propietario() {
        when().post().andReturn();
    }



    @Given("El cliente configura el recurso {string} y omite el campo requerido {string} en el siguiente JSON:")
    public void el_cliente_configura_el_recurso_y_omite_el_campo_requerido_en_el_siguiente_json(String path, String campo, String docString) throws JsonProcessingException {
        Map<String, Object> jsonMap = new ObjectMapper().readValue(docString, new TypeReference<>() {
        });
        jsonMap.remove(campo);

        given().contentType(ContentType.JSON)
                .basePath(path)
                .body(jsonMap);

    }

    @Given("El cliente configura el recurso {string} y proporciona los campos {string} {string} {string} {string} {string}")
    public void el_cliente_configura_el_recurso_y_proporciona_los_campos(String path,String firstName, String lastName, String address, String city, String telephone) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("firstName", firstName);
        jsonMap.put("lastName", lastName);
        jsonMap.put("address", address);
        jsonMap.put("city", city);
        jsonMap.put("telephone", telephone);

        given().contentType(ContentType.JSON)
                .basePath(path)
                .body(jsonMap);

    }

    private Object parseValue(String valor) {
        if (valor.startsWith("$str{") && valor.endsWith("}")) {
            return valor.substring(5, valor.length() - 1);
        } else if (valor.matches("-?\\d+")) {
            return Integer.parseInt(valor);
        } else if (valor.matches("-?\\d+\\.\\d+")) {
            return Double.parseDouble(valor);
        } else {
            return valor;
        }
    }

    @Given("El cliente configura el recurso {string} y omite el campo requerido {string} con valor null en el siguiente JSON:")
    public void el_cliente_configura_el_recurso_y_omite_el_campo_requerido_con_valor_null_en_el_siguiente_json(String path, String campo, String docString) throws JsonProcessingException {
        Map<String, Object> jsonMap = new ObjectMapper().readValue(docString, new TypeReference<>() {
        });

        jsonMap.put(campo, null);

        given().contentType(ContentType.JSON)
                .basePath(path)
                .body(jsonMap);
    }


    @Given("El cliente configura el recurso {string} y tiene los datos de un nuevo propietaro identificado con id {int}")
    public void el_cliente_configura_el_recurso_y_tiene_los_datos_de_un_nuevo_propietaro_identificado_con_id(String path, Integer id, io.cucumber.datatable.DataTable dataTable) throws IOException {
        Optional<CSVRecord> recordOpt = CsvUtils.findRowByProperty(filePath, "id", String.valueOf(id));
        List<List<String>> rows = dataTable.asLists(String.class);
        Map<String, Object> jsonMap = new HashMap<>();
        rows.forEach(row -> {
            recordOpt.ifPresent(record -> {
                String campo = row.get(0);
                jsonMap.put(campo, recordOpt.get().get(campo));
            });
        });

        given().contentType(ContentType.JSON)
                .basePath(path)
                .body(jsonMap);

    }


}
