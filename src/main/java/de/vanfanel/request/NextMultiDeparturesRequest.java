package de.vanfanel.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NextMultiDeparturesRequest {

  private List<String> stations = new ArrayList<>();

  private long departureTime = new Date().getTime();

  private int maxResults = 10;

  public NextMultiDeparturesRequest() {
  }

  public NextMultiDeparturesRequest(List<String> stations, long departureTime, int maxResults) {
    this.stations = stations;
    this.departureTime = departureTime;
    this.maxResults = maxResults;
  }

  public List<String> getStations() {
    return stations;
  }

  public void setStations(List<String> stations) {
    this.stations = stations;
  }

  public long getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(long departureTime) {
    this.departureTime = departureTime;
  }

  public int getMaxResults() {
    return maxResults;
  }

  public void setMaxResults(int maxResults) {
    this.maxResults = maxResults;
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
