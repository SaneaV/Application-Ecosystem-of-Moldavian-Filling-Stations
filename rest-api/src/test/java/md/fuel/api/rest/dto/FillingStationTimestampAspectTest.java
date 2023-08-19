package md.fuel.api.rest.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.facade.FillingStationFacade;
import md.fuel.api.facade.FillingStationFacadeImpl;
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
    FillingStation.timestamp = null;
    FillingStationDto.timestamp = null;
  }

  @Test
  @DisplayName("Test Aspect call for getAllFillingStations method")
  public void testAspectCallForGetAllFillingStationsMethod() {
    final FillingStationFacade FillingStationFacade = mock(FillingStationFacadeImpl.class);
    final AspectJProxyFactory proxyFactory = new AspectJProxyFactory(FillingStationFacade);
    proxyFactory.addAspect(FillingStationTimestampAspect.class);
    final FillingStationFacade proxy = proxyFactory.getProxy();

    FillingStation.timestamp = TIMESTAMP;
    proxy.getAllFillingStations(LATITUDE, LONGITUDE, RADIUS, LIMIT);

    verify(FillingStationFacade).getAllFillingStations(LATITUDE, LONGITUDE, RADIUS, LIMIT);
    assertThat(FillingStationDto.timestamp).isEqualTo(TIMESTAMP);
  }

  @Test
  @DisplayName("Test Aspect call for getNearestFillingStation method")
  public void testAspectCallForGetNearestFillingStationMethod() {
    final FillingStationFacade FillingStationFacade = mock(FillingStationFacadeImpl.class);
    final AspectJProxyFactory proxyFactory = new AspectJProxyFactory(FillingStationFacade);
    proxyFactory.addAspect(FillingStationTimestampAspect.class);
    final FillingStationFacade proxy = proxyFactory.getProxy();

    FillingStation.timestamp = TIMESTAMP;
    proxy.getNearestFillingStation(LATITUDE, LONGITUDE, RADIUS);

    verify(FillingStationFacade).getNearestFillingStation(LATITUDE, LONGITUDE, RADIUS);
    assertThat(FillingStationDto.timestamp).isEqualTo(TIMESTAMP);
  }

  @Test
  @DisplayName("Test Aspect call for getBestFuelPrice method")
  public void testAspectCallForGetBestFuelTypeMethod() {
    final FillingStationFacade FillingStationFacade = mock(FillingStationFacadeImpl.class);
    final AspectJProxyFactory proxyFactory = new AspectJProxyFactory(FillingStationFacade);
    proxyFactory.addAspect(FillingStationTimestampAspect.class);
    final FillingStationFacade proxy = proxyFactory.getProxy();

    FillingStation.timestamp = TIMESTAMP;
    proxy.getBestFuelPrice(LATITUDE, LONGITUDE, RADIUS, FUEL_TYPE, LIMIT);

    verify(FillingStationFacade).getBestFuelPrice(LATITUDE, LONGITUDE, RADIUS, FUEL_TYPE, LIMIT);
    assertThat(FillingStationDto.timestamp).isEqualTo(TIMESTAMP);
  }
}