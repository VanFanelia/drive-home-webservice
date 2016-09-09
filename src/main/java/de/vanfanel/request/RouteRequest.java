package de.vanfanel.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

public class RouteRequest {

  public List<String> departureIds;

  public String destinationId;

  public Date departureTime;

  public RouteRequest() {
  }

  public RouteRequest(List<String> departureIds, String destinationId, Date departureTime) {
    this.departureIds = departureIds;
    this.destinationId = destinationId;
    this.departureTime = departureTime;
  }

  public List<String> getDepartureIds() {
    return departureIds;
  }

  public void setDepartureIds(List<String> departureIds) {
    this.departureIds = departureIds;
  }

  public String getDestinationId() {
    return destinationId;
  }

  public void setDestinationId(String destinationId) {
    this.destinationId = destinationId;
  }

  public Date getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(Date departureTime) {
    this.departureTime = departureTime;
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
