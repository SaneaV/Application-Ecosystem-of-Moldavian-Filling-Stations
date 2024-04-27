package md.fuel.api.rest.aspect;

import lombok.extern.slf4j.Slf4j;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.rest.dto.FillingStationDto;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class FillingStationTimestampAspect {

  @After("execution(* md.fuel.api.facade.FillingStationFacadeImpl.*(..))")
  public void setFillingStationTimestampAspect() {
    log.info("Set new ANRE timestamp for DTO object: {}", FillingStation.TIMESTAMP);

    FillingStationDto.TIMESTAMP = FillingStation.TIMESTAMP;
  }
}