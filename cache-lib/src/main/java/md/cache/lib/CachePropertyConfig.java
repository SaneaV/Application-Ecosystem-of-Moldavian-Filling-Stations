package md.cache.lib;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CachePropertyConfig {

  private String cacheName;
  private Integer onHeapSize;
  private Integer offHeapSize;
  private Integer expiryTime;
}