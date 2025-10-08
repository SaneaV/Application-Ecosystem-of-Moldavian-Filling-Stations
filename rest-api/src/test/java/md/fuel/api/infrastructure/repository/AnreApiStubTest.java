//package md.fuel.api.infrastructure.repository;
//
//import static java.util.Collections.singletonList;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.clearInvocations;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.verifyNoInteractions;
//import static org.mockito.Mockito.when;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//import md.fuel.api.domain.FillingStation;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//@Nested
//@ExtendWith(SpringExtension.class)
//@Import(value = {ObjectMapper.class, AnreApiMapperImpl.class, AnreApiStubImpl.class})
//@TestPropertySource(properties = {"app.anre-stub.enabled=true", "app.anre-stub.file-name=/anre-stub.json"})
//public class AnreApiStubTest {
//
//  @MockBean
//  private ObjectMapper objectMapper;
//
//  @MockBean
//  private AnreApiMapper anreApiMapper;
//
//  @Autowired
//  private AnreApiStubImpl anreApiStub;
//
//  @Test
//  @DisplayName("Should return list of stubbed filling stations twice")
//  void shouldReturnListOfStubbedFillingStationsTwice() throws IOException {
//    final FillingStationApi fillingStationApi = new FillingStationApi("name", 0.0, null, 0.0D, 40, 40);
//
//    when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class))).thenReturn(singletonList(fillingStationApi));
//
//    final List<FillingStation> firstCall = anreApiStub.getFillingStationsInfo();
//
//    assertThat(firstCall).hasSize(1);
//    verify(objectMapper).readValue(any(InputStream.class), any(TypeReference.class));
//    verify(anreApiMapper).toEntity(any(FillingStationApi.class));
//
//    clearInvocations(objectMapper);
//    clearInvocations(anreApiMapper);
//
//    final List<FillingStation> secondCall = anreApiStub.getFillingStationsInfo();
//
//    assertThat(secondCall).hasSize(1);
//    verifyNoInteractions(objectMapper);
//    verifyNoInteractions(anreApiMapper);
//  }
//}