package md.bot.fuel.facade.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import md.bot.fuel.domain.FuelStation;
import md.bot.fuel.facade.FuelStationFacade;
import md.bot.fuel.facade.FuelStationFacadeImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

public class FuelStationTimestampAspectTest {

  private static final double LATITUDE = 10;
  private static final double LONGITUDE = 20;
  private static final double RADIUS = 30;
  private static final int LIMIT = 50;
  private static final String FUEL_TYPE = "Petrol";
  private static final String TIMESTAMP = LocalDateTime.now().toString();

  @AfterEach
  void resetTimestamp() {
    FuelStation.timestamp = null;
    FuelStationDto.timestamp = null;
  }

  @Test
  @DisplayName("Test Aspect call for getAllFuelStations method")
  public void testAspectCallForGetAllFuelStationsMethod() {
    final FuelStationFacade fuelStationFacade = mock(FuelStationFacadeImpl.class);
    final AspectJProxyFactory proxyFactory = new AspectJProxyFactory(fuelStationFacade);
    proxyFactory.addAspect(FuelStationTimestampAspect.class);
    final FuelStationFacade proxy = proxyFactory.getProxy();

    FuelStation.timestamp = TIMESTAMP;
    proxy.getAllFuelStations(LATITUDE, LONGITUDE, RADIUS, LIMIT);

    verify(fuelStationFacade).getAllFuelStations(LATITUDE, LONGITUDE, RADIUS, LIMIT);
    assertThat(FuelStationDto.timestamp).isEqualTo(TIMESTAMP);
  }

  @Test
  @DisplayName("Test Aspect call for getNearestFuelStation method")
  public void testAspectCallForGetNearestFuelStationMethod() {
    final FuelStationFacade fuelStationFacade = mock(FuelStationFacadeImpl.class);
    final AspectJProxyFactory proxyFactory = new AspectJProxyFactory(fuelStationFacade);
    proxyFactory.addAspect(FuelStationTimestampAspect.class);
    final FuelStationFacade proxy = proxyFactory.getProxy();

    FuelStation.timestamp = TIMESTAMP;
    proxy.getNearestFuelStation(LATITUDE, LONGITUDE, RADIUS);

    verify(fuelStationFacade).getNearestFuelStation(LATITUDE, LONGITUDE, RADIUS);
    assertThat(FuelStationDto.timestamp).isEqualTo(TIMESTAMP);
  }

  @Test
  @DisplayName("Test Aspect call for getBestFuelPrice method")
  public void testAspectCallForGetBestFuelTypeMethod() {
    final FuelStationFacade fuelStationFacade = mock(FuelStationFacadeImpl.class);
    final AspectJProxyFactory proxyFactory = new AspectJProxyFactory(fuelStationFacade);
    proxyFactory.addAspect(FuelStationTimestampAspect.class);
    final FuelStationFacade proxy = proxyFactory.getProxy();

    FuelStation.timestamp = TIMESTAMP;
    proxy.getBestFuelPrice(LATITUDE, LONGITUDE, RADIUS, FUEL_TYPE, LIMIT);

    verify(fuelStationFacade).getBestFuelPrice(LATITUDE, LONGITUDE, RADIUS, FUEL_TYPE, LIMIT);
    assertThat(FuelStationDto.timestamp).isEqualTo(TIMESTAMP);
  }
}