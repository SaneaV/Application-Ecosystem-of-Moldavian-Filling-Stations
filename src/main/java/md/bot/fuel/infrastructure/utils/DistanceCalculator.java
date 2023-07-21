package md.bot.fuel.infrastructure.utils;

import lombok.NoArgsConstructor;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static lombok.AccessLevel.NONE;

@NoArgsConstructor(access = NONE)
public class DistanceCalculator {

    private static final double radiansToDecimalDegrees = 180.0D / 3.141592653589793D;
    private static final double decimalDegreesToRadians = 3.141592653589793D / 180.0D;
    private static final double decimalDegreesToKilometers = 111189.57696D * radiansToDecimalDegrees;

    public static boolean isWithinRadius(double userLatitude, double userLongitude, double stationLatitude, double stationLongitude, double radius) {
        return calculateMeters(userLatitude, userLongitude, stationLatitude, stationLongitude) <= radius;
    }

    public static double calculateMeters(double userLatitude, double userLongitude, double stationLatitude, double stationLongitude) {
        double x = userLatitude * decimalDegreesToRadians;
        double y = stationLatitude * decimalDegreesToRadians;
        return acos(sin(x) * sin(y) + cos(x) * cos(y) * cos(decimalDegreesToRadians * (userLongitude - stationLongitude))) * decimalDegreesToKilometers;
    }
}
