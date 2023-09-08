package md.fuel.api.domain;

import java.io.Serializable;

public record FuelPrice(Double petrol, Double diesel, String date) implements Serializable {

}
