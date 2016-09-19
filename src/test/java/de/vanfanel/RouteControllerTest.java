package de.vanfanel;

import de.schildbach.pte.dto.Location;
import de.vanfanel.request.NearbyStationRequest;
import de.vanfanel.request.RouteRequest;
import de.vanfanel.request.TripRequest;
import de.vanfanel.response.NearbyStationsResponse;
import de.vanfanel.response.RouteDataResponse;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;


public class RouteControllerTest {

  public static final String STATION_ID_COLOGNE_HBF = "22000008";
  public static final String STATION_ID_DORTMUND_HBF = "20000131";
  public static final String STATION_ID_DÜSSELDORF = "20018235";


  @Test
  public void testGetNearbyStationsForDuesseldorf () throws Exception
  {
    RouteController routeController = new RouteController();

    NearbyStationRequest request = new NearbyStationRequest(51220000, 6794300,0,20);

    NearbyStationsResponse response = routeController.getNearbyStations(request);

    System.out.println(response);

    assertThat(response.getStations(), hasItem(NearbyStationsResponse.station(STATION_ID_DÜSSELDORF,"Düsseldorf Hbf")));
  }

  @Test
  public void testGetLocationsForSearchString () throws Exception
  {
    RouteController routeController = new RouteController();

    List<Location> locations = routeController.getLocations("Dortmund HauptBahnHof");

    System.out.println(locations);
  }

  @Test
  public void testGetNearbyStationsForCologne () throws Exception
  {
    RouteController routeController = new RouteController();

    NearbyStationRequest request = new NearbyStationRequest(50941357, 6958307,0,20);

    NearbyStationsResponse response = routeController.getStations(request);

    System.out.println(response);

    assertThat(response.getStations(), hasItem(NearbyStationsResponse.station(STATION_ID_COLOGNE_HBF,"Köln Dom / Hbf")));
  }

  @Test
  public void testRouteControllerDuesseldorf () throws Exception
  {
    RouteController routeController = new RouteController();
    List<String> departureIds = new ArrayList<>();
    departureIds.add(STATION_ID_DORTMUND_HBF);
    RouteDataResponse response = routeController.getRoute(new RouteRequest(departureIds,STATION_ID_DÜSSELDORF, new Date()));

    response.routes.stream().forEachOrdered(System.out::println);
  }

  @Test
  public void testResultIsTheSameWithIdAndStationName () throws Exception {
    RouteController routeController = new RouteController();
    List<String> departureIds = new ArrayList<>();
    departureIds.add(STATION_ID_DORTMUND_HBF);
    RouteDataResponse responseById = routeController.getTrip(new TripRequest(departureIds,STATION_ID_DÜSSELDORF, new Date().getTime()));

    List<String> departureNames = new ArrayList<>();
    departureNames.add("Dortmund HBF");
    RouteDataResponse responseByName = routeController.getTrip(new TripRequest(departureNames,STATION_ID_DÜSSELDORF, new Date().getTime()));

    assertThat(responseById, Matchers.equalTo(responseByName));
  }


}
