package md.fuel.api.rest.aspect;

import md.fuel.api.domain.FillingStation;
import md.fuel.api.rest.dto.FillingStationDto;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FillingStationTimestampAspect {

  @After("execution(* md.fuel.api.facade.FillingStationFacadeImpl.*(..))")
  public void setFillingStationTimestampAspect() {
    FillingStationDto.TIMESTAMP = FillingStation.TIMESTAMP;
  }
}
