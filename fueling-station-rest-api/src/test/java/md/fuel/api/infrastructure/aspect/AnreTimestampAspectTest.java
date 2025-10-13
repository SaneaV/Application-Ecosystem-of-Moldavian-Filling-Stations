package md.fuel.api.infrastructure.aspect;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.infrastructure.repository.AnreApi;
import md.fuel.api.infrastructure.repository.AnreApiImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

public class AnreTimestampAspectTest {

  private static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
  private static final LocalDateTime TIMESTAMP = LocalDateTime.now();

  @AfterEach
  void resetTimestamp() {
    FillingStation.TIMESTAMP = null;
  }

  @Test
  @DisplayName("Test Aspect call for getFillingStationsInfo method")
  public void testAspectCallForGetAllFillingStationsInfoMethod() {
    final AnreApiImpl anreApi = mock(AnreApiImpl.class);
    final AspectJProxyFactory proxyFactory = new AspectJProxyFactory(anreApi);
    proxyFactory.addAspect(AnreTimestampAspect.class);
    final AnreApi proxy = proxyFactory.getProxy();

    final String formattedTimeStamp = TIMESTAMP.format(FORMATTER);
    FillingStation.TIMESTAMP = formattedTimeStamp;
    proxy.getFillingStationsInfo();

    verify(anreApi).getFillingStationsInfo();
    assertThat(FillingStation.TIMESTAMP).isEqualTo(formattedTimeStamp);
  }
}