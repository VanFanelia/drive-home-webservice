package de.vanfanel.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class NextDeparturesResponse {

  private String station;

  public NextDeparturesResponse() {
  }

  public NextDeparturesResponse(String station) {
    this.station = station;
  }

  public String getStation() {
    return station;
  }

  public void setStation(String station) {
    this.station = station;
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
