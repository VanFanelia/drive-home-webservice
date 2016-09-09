package de.vanfanel.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class NearbyStationRequest {
  private int lat;

  private int lng;

  private int distance = 0;

  private int maxResult = 20;

  public NearbyStationRequest() {
  }

  public NearbyStationRequest(int lat, int lng, int distance, int maxResult) {
    this.lat = lat;
    this.lng = lng;
    this.distance = distance;
    this.maxResult = maxResult;
  }

  public int getLat() {
    return lat;
  }

  public void setLat(int lat) {
    this.lat = lat;
  }

  public int getLng() {
    return lng;
  }

  public void setLng(int lng) {
    this.lng = lng;
  }

  public int getDistance() {
    return distance;
  }

  public void setDistance(int distance) {
    this.distance = distance;
  }

  public int getMaxResult() {
    return maxResult;
  }

  public void setMaxResult(int maxResult) {
    this.maxResult = maxResult;
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
