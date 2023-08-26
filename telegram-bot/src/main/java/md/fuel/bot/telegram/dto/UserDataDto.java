package md.fuel.bot.telegram.dto;

public record UserDataDto(
    long id,
    double radius,
    double latitude,
    double longitude) {

}