package md.bot.fuel.facade.dto;

import md.bot.fuel.domain.FuelStation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FuelStationTimestampAspect {

    @After("execution(* md.bot.fuel.facade.FuelStationFacadeImpl.*(..))")
    public void setFuelStationAspect() {
        FuelStationDto.timestamp = FuelStation.timestamp;
    }
}
