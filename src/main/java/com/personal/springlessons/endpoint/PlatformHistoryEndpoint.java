package com.personal.springlessons.endpoint;

import com.personal.springlessons.model.dto.platformhistory.GetBookHistoryRequest;
import com.personal.springlessons.model.dto.platformhistory.GetBookHistoryResponse;
import com.personal.springlessons.service.books.BooksService;
import com.personal.springlessons.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class PlatformHistoryEndpoint {

  private static final Logger log = LoggerFactory.getLogger(PlatformHistoryEndpoint.class);
  private final BooksService bookService;

  public PlatformHistoryEndpoint(BooksService bookService) {
    this.bookService = bookService;
  }

  @PayloadRoot(namespace = Constants.S_XML_NAMESPACE_PLATFORM_HISTORY,
      localPart = "GetBookHistoryRequest")
  @ResponsePayload
  public GetBookHistoryResponse getBookHistory(
      @RequestPayload GetBookHistoryRequest getBookHistoryRequest) {
    log.info("Called SOAP API to retrieve book history");
    GetBookHistoryResponse result = this.bookService.getBookHistory(getBookHistoryRequest);
    log.info("History retrieved !");
    return result;
  }
}
