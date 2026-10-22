package com.mycompany.util;
import jakarta.xml.soap.*;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;


public class SoapRequestUtil {


    public enum Operation {
        ADD("Add"),
        SUBTRACT("Subtract"),
        MULTIPLY("Multiply"),
        DIVIDE("Divide");


        private final String value;


        Operation(String value) {
            this.value = value;
        }


        public String getValue() {
            return value;
        }
    }


    public static String createSoapRequest(Operation operation, Map<String, Integer> parameters) throws Exception {
        // Create SOAP Message
        var messageFactory = MessageFactory.newInstance();
        var soapMessage = messageFactory.createMessage();

        // Create SOAP Part
        var soapPart = soapMessage.getSOAPPart();
        var envelope = soapPart.getEnvelope();

        // Add namespaces
        envelope.removeNamespaceDeclaration(envelope.getPrefix());
        envelope.setPrefix("soapenv");
        envelope.addNamespaceDeclaration("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
        envelope.addNamespaceDeclaration("tem", "http://tempuri.org/");

        // Add SOAP Header
        envelope.getHeader().setPrefix("soapenv");

        // Create SOAP Body
        var soapBody = envelope.getBody();
        soapBody.setPrefix("soapenv");
        createOperationElement(soapBody, operation, parameters);

        // Save message
        soapMessage.saveChanges();

        // Convert SOAP message to String
        return soapMessageToString(soapMessage);
    }


    private static SOAPElement createOperationElement(SOAPBody soapBody, Operation operation, Map<String, Integer> parameters) throws Exception {
        var operationElement = soapBody.addChildElement(operation.getValue(), "tem");
        for (Map.Entry<String, Integer> parameter : parameters.entrySet()) {
            var paramElement = operationElement.addChildElement(parameter.getKey(), "tem");
            paramElement.addTextNode(String.valueOf(parameter.getValue()));
        }
        return operationElement;
    }

    private static String soapMessageToString(SOAPMessage soapMessage) throws IOException, SOAPException {
        try (var outputStream = new ByteArrayOutputStream()) {
            soapMessage.writeTo(outputStream);
            return outputStream.toString();
        }
    }
}