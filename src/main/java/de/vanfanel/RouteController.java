package de.vanfanel;


import de.schildbach.pte.AbstractEfaProvider;
import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.VrrProvider;
import de.schildbach.pte.dto.*;
import de.vanfanel.exceptions.HTTPInternalServerErrorException;
import de.vanfanel.exceptions.HTTPNotFoundException;
import de.vanfanel.request.NearbyStationRequest;
import de.vanfanel.request.RouteRequest;
import de.vanfanel.response.NearbyStationsResponse;
import de.vanfanel.response.RouteDataResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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


  @RequestMapping(value = "getNearbyStations", method = RequestMethod.POST)
  public @ResponseBody
  NearbyStationsResponse getNearbyStations(@RequestBody NearbyStationRequest request) throws Exception{

    AbstractEfaProvider provider = new VrrProvider();
    NearbyStationsResponse response = new NearbyStationsResponse();
    final Location requestLocation = Location.coord(request.getLat(), request.getLng());

    try {
      NearbyLocationsResult result = provider.queryNearbyLocations(DEFAULT_LOCATION_SET, requestLocation, request.getDistance(), request.getMaxResult());
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

  @RequestMapping(value = "", method = RequestMethod.POST)
  public @ResponseBody
  RouteDataResponse getRoute(@RequestBody RouteRequest request) throws Exception {

    AbstractEfaProvider provider = new VrrProvider();

    List<QueryTripsResult> results = new ArrayList<>();

    request.getDepartureIds().parallelStream().forEach(departureId -> {
      try {
        QueryTripsResult tripsResult = getQueryTripsResult(request.getDepartureTime(), departureId,
            request.getDestinationId(), provider);
        results.add(tripsResult);
      }catch (IOException e) {
        e.printStackTrace();
      }
    });

    int sumResults = results.parallelStream().mapToInt(trips -> trips.trips.size()).sum();

    if(sumResults <= 0){
      throw new HTTPNotFoundException();
    }

    RouteDataResponse response = new RouteDataResponse();

    List<Trip> tripResults = new ArrayList<>();
    results.parallelStream().map(queryTripsResult -> queryTripsResult.trips).forEach(tripResults::addAll);

    List<Trip> trips = orderTripsByDeparture(tripResults);

    trips.stream().forEachOrdered( trip ->
        response.addRoute(createRouteShortInfo(trip))
    );

    return response;
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
