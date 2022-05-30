package com.miqdigital.scheduling.server.filter;

import static com.miqdigital.scheduling.server.util.Constants.HTTP_HEADER_INSTANCE_HEADER;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * The type Hostname header filter.
 * This adds a header on all response to return the hostname.
 */
@Order(2)
public class HostnameHeaderFilter extends OncePerRequestFilter {

  private static final String HOSTNAME =
      System.getenv("HOSTNAME") == null ? "" : System.getenv("HOSTNAME");
  private static final Logger LOGGER = LoggerFactory.getLogger(HostnameHeaderFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      response.addHeader(HTTP_HEADER_INSTANCE_HEADER, HOSTNAME);
    } catch (NullPointerException e) {
      LOGGER.warn("Exception occurred while adding host header: {}",
          ExceptionUtils.getStackTrace(e));
    } finally {
      filterChain.doFilter(request, response);
    }
  }
}
