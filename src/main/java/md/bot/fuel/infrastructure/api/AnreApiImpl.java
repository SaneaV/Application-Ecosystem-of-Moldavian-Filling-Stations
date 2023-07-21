package md.bot.fuel.infrastructure.api;

import java.util.List;
import md.bot.fuel.domain.FuelStation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static md.bot.fuel.infrastructure.configuration.EhcacheConfiguration.ANRE_CACHE;

@Component
public class AnreApiImpl implements AnreApi {

    private final WebClient webClient;
    private final AnreApiMapper mapper;
    private final String anreApiPath;

    public AnreApiImpl(WebClient webClient,
                       AnreApiMapper mapper,
                       @Value("${anre.api.path}") String anreApiPath) {
        this.webClient = webClient;
        this.mapper = mapper;
        this.anreApiPath = anreApiPath;
    }

    @Override
    @Cacheable(value = ANRE_CACHE, cacheManager = "jCacheCacheManager")
    public List<FuelStation> getFuelStationsInfo() {
        final UriComponents anreUri = UriComponentsBuilder.fromUriString(anreApiPath)
                .build();
        return webClient.get()
                .uri(anreUri.toUri())
                .retrieve()
                .bodyToFlux(FuelStationApi.class)
                .map(mapper::toEntity)
                .collectList()
                .block();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fetchFuelStationInfoOnStartup() {
        getFuelStationsInfo();
    }
}
