package md.fuel.bot.telegram.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDataDto {

  private final long id;
  private final double radius;
  private final double latitude;
  private final double longitude;
}