# language: en
Feature: Servicio SOAP de Calculadora

  @Sumar
  Scenario: Calcular la suma de dos números
    Given El cliente configura el recurso "/calculator.asmx" para que los números 3 y 4 están listos para ser sumados
    When envío una solicitud SOAP
    Then la respuesta debe contener el resultado 7