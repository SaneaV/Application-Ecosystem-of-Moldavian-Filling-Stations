package md.fuel.api.rest.controller;

import lombok.RequiredArgsConstructor;
import md.fuel.api.facade.FillingStationFacade;
import md.fuel.api.rest.dto.FuelPriceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class AnrePriceControllerImpl implements AnrePriceController {

  private final FillingStationFacade fillingStationFacade;

  @Override
  public ResponseEntity<FuelPriceDto> getAnreFuelPrice() {
    return ResponseEntity.ok(fillingStationFacade.getAnrePrices());
  }
}
