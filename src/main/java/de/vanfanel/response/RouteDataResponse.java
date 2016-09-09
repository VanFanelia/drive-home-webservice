package de.vanfanel.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class RouteDataResponse {


  public List<String> routes = new ArrayList<>();

  public RouteDataResponse() {
  }

  public RouteDataResponse(List<String> routes) {
    this.routes = routes;
  }

  public List<String> getRoutes() {
    return routes;
  }

  public void setRoutes(List<String> routes) {
    this.routes = routes;
  }

  public void addRoute(String route)
  {
    this.routes.add(route);
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }


}
