package de.vanfanel;

import de.vanfanel.request.NearbyStationRequest;
import de.vanfanel.request.RouteRequest;
import de.vanfanel.response.NearbyStationsResponse;
import de.vanfanel.response.RouteDataResponse;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;


public class RouteControllerTest {

  public static final String STATION_ID_COLOGNE_HBF = "22000008";
  public static final String STATION_ID_DORTMUND_HBF = "20009289";
  public static final String STATION_ID_DÜSSELDORF = "20018235";


  @Test
  @Ignore
  public void testGetNearbyStationsForDuesseldorf () throws Exception
  {
    RouteController routeController = new RouteController();

    NearbyStationRequest request = new NearbyStationRequest(51220000, 6794300,0,20);

    NearbyStationsResponse response = routeController.getNearbyStations(request);

    System.out.println(response);

    assertThat(response.getStations(), hasItem(NearbyStationsResponse.station(STATION_ID_DÜSSELDORF,"Düsseldorf Hbf")));
  }

  @Test
  @Ignore
  public void testGetNearbyStationsForCologne () throws Exception
  {
    RouteController routeController = new RouteController();

    NearbyStationRequest request = new NearbyStationRequest(50941357, 6958307,0,20);

    NearbyStationsResponse response = routeController.getNearbyStations(request);

    System.out.println(response);

    assertThat(response.getStations(), hasItem(NearbyStationsResponse.station(STATION_ID_COLOGNE_HBF,"Köln Dom / Hbf")));
  }

  @Test
  @Ignore
  public void testRouteControllerDuesseldorf () throws Exception
  {
    RouteController routeController = new RouteController();
    List<String> departureIds = new ArrayList<>();
    departureIds.add(STATION_ID_DORTMUND_HBF);
    RouteDataResponse response = routeController.getRoute(new RouteRequest(departureIds,STATION_ID_DÜSSELDORF, new Date()));

    response.routes.stream().forEachOrdered(System.out::println);
  }
}
