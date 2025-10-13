package md.fuel.api.rest.dto;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.facade.FillingStationFacade;
import md.fuel.api.facade.FillingStationFacadeImpl;
import md.fuel.api.rest.aspect.FillingStationTimestampAspect;
import md.fuel.api.rest.request.BaseFillingStationRequest;
import md.fuel.api.rest.request.LimitFillingStationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

public class FillingStationTimestampAspectTest {

  private static final double LATITUDE = 10;
  private static final double LONGITUDE = 20;
  private static final double RADIUS = 30;
  private static final int LIMIT = 50;
  private static final String FUEL_TYPE = "Petrol";
  private static final String TIMESTAMP = LocalDateTime.now().toString();

  @AfterEach
  void resetTimestamp() {
    FillingStation.TIMESTAMP = null;
    FillingStationDto.TIMESTAMP = null;
  }

  @Test
  @DisplayName("Test Aspect call for getAllFillingStations method")
  public void testAspectCallForGetAllFillingStationsMethod() {
    final FillingStationFacade FillingStationFacade = mock(FillingStationFacadeImpl.class);
    final AspectJProxyFactory proxyFactory = new AspectJProxyFactory(FillingStationFacade);
    proxyFactory.addAspect(FillingStationTimestampAspect.class);
    final FillingStationFacade proxy = proxyFactory.getProxy();
    final LimitFillingStationRequest request = buildLimitRequest();

    FillingStation.TIMESTAMP = TIMESTAMP;
    proxy.getAllFillingStations(request);

    verify(FillingStationFacade).getAllFillingStations(request);
    assertThat(FillingStationDto.TIMESTAMP).isEqualTo(TIMESTAMP);
  }

  @Test
  @DisplayName("Test Aspect call for getNearestFillingStation method")
  public void testAspectCallForGetNearestFillingStationMethod() {
    final FillingStationFacade FillingStationFacade = mock(FillingStationFacadeImpl.class);
    final AspectJProxyFactory proxyFactory = new AspectJProxyFactory(FillingStationFacade);
    proxyFactory.addAspect(FillingStationTimestampAspect.class);
    final FillingStationFacade proxy = proxyFactory.getProxy();
    final BaseFillingStationRequest request = buildBaseRequest();

    FillingStation.TIMESTAMP = TIMESTAMP;
    proxy.getNearestFillingStation(request);

    verify(FillingStationFacade).getNearestFillingStation(request);
    assertThat(FillingStationDto.TIMESTAMP).isEqualTo(TIMESTAMP);
  }

  @Test
  @DisplayName("Test Aspect call for getBestFuelPrice method")
  public void testAspectCallForGetBestFuelTypeMethod() {
    final FillingStationFacade FillingStationFacade = mock(FillingStationFacadeImpl.class);
    final AspectJProxyFactory proxyFactory = new AspectJProxyFactory(FillingStationFacade);
    proxyFactory.addAspect(FillingStationTimestampAspect.class);
    final FillingStationFacade proxy = proxyFactory.getProxy();
    final LimitFillingStationRequest request = buildLimitRequest();

    FillingStation.TIMESTAMP = TIMESTAMP;
    proxy.getBestFuelPrice(request, FUEL_TYPE);

    verify(FillingStationFacade).getBestFuelPrice(request, FUEL_TYPE);
    assertThat(FillingStationDto.TIMESTAMP).isEqualTo(TIMESTAMP);
  }

  private LimitFillingStationRequest buildLimitRequest() {
    final LimitFillingStationRequest request = new LimitFillingStationRequest();
    request.setLatitude(LATITUDE);
    request.setLongitude(LONGITUDE);
    request.setRadius(RADIUS);
    request.setLimit_in_radius(LIMIT);
    request.setSorting(emptyList());
    return request;
  }

  private BaseFillingStationRequest buildBaseRequest() {
    final BaseFillingStationRequest request = new BaseFillingStationRequest();
    request.setLatitude(LATITUDE);
    request.setLongitude(LONGITUDE);
    request.setRadius(RADIUS);
    return request;
  }
}