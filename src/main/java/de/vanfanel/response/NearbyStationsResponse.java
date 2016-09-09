package de.vanfanel.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class NearbyStationsResponse {

  private List<NearbyStation> stations = new ArrayList<>();

  public NearbyStationsResponse() {
  }

  public List<NearbyStation> getStations() {
    return stations;
  }

  public void setStations(List<NearbyStation> stations) {
    this.stations = stations;
  }

  public boolean addStation(String id, String name)
  {
    return this.stations.add(new NearbyStation(id, name));
  }

  public static NearbyStation station(String id, String name){
    return new NearbyStation(id, name);
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


class NearbyStation {

  private String id;

  private String name;

  public NearbyStation() {
  }

  public NearbyStation(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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