package md.electric.api.infrastructure.aspect;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import md.electric.api.domain.ElectricStation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PlugShareTimestampAspect {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @After("execution(* md.electric.api.infrastructure.client.plugshare.PlugShareClient.fetchStations(..))")
  public void setPlugShareTimestamp() {
    if (isNull(ElectricStation.TIMESTAMP)) {
      final String timestamp = LocalDateTime.now().format(FORMATTER);
      ElectricStation.TIMESTAMP = timestamp;
      log.info("Set new PlugShare timestamp: {}", timestamp);
    }
  }
}

