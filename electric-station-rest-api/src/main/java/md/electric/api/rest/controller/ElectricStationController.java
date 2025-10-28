package md.electric.api.rest.controller;

import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import md.electric.api.facade.ElectricStationFacade;
import md.electric.api.rest.dto.ElectricStationDto;
import md.electric.api.rest.dto.PageDto;
import md.electric.api.rest.request.ElectricStationRequest;
import md.electric.api.rest.request.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class ElectricStationController {

  private final ElectricStationFacade facade;

  @GetMapping(value = "/electric-stations")
  public ResponseEntity<List<ElectricStationDto>> getElectricStations() {
    final List<ElectricStationDto> allChargingStations = facade.getElectricStations();
    return ResponseEntity.ok().body(allChargingStations);
  }

  @GetMapping("/page/electric-stations")
  public ResponseEntity<PageDto<ElectricStationDto>> getElectricStations(@Valid ElectricStationRequest request,
      @Valid PageRequest pageRequest) {
    final PageDto<ElectricStationDto> fillingStationDtoPageDto = facade.getElectricStations(request, pageRequest);

    return ResponseEntity.ok().body(fillingStationDtoPageDto);
  }

  @GetMapping("/electric-stations/last-update")
  public ResponseEntity<ZonedDateTime> getLastUpdate() {
    final ZonedDateTime lastUpdate = facade.getLastUpdatedDateTime();
    return ResponseEntity.ok().body(lastUpdate);
  }
}