package md.bot.fuel.infrastructure.aspect;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import md.bot.fuel.domain.FuelStation;
import md.bot.fuel.infrastructure.api.AnreApi;
import md.bot.fuel.infrastructure.api.AnreApiImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AnreTimestampAspectTest {

    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    private static final LocalDateTime TIMESTAMP = LocalDateTime.now();

    @AfterEach
    void resetTimestamp() {
        FuelStation.timestamp = null;
    }

    @Test
    @DisplayName("Test Aspect call for getFuelStationsInfo method")
    public void testAspectCallForGetAllFuelStationsInfoMethod() {
        final AnreApiImpl anreApi = mock(AnreApiImpl.class);
        final AspectJProxyFactory proxyFactory = new AspectJProxyFactory(anreApi);
        proxyFactory.addAspect(AnreTimestampAspect.class);
        final AnreApi proxy = proxyFactory.getProxy();

        final String formattedTimeStamp = TIMESTAMP.format(FORMATTER);
        FuelStation.timestamp = formattedTimeStamp;
        proxy.getFuelStationsInfo();

        verify(anreApi).getFuelStationsInfo();
        assertThat(FuelStation.timestamp).isEqualTo(formattedTimeStamp);
    }
}