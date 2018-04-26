package cn.jyl.fallback;

import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author jiyanlong
 *
 * 用户中心断路器
 *
 */

@Component
public class ServiceFallbackProvider implements ZuulFallbackProvider {
  @Override
  public String getRoute() {
    return "online";
  }

  @Override
  public ClientHttpResponse fallbackResponse() {
    return new ClientHttpResponse() {
      @Override
      public HttpStatus getStatusCode() throws IOException {
        return HttpStatus.INTERNAL_SERVER_ERROR;
      }

      @Override
      public int getRawStatusCode() throws IOException {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
      }

      @Override
      public String getStatusText() throws IOException {
        return HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
      }

      @Override
      public void close() {
      }

      @Override
      public InputStream getBody() throws IOException {
        return new ByteArrayInputStream("fallback".getBytes());
      }

      @Override
      public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
      }

    };
  }
}