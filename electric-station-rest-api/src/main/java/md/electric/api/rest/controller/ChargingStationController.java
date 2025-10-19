package md.electric.api.rest.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import md.electric.api.facade.ChargingStationFacade;
import md.electric.api.rest.dto.ChargingStationDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class ChargingStationController {

  private final ChargingStationFacade facade;

  @GetMapping("/all-electric-stations")
  public List<ChargingStationDto> getStations(
      @RequestParam(required = false) Double latitude,
      @RequestParam(required = false) Double longitude) {
    return facade.getAllChargingStations(latitude, longitude);
  }
}