Feature: Gestionar tipos de mascotas
  Scenario: Listar todos los tipos de mascotas

    #Precondiciones para lograr un objetivo de prueba
    #Establece el contexto inicial o el estado previo antes de realizar cualquier acción.
    #¿Qué debes hacer en Java?: Configurar el entorno, preparar datos, autenticarte o establecer URLs base.
    Given El cliente configura la URI base

    #La Acción Principial
    #Describe la acción central o el evento que desencadena la prueba.
    #Ejecutar la operación real que quieres probar (hacer clic en un botón, enviar un formulario o hacer una petición HTTP GET/POST).
    When El cliente realiza una consulta a "/pettypes"


    #La Verificación / Resultado
    #Comprueba la consecuencia o el resultado esperado tras ejecutar la acción del When.
    #Escribir las aserciones (asserts) para confirmar que todo salio bien
    #validar códigos de respuesta HTTP, mensajes de error o datos devueltos.
    Then El servidor debe responder con un status 200
    And El cuerpo de la respuesta debe ser una lista de tipos de mascotas


  Scenario: Listar un tipo de mascota por id 4
    Given El cliente configura la URI base
    When El cliente realiza una consulta a "/pettypes/4"
    Then El servidor debe responder con un status 200

  Scenario Outline: Listar un tipo de mascota por id 1 and 5
    Given El cliente configura la URI base
    When El cliente realiza una consulta a <path>
    Then El servidor debe responder con un status <statusCode>
    And El cuerpo de la respuesta contiene la propiedad id con el valor <id>
    And El cuerpo de la respuesta contiene la propiedad name con el valor <name>
  Examples:
    |path|statusCode|id|name|
    |"/pettypes/4"|200|4|"snake"|
    |"/pettypes/5"|200|5|"bird"|


  Scenario: Registrar un nuevo tipo de mascota
    Given El cliente configura el recurso "/pettypes" con los datos de un nuevo tipo de mascota
    """
    {
      "name":"rabbit"
    }
    """
    When El cliente registra el nuevo tipo de mascota
    Then El servidor debe responder con un status 201
    And El cuerpo de la respuesta debe contener los detalle del nuevo tipo de mascota registrado

  Scenario: Actualizar un tipo de mascota
    Given El cliente configura el recurso "/pettypes/{id}" con id 6 usando los datos
    """
    {
      "id":0,
      "name":"rabbit"
    }
    """
    When El cliente actualiza el nuevo tipo de mascota
    Then El servidor debe responder con un status 204
    And El cuerpo de la respuesta debe estar vacio

  #Scenario: Eliminar un tipo de mascota
  #  Given El cliente configura el recurso "/pettypes/{id}" con id 7
  #  When El cliente elimina el tipo de mascota
  #  Then El servidor debe responder con un status 204
  #  And El cuerpo de la respuesta debe estar vacio