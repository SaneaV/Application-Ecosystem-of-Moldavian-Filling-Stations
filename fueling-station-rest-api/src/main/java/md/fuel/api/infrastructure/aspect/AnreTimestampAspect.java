package md.fuel.api.infrastructure.aspect;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import md.fuel.api.domain.FillingStation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class AnreTimestampAspect {

  private static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

  @After("execution(* md.fuel.api.infrastructure.repository.AnreApiImpl.getFillingStationsInfo())")
  public void setAnreTimestamp() {
    final String timestamp = FORMATTER.format(LocalDateTime.now());

    log.info("Set new ANRE timestamp: {}", timestamp);

    FillingStation.TIMESTAMP = timestamp;
  }
}