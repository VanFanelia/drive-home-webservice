package de.vanfanel.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class NextDeparturesResponse {

  private List<String> departures = new ArrayList<>();

  public NextDeparturesResponse() {
  }

  public NextDeparturesResponse(List<String> departures) {
    this.departures = departures;
  }

  public List<String> getDepartures() {
    return departures;
  }

  public void setDepartures(List<String> departures) {
    this.departures = departures;
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
