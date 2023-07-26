package md.bot.fuel.rest.request;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class FuelStationRequest implements Serializable {

    private double latitude;
    private double longitude;
    private double radius;
    private int limitInRadius;
    private int limit;
    private int offset;

    public void setLimit_in_radius(int limitInRadius) {
        this.limitInRadius = limitInRadius;
    }
}