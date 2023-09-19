package md.fuel.api.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.domain.FuelPrice;
import md.fuel.api.infrastructure.exception.model.InfrastructureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class AnreApiStubIT {

  @Nested
  @ExtendWith(SpringExtension.class)
  @Import(value = {ObjectMapper.class, AnreApiMapperImpl.class, AnreApiStubImpl.class})
  @TestPropertySource(properties = {"app.anre-stub.enabled=true", "app.anre-stub.file-name=/anre-stub.json"})
  class AnreApiStubFirstCallTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnreApiMapper anreApiMapper;

    @Autowired
    private AnreApi anreApiStub;

    @Test
    @DisplayName("Should return list of stubbed filling stations")
    void shouldReturnListOfStubbedFillingStations() {
      final List<FillingStation> result = anreApiStub.getFillingStationsInfo();
      assertThat(result).hasSize(723);
    }

    @Test
    @DisplayName("Should return stubbed ANRE fuel prices")
    void shouldReturnStubbedAnreFuelPrices() {
      final FuelPrice result = anreApiStub.getAnrePrices();

      assertThat(result.date()).isEqualTo(LocalDate.now().toString());
      assertThat(result.petrol()).isExactlyInstanceOf(Double.class);
      assertThat(result.petrol()).isLessThan(50);
      assertThat(result.diesel()).isExactlyInstanceOf(Double.class);
      assertThat(result.diesel()).isLessThan(50);
    }
  }

  @Nested
  @ExtendWith(SpringExtension.class)
  @Import(value = {ObjectMapper.class, AnreApiMapperImpl.class, AnreApiStubImpl.class})
  @TestPropertySource(properties = {"app.anre-stub.enabled=true", "app.anre-stub.file-name=/"})
  class AnreApiStubResourceNotFoundTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnreApiMapper anreApiMapper;

    @Autowired
    private AnreApi anreApiStub;

    @Test
    @DisplayName("Should catch exception while reading json file")
    void shouldCatchExceptionWhileReadingJsonFile() {
      assertThatThrownBy(() -> anreApiStub.getFillingStationsInfo())
          .isInstanceOf(InfrastructureException.class)
          .hasMessage("Can't read stub ANRE json file.");
    }
  }
}