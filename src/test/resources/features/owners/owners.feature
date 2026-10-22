Feature: Gestionar propietarios

  @listarPropietarios
  Scenario: Listar todos los propietarios
    Given El cliente configura la URI base
    When El cliente realiza una consulta a "/owners"
    Then El servidor debe responder con un status 200
    And El cuerpo de la respuesta debe ser una lista propietarios valida


  @listarPropietarioPorId
  Scenario: Validar detalles del propietario y sus mascotas
    Given El cliente configura la URI base
    When El cliente realiza una consulta a "/owners/6"
    Then El servidor debe responder con un status 200
    And El cuerpo de la respuesta contiene con las siguientes propiedades
      | firstName |
      | lastName  |
      | address   |
      | city      |
      | telephone |
    And El cuerpo de la respuesta contiene con las siguientes propiedades2
      | firstName| lastName|address|city|telephone|
    And El cuerpo de la respuesta contiene con las siguientes propiedades y valores
      | firstName | Jean             |
      | lastName  | Coleman          |
      | address   | 105 N. Lake St.  |
      | city      | Monona |
      | telephone | $str{6085552654} |
      | id        | 6|
    And El cliente comprueba el valor del campo "firstName" es igual a "Jean"
    And El cliente comprueba el valor del campo "lastName" es igual a "Coleman"
    And El cliente comprueba el valor del campo "address" es igual a "105 N. Lake St."
    And El cliente comprueba el valor del campo "city" es igual a "Monona"
    And El cliente comprueba el valor del campo "telephone" es igual a "6085552654"
    And El cliente comprueba el valor del campo "id" es igual a 6


  @registrarPropietario
  Scenario: Registrar propietario exitosamente
    Given El cliente configura el recurso "/owners" con los datos de un nuevo propietario
       """
          {
              "firstName": "Dany",
              "lastName": "Cenas",
              "address": "Av. Javier Prado",
              "city": "Lima",
              "telephone": "1234567890"
          }
       """
    When El cliente registra el nuevo propietario
    Then El servidor debe responder con un status 201
    And El cliente comprueba el valor del campo "firstName" es igual a "Dany"
    And El cliente comprueba el valor del campo "lastName" es igual a "Cenas"
    And El cliente comprueba el valor del campo "address" es igual a "Av. Javier Prado"
    And El cliente comprueba el valor del campo "city" es igual a "Lima"
    And El cliente comprueba el valor del campo "telephone" es igual a "1234567890"

  @registrarPropietarioConCamposRequeridos
  Scenario Outline: Registrar propietario con campos requeridos
    Given El cliente configura el recurso "/owners" y omite el campo requerido "<campo>" en el siguiente JSON:
       """
          {
              "firstName": "Dany",
              "lastName": "Cenas",
              "address": "Av. Javier Prado",
              "city": "Lima",
              "telephone": "1234567890"
          }
       """
    When El cliente registra el nuevo propietario
    Then El servidor debe responder con un status 400
    Examples:
      | campo     |
      | firstName |
      | lastName  |
      | address   |
      | city      |
      | telephone |

  @registrarPropietarioConCamposVacios
  Scenario Outline: Registrar propietario con campos vacios
    Given El cliente configura el recurso "/owners" y proporciona los campos <firstName> <lastName> <address> <city> <telephone>
    When El cliente registra el nuevo propietario
    Then El servidor debe responder con un status <status>
    Examples:
      | firstName | lastName | address            | city   | telephone   |status|
      | ""        | "Cenas"  | "Av. Javier Prado" | "Lima" | "1234567890" |400  |
      | "Dany"    | ""       | "Av. Javier Prado" | "Lima" | "1234567890" |400  |
      | "Dany"    | "Cenas"  | ""                 | "Lima" | "1234567890" |400  |
      | "Dany"    | "Cenas"  | "Av. Javier Prado" | ""     | "1234567890" |400  |
      | "Dany"    | "Cenas"  | "Av. Javier Prado" | "Lima" | ""          |400  |
      | "Dany"    | "Cenas"  | "Av. Javier Prado" | "Lima" | "123"          |500  |


  @registrarPropietarioConCamposNulos
  Scenario Outline: Registrar propietario con campos nulos
    Given El cliente configura el recurso "/owners" y omite el campo requerido "<campo>" con valor null en el siguiente JSON:
       """
           {
              "firstName": "Dany",
              "lastName": "Cenas",
              "address": "Av. Javier Prado",
              "city": "Lima",
              "telephone": "1234567890"
          }
       """
    When El cliente registra el nuevo propietario
    Then El servidor debe responder con un status 400
    Examples:
      | campo     |
      | firstName |
      | lastName  |
      | address   |
      | city      |
      | telephone |


  @registrarPropietarioConCSV
  Scenario Outline: Registrar propietario exitosamente con archivo CSV
    Given El cliente configura el recurso "/owners" y tiene los datos de un nuevo propietaro identificado con id <id>
      | firstName |
      | lastName  |
      | address   |
      | city      |
      | telephone |
    When El cliente registra el nuevo propietario
    Then El servidor debe responder con un status 201
    Examples:
     |id|
     |1 |
     |3 |
     |5 |
