package md.bot.fuel.infrastructure.aspect;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import md.bot.fuel.domain.FuelStation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AnreTimestampAspect {

    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    @After("execution(* md.bot.fuel.infrastructure.api.AnreApiImpl.getFuelStationsInfo())")
    public void setAnreTimestamp() {
        FuelStation.timestamp = FORMATTER.format(LocalDateTime.now());
    }
}
