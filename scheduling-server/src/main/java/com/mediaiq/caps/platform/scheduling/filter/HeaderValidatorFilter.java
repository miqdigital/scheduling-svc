package com.mediaiq.caps.platform.scheduling.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mediaiq.caps.platform.trackingtags.Department;
import com.mediaiq.caps.platform.trackingtags.Environment;
import com.mediaiq.caps.platform.trackingtags.Product;
import com.mediaiq.caps.platform.trackingtags.Service;
import com.mediaiq.caps.platform.trackingtags.Team;
import com.mediaiq.caps.platform.trackingtags.TrackingTags;

@Order(1)
public class HeaderValidatorFilter extends OncePerRequestFilter {
  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (!request.getServletPath().contains("schedule-task")) {
      filterChain.doFilter(request, response);
      return;
    }
    try {
      TrackingTags trackingTags = new TrackingTags.TrackingTagsBuilder()
          .setEnvironment(Environment.valueOf(request.getHeader(TrackingTags.ENVIRONMENT)))
          .setDepartment(Department.valueOf(request.getHeader(TrackingTags.DEPARTMENT)))
          .setTeam(Team.valueOf(request.getHeader(TrackingTags.TEAM)))
          .setService(Service.valueOf(request.getHeader(TrackingTags.SERVICE)))
          .setProduct(Product.valueOf(request.getHeader(TrackingTags.PRODUCT)))
          .setOwner(request.getHeader(TrackingTags.OWNER))
          .setFunction(request.getHeader(TrackingTags.FUNCTION)).build();
      log.debug("Tracking tags {} validated {}", trackingTags, request.getRequestURI());
    } catch (IllegalArgumentException | NullPointerException exception) {
      log.warn("Error in tracking tags exception{}",exception);
    } finally {
      filterChain.doFilter(request, response);
    }
  }
}
