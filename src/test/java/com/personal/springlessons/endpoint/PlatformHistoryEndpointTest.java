package com.personal.springlessons.endpoint;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import com.personal.springlessons.model.dto.platformhistory.GetBookHistoryRequest;
import com.personal.springlessons.model.dto.platformhistory.GetBookHistoryResponse;
import com.personal.springlessons.model.dto.platformhistory.ResultType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PlatformHistoryEndpointTest {

  @LocalServerPort
  private int port;

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
  void givenValidBookId_whenGetBookHistory_thenReturnHistory() {
    GetBookHistoryRequest request = new GetBookHistoryRequest();
    request.setBookId(UUID.randomUUID().toString());

    GetBookHistoryResponse response =
        (GetBookHistoryResponse) this.webServiceTemplate.marshalSendAndReceive(request);

    assertNotNull(response);
    assertNotNull(response.getOutcomeDTO());
    assertTrue(response.getRevisionsDTO().isEmpty());
    assertTrue(response.getOutcomeDTO().getResult() == ResultType.KO);
  }

  @Test
  void givenInvalidBookId_whenGetBookHistory_thenThrowInvalidUUIDException() {
    GetBookHistoryRequest request = new GetBookHistoryRequest();
    request.setBookId("not-a-uuid");

    assertThrows(SoapFaultClientException.class,
        () -> this.webServiceTemplate.marshalSendAndReceive(request));
  }
}
