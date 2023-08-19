package md.fuel.api.infrastructure.aspect;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import md.fuel.api.domain.FillingStation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AnreTimestampAspect {

  private static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

  @After("execution(* md.fuel.api.infrastructure.repository.AnreApiImpl.getFillingStationsInfo())")
  public void setAnreTimestamp() {
    FillingStation.timestamp = FORMATTER.format(LocalDateTime.now());
  }
}
