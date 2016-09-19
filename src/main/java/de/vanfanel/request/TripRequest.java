package de.vanfanel.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripRequest {

  public List<String> departures = new ArrayList<>();

  public String destination;

  public long departureTime = new Date().getTime();

  public TripRequest() {
  }

  public TripRequest(List<String> departures, String destination, long departureTime) {
    this.departures = departures;
    this.destination = destination;
    this.departureTime = departureTime;
  }

  public List<String> getDepartures() {
    return departures;
  }

  public void setDepartures(List<String> departures) {
    this.departures = departures;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public long getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(long departureTime) {
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
