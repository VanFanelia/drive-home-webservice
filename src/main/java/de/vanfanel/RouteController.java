package de.vanfanel;


import de.schildbach.pte.AbstractEfaProvider;
import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.VrrProvider;
import de.schildbach.pte.dto.*;
import de.vanfanel.exceptions.HTTPInternalServerErrorException;
import de.vanfanel.exceptions.HTTPNotFoundException;
import de.vanfanel.request.NearbyStationRequest;
import de.vanfanel.request.NextDeparturesRequest;
import de.vanfanel.request.RouteRequest;
import de.vanfanel.request.TripRequest;
import de.vanfanel.response.NearbyStationsResponse;
import de.vanfanel.response.RouteDataResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

@Controller
@RequestMapping("/route")
public class RouteController {

  public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("HH:mm");
  public static EnumSet<LocationType> DEFAULT_LOCATION_SET = EnumSet.of(LocationType.STATION);
  public static EnumSet<Product> DEFAULT_PRODUCT_SET = EnumSet.of(Product.BUS, Product.TRAM,
      Product.REGIONAL_TRAIN, Product.SUBURBAN_TRAIN, Product.CABLECAR, Product.ON_DEMAND);


  @RequestMapping(value = "departures", method = RequestMethod.GET)
  public @ResponseBody List<StationDepartures> getNextDepartures(NextDeparturesRequest request) throws Exception {

    VrrProvider vrrProvider = new VrrProvider();
    String stationId = convertStationNameToId(request.getStation());

    QueryDeparturesResult results;
    try {
       results = vrrProvider.queryDepartures(stationId, new Date(request.getDepartureTime()),
          request.getMaxDepartures(), true);
    } catch (IOException e) {
      e.printStackTrace();
      throw new HTTPInternalServerErrorException();
    }

    if(results.stationDepartures.size() == 0) {
      System.out.println("no result found");
      throw new HTTPNotFoundException();
    }

    return results.stationDepartures;
  }

  @RequestMapping(value = "locations", method = RequestMethod.GET)
  public @ResponseBody List<Location> getLocations(@RequestParam String searchFor) throws Exception {
    List<Location> result;
    try {
      result = getLocationSuggestions(searchFor);
    } catch (IOException e) {
      e.printStackTrace();
      throw new HTTPInternalServerErrorException();
    }
    if(result.size() == 0){
      throw new HTTPNotFoundException();
    }
    return result;
  }

  @RequestMapping(value = "stations", method = RequestMethod.GET)
  public @ResponseBody NearbyStationsResponse getStations(NearbyStationRequest request) throws Exception{

    AbstractEfaProvider provider = new VrrProvider();
    NearbyStationsResponse response = new NearbyStationsResponse();
    final Location requestLocation = Location.coord(request.getLat(), request.getLng());

    try {
      NearbyLocationsResult result = provider.queryNearbyLocations(DEFAULT_LOCATION_SET, requestLocation,
          request.getDistance(), request.getMaxResult());
      if(result.locations.size() <= 0)
      {
        throw new HTTPNotFoundException();
      }

      result.locations.stream().forEachOrdered(location ->
          response.addStation(location.id, String.format("%s %s", location.place, location.name))
      );

      return response;

    } catch (IOException e) {
      e.printStackTrace();
      throw new HTTPInternalServerErrorException();
    }
  }

  @RequestMapping(value = "trip", method = RequestMethod.GET)
  public @ResponseBody RouteDataResponse getTrip(TripRequest request) throws Exception {
    AbstractEfaProvider provider = new VrrProvider();
    List<QueryTripsResult> results = new ArrayList<>();
    List<String> departureIds = new ArrayList<>();

    String destinationId = this.convertStationNameToId(request.getDestination());

    for(String stationId: request.getDepartures()) {
      departureIds.add(this.convertStationNameToId(stationId));
    }

    departureIds.parallelStream().forEach(departureId -> {
      try {
        QueryTripsResult tripsResult = getQueryTripsResult(request.getDepartureTime(), departureId,
            destinationId, provider);
        results.add(tripsResult);
      }catch (IOException e) {
        e.printStackTrace();
      }
    });

    if(results.size() == 0 ) {
      throw new HTTPNotFoundException();
    }

    int sumResults = results.parallelStream().mapToInt(trips -> {
      Optional<List<Trip>> optionalTrips = Optional.ofNullable(trips.trips);
      return optionalTrips.isPresent() ? optionalTrips.get().size() : 0;
    }).sum();

    if(sumResults <= 0){
      throw new HTTPNotFoundException();
    }

    RouteDataResponse response = new RouteDataResponse();

    List<Trip> tripResults = new ArrayList<>();
    results.parallelStream().map(queryTripsResult -> Optional.ofNullable(queryTripsResult.trips)).forEach( trip -> {
          trip.ifPresent(tripResults::addAll);
        }
    );

    List<Trip> trips = orderTripsByDeparture(tripResults);

    trips.stream().forEachOrdered( trip ->
        response.addRoute(createRouteShortInfo(trip))
    );

    return response;
  }

  @Deprecated
  @RequestMapping(value = "nearbyStations", method = RequestMethod.POST)
  public @ResponseBody NearbyStationsResponse getNearbyStations(@RequestBody NearbyStationRequest request) throws Exception{
    return getStations(request);
  }

  @Deprecated
  @RequestMapping(value = "", method = RequestMethod.POST)
  public @ResponseBody RouteDataResponse getRoute(@RequestBody RouteRequest request) throws Exception {
    return getTrip(new TripRequest(request.getDepartureIds(),request.getDestinationId(),request.getDepartureTime()));
  }



  private String convertStationNameToId(String station) throws Exception {
    if(StringUtils.isNumeric(station)) {
      return station;
    }
    return getLocations(station).get(0).id;
  }

  private List<Location> getLocationSuggestions(String location) throws IOException{
    VrrProvider provider = new VrrProvider();
    SuggestLocationsResult locations = provider.suggestLocations(location);
    return locations.getLocations();
  }

  private List<Trip> orderTripsByDeparture(List<Trip> results) {
    List<Trip> tripsOrderedByDeparture = new ArrayList<>();

    tripsOrderedByDeparture.addAll(results);
    tripsOrderedByDeparture.sort(Comparator.comparingLong(trip -> trip.getFirstDepartureTime().getTime()));

    return tripsOrderedByDeparture;
  }

  private QueryTripsResult getQueryTripsResult(Date departureTime, String departureId, String destinationId,
                                               AbstractEfaProvider provider) throws IOException {
    return provider.queryTrips(
            new Location(LocationType.STATION, departureId),null,
            new Location(LocationType.STATION, destinationId),
            departureTime == null ? new Date(): departureTime,
            true, DEFAULT_PRODUCT_SET ,null, NetworkProvider.WalkSpeed.FAST, null, null );
  }

  private static String createRouteShortInfo(Trip trip){
    final StringBuilder result = new StringBuilder();
    result.append(String.format("[%dm] ", trip.getDuration() / 60000));

    for (int counter = 0; counter < trip.legs.size(); counter++){
      Trip.Public leg = (Trip.Public) trip.legs.get(counter);
      // start position
      if(counter <= 0)
      {
        String departureTime = DEFAULT_DATE_FORMAT.format(leg.getDepartureTime());
        result.append(String.format("%s (+%dm) %s (%s) -> ", departureTime, leg.departureStop.getDepartureDelay() / 60000, trip.from.name, leg.line.label));
      }

      if(counter > 0)
      {
        Trip.Public lastLeg = (Trip.Public) trip.legs.get(counter -1);
        String arrivalTime = DEFAULT_DATE_FORMAT.format(lastLeg.getArrivalTime());
        String departureTime = DEFAULT_DATE_FORMAT.format(leg.getDepartureTime());
        String place = defaultIfEmpty(leg.departureStop.location.place,"?");

        result.append(String.format("%s (+%dm) %s. %s -= (%dm) =- %s (+%dm) %s -> ",
            arrivalTime, lastLeg.getArrivalDelay() / 60000, place.substring(0,1), leg.departureStop.location.name,
            (leg.getDepartureTime().getTime() - lastLeg.getArrivalTime().getTime()) / 60000,
            departureTime, leg.getDepartureDelay() / 60000, leg.line.label));
      }

      // end position
      if(counter == trip.legs.size()-1) {
        String arrivalTime = DEFAULT_DATE_FORMAT.format(leg.getArrivalTime());
        String place = defaultIfEmpty(trip.to.place,"?");
        result.append(String.format("%s (+%dm) %s. %s", arrivalTime, leg.getArrivalDelay() / 60000,
            place.substring(0,1), trip.to.name));
      }
    }


    return result.toString();
  }


}
