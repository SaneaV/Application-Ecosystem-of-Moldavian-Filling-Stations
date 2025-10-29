package md.fuel.bot.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserData {

  private long id;
  private String language;
  private double radius;
  private double latitude;
  private double longitude;
  private StationType stationType;
}