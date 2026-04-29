package com.personal.springlessons.endpoint;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Locale;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPMessage;

import com.personal.springlessons.exception.SpringLessonsApplicationException;
import com.personal.springlessons.model.dto.platformhistory.GetBookHistoryRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CustomSoapFaultResolverTest {

  @LocalServerPort
  private int port;

  @Autowired
  private CustomSoapFaultResolver resolver;

  private WebServiceTemplate webServiceTemplate;

  @BeforeEach
  void init() throws Exception {
    org.springframework.oxm.jaxb.Jaxb2Marshaller marshaller =
        new org.springframework.oxm.jaxb.Jaxb2Marshaller();
    marshaller.setContextPath("com.personal.springlessons.model.dto.platformhistory");
    marshaller.afterPropertiesSet();
    this.webServiceTemplate = new WebServiceTemplate();
    this.webServiceTemplate.setMarshaller(marshaller);
    this.webServiceTemplate.setUnmarshaller(marshaller);
    this.webServiceTemplate.setDefaultUri("http://localhost:" + this.port + "/ws-soap/platform");
  }

  @Test
  void givenInvalidUUIDException_whenCustomizeFault_thenAddIdAndTimestampToFaultDetail() {
    GetBookHistoryRequest request = new GetBookHistoryRequest();
    request.setBookId("not-a-uuid");

    assertThrows(SoapFaultClientException.class,
        () -> this.webServiceTemplate.marshalSendAndReceive(request));
  }

  @Test
  void givenSpringLessonsApplicationException_whenCustomizeFault_thenAddExceptionAndTimestampToFaultDetail()
      throws Exception {
    SOAPMessage rawMessage = MessageFactory.newInstance().createMessage();
    SaajSoapMessage soapMessage = new SaajSoapMessage(rawMessage);
    SoapBody body = soapMessage.getSoapBody();
    SoapFault fault = body.addServerOrReceiverFault("test", Locale.ENGLISH);

    assertDoesNotThrow(() -> this.resolver.customizeFault(null,
        new SpringLessonsApplicationException("error"), fault));
  }

  @Test
  void givenGenericException_whenCustomizeFault_thenAddExceptionAndTimestampToFaultDetail()
      throws Exception {
    SOAPMessage rawMessage = MessageFactory.newInstance().createMessage();
    SaajSoapMessage soapMessage = new SaajSoapMessage(rawMessage);
    SoapBody body = soapMessage.getSoapBody();
    SoapFault fault = body.addServerOrReceiverFault("test", Locale.ENGLISH);

    assertDoesNotThrow(
        () -> this.resolver.customizeFault(null, new RuntimeException("generic error"), fault));
  }
}
