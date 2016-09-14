package de.vanfanel.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class NextDeparturesRequest {

  private String station;

  private long departureTime = new Date().getTime();

  private int maxDepartures = 10;

  public NextDeparturesRequest() {
  }

  public NextDeparturesRequest(String station, long departureTime, int maxDepartures) {
    this.station = station;
    this.departureTime = departureTime;
    this.maxDepartures = maxDepartures;
  }

  public String getStation() {
    return station;
  }

  public void setStation(String station) {
    this.station = station;
  }

  public long getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(long departureTime) {
    this.departureTime = departureTime;
  }

  public int getMaxDepartures() {
    return maxDepartures;
  }

  public void setMaxDepartures(int maxDepartures) {
    this.maxDepartures = maxDepartures;
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
