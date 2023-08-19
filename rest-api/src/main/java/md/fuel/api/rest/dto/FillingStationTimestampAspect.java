package md.fuel.api.rest.dto;

import md.fuel.api.domain.FillingStation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FillingStationTimestampAspect {

  @After("execution(* md.fuel.api.facade.FillingStationFacadeImpl.*(..))")
  public void setFillingStationTimestampAspect() {
    FillingStationDto.timestamp = FillingStation.timestamp;
  }
}
